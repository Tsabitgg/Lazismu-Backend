package com.ict.careus.service;

import com.ict.careus.dto.request.LoginRequest;
import com.ict.careus.dto.request.SignupRequest;
import com.ict.careus.dto.response.JwtResponse;
import com.ict.careus.enumeration.ERole;
import com.ict.careus.model.user.Role;
import com.ict.careus.model.user.User;
import com.ict.careus.repository.RoleRepository;
import com.ict.careus.repository.UserRepository;
import com.ict.careus.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
        // Autentikasi pengguna
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword()));

        // Set autentikasi ke dalam konteks keamanan
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mendapatkan informasi pengguna yang terotentikasi
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generate token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Ambil daftar peran pengguna
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> ((SimpleGrantedAuthority) authority).getAuthority())
                .collect(Collectors.toList());


        // Buat dan kembalikan respons JWT
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getPhoneNumber(), roles);
    }



    @Override
    public User registerUser(SignupRequest signUpRequest) throws BadRequestException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Error: Username is already taken!");
        }

        if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new BadRequestException("Error: PhoneNumber is already in use!");
        }

        Role defaultRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Error: Default Role is not found."));

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getPhoneNumber(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAddress(),
                defaultRole);

        user.setProfilePicture("https://res.cloudinary.com/donation-application/image/upload/v1711632747/default-avatar-icon-of-social-media-user-vector_thrtbz.jpg");

        if (signUpRequest.getRole() != null) {
            if (signUpRequest.getRole().contains("admin") || signUpRequest.getRole().contains("ADMIN")) {
                Role adminRole = roleRepository.findByName(ERole.ADMIN)
                        .orElseThrow(() -> new BadRequestException("Error: Role is not found."));
                user.setRole(adminRole);
            } else if (signUpRequest.getRole().contains("sub admin") || signUpRequest.getRole().contains("SUB ADMIN")) {
                Role subAdminRole = roleRepository.findByName(ERole.SUB_ADMIN)
                        .orElseThrow(() -> new BadRequestException("Error: Role is not found."));
                user.setRole(subAdminRole);
            }
        }

        userRepository.save(user);

        return user;
    }
}
