package com.ict.careus.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ict.careus.dto.request.EditProfileRequest;
import com.ict.careus.model.user.User;
import com.ict.careus.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public User editProfile(EditProfileRequest editProfileRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingUser.setUsername(editProfileRequest.getUsername());
            existingUser.setPhoneNumber(editProfileRequest.getPhoneNumber());
            existingUser.setPassword(encoder.encode(editProfileRequest.getPassword()));
            existingUser.setAddress(editProfileRequest.getAddress());
            existingUser.getRole();

            // Validasi username dan phoneNumber
            if (editProfileRequest.getUsername() == null || editProfileRequest.getPhoneNumber() == null) {
                throw new RuntimeException("Username and phoneNumber cannot be null");
            }

            if (editProfileRequest.getProfilePicture() != null && !editProfileRequest.getProfilePicture().isEmpty()) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(
                            editProfileRequest.getProfilePicture().getBytes(),
                            ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("url").toString();
                    existingUser.setProfilePicture(imageUrl);
                } catch (IOException e) {
                    throw new BadRequestException("Error uploading image", e);
                }
            }

            return userRepository.save(existingUser);
        }
        throw new RuntimeException("User not found");
    }
}
