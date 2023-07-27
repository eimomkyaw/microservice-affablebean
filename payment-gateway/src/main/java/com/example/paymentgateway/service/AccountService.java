package com.example.paymentgateway.service;

import com.example.paymentgateway.dao.AccountDao;
import com.example.paymentgateway.entity.Account;
import com.example.paymentgateway.exception.InsuffientAmount;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/*

 */
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountDao accountDao;
    public double getBalance(String email){
        return accountDao.findByEmail(email).orElseThrow( EntityNotFoundException :: new).getAmount();
    }
    @Transactional
    public double deposit(String email,double amount){
        double balance=getBalance(email);
        double updateAmount=(balance+amount);
        Optional<Account> accountOptional=accountDao.findByEmail(email);
        if (accountOptional.isPresent()){
            Account account=accountOptional.get();
            account.setAmount(updateAmount);
            accountDao.save(account);
            return updateAmount;
        }
        throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    @Transactional
    public double withdraw(String email,double amount){
        double balance=getBalance(email);
        if (balance < amount) {

            throw new InsuffientAmount();
        }
        double updatedAmount=(balance-amount);
        Optional<Account>accountOptional=accountDao.findByEmail(email);
        if (accountOptional.isPresent()){
            Account account=accountOptional.get();
            account.setAmount(updatedAmount);
            accountDao.save(account);
            return updatedAmount;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void transfer(String fromEmail,String toEmail,double amount){
        withdraw(fromEmail,amount);
        deposit(toEmail,amount);

    }
}
