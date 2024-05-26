package com.ict.careus.service;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.dto.response.CampaignTransactionsHistoryResponse;
import com.ict.careus.dto.response.TransactionResponse;
import com.ict.careus.dto.response.UserTransactionsHistoryResponse;
import com.ict.careus.enumeration.ERole;
import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.model.user.Role;
import com.ict.careus.model.user.User;
import com.ict.careus.model.ziswaf.Infak;
import com.ict.careus.model.ziswaf.Wakaf;
import com.ict.careus.model.ziswaf.Zakat;
import com.ict.careus.repository.*;
import com.ict.careus.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Transaction createTransaction(String transactionType, String code, TransactionRequest transactionRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user;

        // Jika tidak ada autentikasi atau autentikasi bukan dari UserDetailsImpl, maka buat pengguna baru
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            // Validasi username dan phoneNumber
            if (transactionRequest.getUsername() == null || transactionRequest.getPhoneNumber() == null) {
                throw new BadRequestException("Username and phoneNumber cannot be null for new user");
            }

            // Buat pengguna baru
            user = new User();
            user.setUsername(transactionRequest.getUsername());
            user.setPhoneNumber(transactionRequest.getPhoneNumber());
            String password = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String encodedPassword = encoder.encode(password);
            user.setPassword(encodedPassword);
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRole(userRole);
            user.setCreatedAt(new Date());
            user = userRepository.save(user);
        } else {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            user = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));
        }

        // Buat objek transaksi
        Transaction transaction = modelMapper.map(transactionRequest, Transaction.class);
        transaction.setUser(user);
        transaction.setUsername(user.getUsername());
        transaction.setPhoneNumber(user.getPhoneNumber());

        // Melakukan switch berdasarkan tipe transaksi
        switch (transactionType) {
            case "campaign":
                Campaign campaign = campaignRepository.findByCampaignCode(code);
                if (campaign != null) {
                    transaction.setCampaign(campaign);
                } else {
                    throw new RuntimeException("Campaign not found with code: " + code);
                }
                break;
            case "zakat":
                Zakat zakat = zakatRepository.findByZakatCode(code);
                if (zakat != null) {
                    transaction.setZakat(zakat);
                } else {
                    throw new RuntimeException("Zakat not found with code: " + code);
                }
                break;
            case "infak":
                Infak infak = infakRepository.findByInfakCode(code);
                if (infak != null) {
                    transaction.setInfak(infak);
                } else {
                    throw new RuntimeException("Infak not found with code: " + code);
                }
                break;
            case "wakaf":
                Wakaf wakaf = wakafRepository.findByWakafCode(code);
                if (wakaf != null) {
                    transaction.setWakaf(wakaf);
                } else {
                    throw new RuntimeException("Wakaf not found with code: " + code);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }

        // Atur tanggal transaksi dan kategori
        transaction.setTransactionDate(new Date());
        transaction.setCategory(transactionType);
        transaction.setSuccess(true);

        // Simpan transaksi ke dalam database
        transaction = transactionRepository.save(transaction);

    // Update jumlah transaksi terkait berdasarkan tipe transaksi
        switch (transactionType) {
            case "campaign":
                transactionRepository.update_campaign_current_amount(code, transaction.getTransactionAmount());
                break;
            case "zakat":
                transactionRepository.update_zakat_amount(code, transaction.getTransactionAmount());
                break;
            case "infak":
                transactionRepository.update_infak_amount(code, transaction.getTransactionAmount());
                break;
            case "wakaf":
                transactionRepository.update_wakaf_amount(code, transaction.getTransactionAmount());
                break;
        }

        return transaction;
    }

