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
import com.ict.careus.security.jwt.JwtTokenExtractor;
import com.ict.careus.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    private JwtTokenExtractor jwtTokenExtractor;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Transaction createTransaction(String transactionType, String code, TransactionRequest transactionRequest) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Baca token dari cookie
        String jwtToken = jwtTokenExtractor.extractJwtTokenFromCookie(request);

        // Inisialisasi variabel pengguna
        User user = null;

        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            // Jika pengguna sudah login
            String phoneNumber = jwtTokenExtractor.getPhoneNumberFromJwtToken(jwtToken);
            user = userRepository.findByPhoneNumber(phoneNumber);
        }

        // Validasi username dan phoneNumber
        if (user == null && (transactionRequest.getUsername() == null || transactionRequest.getPhoneNumber() == null)) {
            throw new RuntimeException("Username and phoneNumber cannot be null for new user");
        }


        // Jika pengguna tidak ditemukan, buat pengguna baru
        if (user == null) {
            user = new User();
            user.setUsername(transactionRequest.getUsername());
            user.setPhoneNumber(transactionRequest.getPhoneNumber());
            // Generate password sementara (bisa diganti)
            String password = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String encodedPassword = encoder.encode(password);
            user.setPassword(encodedPassword);
            // Atur peran pengguna (misalnya, USER)
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRole(userRole);
            user.setCreatedAt(new Date());
            user = userRepository.save(user);
        }

        // Buat objek transaksi
        Transaction transaction = modelMapper.map(transactionRequest, Transaction.class);
        transaction.setUser(user);

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


    @Override
    public List<CampaignTransactionsHistoryResponse> getCampaignTransactionsHistory(Campaign campaign) {
        List<Transaction> campaignTransactions = transactionRepository.findByCampaign(campaign);
        return campaignTransactionsDTO(campaignTransactions);
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
    public Map<String, Double> getUserTransactionSummary() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Baca token dari cookie
        String jwtToken = jwtTokenExtractor.extractJwtTokenFromCookie(request);

        // Validasi token dan ambil email pengguna dari token
        String userPhoneNumber = jwtTokenExtractor.getPhoneNumberFromJwtToken(jwtToken);

        // Cari pengguna berdasarkan email
        User existingUser = userRepository.findByPhoneNumber(userPhoneNumber);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        long userId = existingUser.getId();

        return transactionRepository.getUserTransactionSummary(userId);
    }

    @Override
    public Map<String, Double> getUserTransactionSummaryByYear(int year) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Baca token dari cookie
        String jwtToken = jwtTokenExtractor.extractJwtTokenFromCookie(request);

        // Validasi token dan ambil email pengguna dari token
        String userPhoneNumber = jwtTokenExtractor.getPhoneNumberFromJwtToken(jwtToken);

        // Cari pengguna berdasarkan email
        User existingUser = userRepository.findByPhoneNumber(userPhoneNumber);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        long userId = existingUser.getId();
        return transactionRepository.getUserTransactionSummaryByYear(userId, year);
    }

    private  List<CampaignTransactionsHistoryResponse> campaignTransactionsDTO(List<Transaction> transactions){
        List<CampaignTransactionsHistoryResponse> campaignTransactionsHistory = new ArrayList<>();
        for (Transaction transaction : transactions){
            CampaignTransactionsHistoryResponse campaignTransactionsDTO = new CampaignTransactionsHistoryResponse();
            campaignTransactionsDTO.setUsername(transaction.getUsername());
            campaignTransactionsDTO.setTransactionAmount(transaction.getTransactionAmount());
            campaignTransactionsDTO.setMessage(transaction.getMessage());
            campaignTransactionsDTO.setTransactionDate(transaction.getTransactionDate());

            campaignTransactionsHistory.add(campaignTransactionsDTO);
        }
        return campaignTransactionsHistory;
    }

    @Override
    public List<UserTransactionsHistoryResponse> getUserTransactionsHistory() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Baca token dari cookie
        String jwtToken = jwtTokenExtractor.extractJwtTokenFromCookie(request);

        // Validasi token dan ambil email pengguna dari token
        String userPhoneNumber = jwtTokenExtractor.getPhoneNumberFromJwtToken(jwtToken);

        // Cari pengguna berdasarkan email
        User existingUser = userRepository.findByPhoneNumber(userPhoneNumber);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }
        List<Transaction> userTransactions = transactionRepository.findByUser(existingUser);
        return userTransacctionsDTO(userTransactions);
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

