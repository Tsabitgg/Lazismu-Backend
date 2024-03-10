package com.ict.careus.service;

import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.model.User;
import com.ict.careus.model.ziswaf.Infak;
import com.ict.careus.model.ziswaf.Wakaf;
import com.ict.careus.model.ziswaf.Zakat;
import com.ict.careus.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private PasswordEncoder encoder;

    @Override
    public Transaction createTransaction(String transactionType, String code, Transaction transaction) {
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

        // Update amount berdasarkan type
        if (transactionType.equals("campaign")) {
            transactionRepository.update_campaign_current_amount(code, transaction.getTransactionAmount());
        }else  if (transactionType.equals("zakat")){
            transactionRepository.update_zakat_amount(code, transaction.getTransactionAmount());
        } else if (transactionType.equals("infak")){
            transactionRepository.update_infak_amount(code, transaction.getTransactionAmount());
        } else if (transactionType.equals("wakaf")){
            transactionRepository.update_wakaf_amount(code, transaction.getTransactionAmount());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String password = dateFormat.format(new Date());
        User user = userRepository.findByNoPhone(transaction.getNoPhone());
        if (user == null) {
            user = new User();
            user.setUsername(transaction.getUsername());
            user.setNoPhone(transaction.getNoPhone());

            String encodePassword = encoder.encode(password);
            user.setPassword(encodePassword);
            userRepository.save(user);
        }

        return transaction;
    }

}