//    @Override
//    public byte[] generateQRCode(long transactionId) throws BadRequestException {
//        Transaction transaction = transactionRepository.findById(transactionId)
//                .orElseThrow(() -> new BadRequestException("Transaction not found"));
//
//        String vaNumber = null;
//
//        switch (transaction.getCategory()) {
//            case "campaign":
//                vaNumber = transaction.getCampaign().getVaNumber();
//                break;
//            case "zakat":
//                vaNumber = transaction.getZakat().getVaNumber();
//                break;
//            case "infak":
//                vaNumber = transaction.getInfak().getVaNumber();
//                break;
//            case "wakaf":
//                vaNumber = transaction.getWakaf().getVaNumber();
//                break;
//        }
//
//        // Generate QR Code
//        ByteArrayOutputStream stream = QRCode.from(vaNumber).to(ImageType.PNG).stream();
//        return stream.toByteArray();
//    }


    @Override
    public Page<CampaignTransactionsHistoryResponse> getCampaignTransactionsHistory(Campaign campaign, Pageable pageable) {
        Page<Transaction> campaignTransactions = transactionRepository.findByCampaign(campaign, pageable);
        return campaignTransactions.map(this::campaignTransactionsDTO);
    }

    @Override
    public Page<TransactionResponse> getAllTransaction(int year, Pageable pageable) {
        return transactionRepository.findByYear(year, pageable);
    }


    @Override
    public double getTotalTransactionCount() {
        return transactionRepository.totalTransactionCount();
    }

    @Override
    public double getTotalDonationCampaign(){
        return transactionRepository.totalDonationCampaign();
    }

    @Override
    public Map<String, Double> getUserTransactionSummary() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            long userId = existingUser.getUserId();
            return transactionRepository.getUserTransactionSummary(userId);
        }
        throw new BadRequestException("User not found");
    }


    @Override
    public Map<String, Double> getUserTransactionSummaryByYear(int year) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            long userId = existingUser.getUserId();
            return transactionRepository.getUserTransactionSummaryByYear(userId, year);
        }
        throw new BadRequestException("User not found");
    }

    @Override
    public Optional<Transaction> getTransactionById(long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    private CampaignTransactionsHistoryResponse campaignTransactionsDTO(Transaction transaction) {
        CampaignTransactionsHistoryResponse campaignTransactionsDTO = new CampaignTransactionsHistoryResponse();
        campaignTransactionsDTO.setUsername(transaction.getUsername());
        campaignTransactionsDTO.setTransactionAmount(transaction.getTransactionAmount());
        campaignTransactionsDTO.setMessage(transaction.getMessage());
        campaignTransactionsDTO.setTransactionDate(transaction.getTransactionDate());

        return campaignTransactionsDTO;
    }

    @Override
    public List<UserTransactionsHistoryResponse> getUserTransactionsHistory() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User existingUser = userRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            List<Transaction> userTransactions = transactionRepository.findByUser(existingUser);
            return userTransacctionsDTO(userTransactions);
        }
        throw new BadRequestException("User not found");
    }

    private List<UserTransactionsHistoryResponse> userTransacctionsDTO(List<Transaction> transactions) {
        List<UserTransactionsHistoryResponse> userTransactionsHistory = new ArrayList<>();
        for (Transaction transaction : transactions) {
            UserTransactionsHistoryResponse transactionDTO = new UserTransactionsHistoryResponse();
            transactionDTO.setUsername(transaction.getUsername());
            transactionDTO.setTransactionAmount(transaction.getTransactionAmount());
            transactionDTO.setMessage(transaction.getMessage());
            transactionDTO.setTransactionDate(transaction.getTransactionDate());
            transactionDTO.setSuccess(transaction.isSuccess());

            if (transaction.getCampaign() != null) {
                transactionDTO.setType("Campaign");
                transactionDTO.setTransactionName(transaction.getCampaign().getCampaignName());
            } else if (transaction.getZakat() != null) {
                transactionDTO.setType("Zakat");
                transactionDTO.setTransactionName(transaction.getZakat().getZakatCategory().name());
            } else if (transaction.getInfak() != null) {
                transactionDTO.setType("Infak");
                transactionDTO.setTransactionName(transaction.getInfak().getInfakCategory().name());
            } else if (transaction.getWakaf() != null) {
                transactionDTO.setType("Wakaf");
                transactionDTO.setTransactionName(transaction.getWakaf().getWakafCategory().name());
            }

            userTransactionsHistory.add(transactionDTO);
        }
        return userTransactionsHistory;
    }

}

