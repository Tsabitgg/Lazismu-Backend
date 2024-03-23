package com.ict.careus.service;

import com.ict.careus.dto.request.TransactionRequest;
import com.ict.careus.dto.response.CampaignTransactionsHistoryResponse;
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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public Transaction createTransaction(String transactionType, String code, TransactionRequest transactionRequest) {
        Transaction transaction = modelMapper.map(transactionRequest, Transaction.class);

        Optional<User> userOptional = userRepository.findByPhoneNumber(transaction.getPhoneNumber());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            String password = new SimpleDateFormat("yyyyMMdd").format(new Date());
            user = new User();
            user.setUsername(transaction.getUsername());
            user.setPhoneNumber(transaction.getPhoneNumber());
            String encodePassword = encoder.encode(password);
            user.setPassword(encodePassword);
            user.setRoles(Collections.singleton(roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."))));
            userRepository.save(user);
        }

        transaction.setUser(user);

        switch (transactionType) {
            case "campaign":
                Campaign campaign = campaignRepository.findByCampaignCode(code);
                if (campaign != null) {
                    transaction.setCampaign(campaign);
                    break;
                } else {
                    throw new RuntimeException("Campaign not found with code: " + code);
                }
            case "zakat":
                Zakat zakat = zakatRepository.findByZakatCode(code);
                if (zakat != null) {
                    transaction.setZakat(zakat);
                    break;
                } else {
                    throw new RuntimeException("Zakat not found with code: " + code);
                }
            case "infak":
                Infak infak = infakRepository.findByInfakCode(code);
                if (infak != null) {
                    transaction.setInfak(infak);
                    break;
                } else {
                    throw new RuntimeException("infak not found with code: " + code);
                }
            case "wakaf":
                Wakaf wakaf = wakafRepository.findByWakafCode(code);
                if (wakaf != null) {
                    transaction.setWakaf(wakaf);
                    break;
                } else {
                    throw new RuntimeException("wakaf not found with code: " + code);
                }
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }

        transaction.setTransactionDate(new Date());
        transaction.setCategory(transactionType);
        transaction.setSuccess(true);
        transaction = transactionRepository.save(transaction);

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
    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    @Override
    public double getTotalDonationCampaign(){
        return transactionRepository.totalDonationCampaign();
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
    public List<UserTransactionsHistoryResponse> getUserTransactionsHistory(User user) {
        List<Transaction> userTransactions = transactionRepository.findByUser(user);
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

