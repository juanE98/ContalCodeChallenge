package net.contal.demo.services;

import net.contal.demo.AccountNumberUtil;
import net.contal.demo.DbUtils;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO complete this service class
 * TODO use BankServiceTest class
 */
@Service
@Transactional
public class BankService {

    //USE this class to access database , you can call openASession to access database
    private final DbUtils dbUtils;
    @Autowired
    public BankService(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }


    /**
     * TODO implement the rest , populate require fields for CustomAccount (Generate Back account by using AccountNumberUtil )
     * Save customAccount to database
     * return AccountNumber
     * @param customerAccount populate this (firstName , lastName ) already provided
     * @return accountNumber
     */
    public String createAnAccount(CustomerAccount customerAccount) {
        int accountNumber = AccountNumberUtil.generateAccountNumber();
        customerAccount.setAccountNumber(accountNumber);
        dbUtils.openASession().saveOrUpdate(customerAccount);
        return String.valueOf(accountNumber);
    }


    /**
     *
     * @param accountNumber target account number
     * @param amount amount to register as transaction
     * @return boolean , if added as transaction
     */
    public boolean addTransactions(int accountNumber , Double amount){

        /**
         *TODO
         * Find and account by using accountNumber (Only write  the query in hql String  )
         * create Transaction for account with provided  amount
         * return true if added , return false if account dont exist , or amount is null
         */

        if (amount == null) {
            return false;
        }

        try {
            String hql = "FROM CustomerAccount WHERE accountNumber = :accountNumber";
            List<CustomerAccount> accounts = dbUtils.openASession()
                    .createQuery(hql, CustomerAccount.class)
                    .setParameter("accountNumber", accountNumber)
                    .getResultList();

            if (accounts.isEmpty()) {
                return false;
            }

            CustomerAccount account = accounts.get(0);
            BankTransaction transaction = new BankTransaction();
            transaction.setCustomerAccount(account);
            transaction.setTransactionAmount(amount);
            transaction.setTransactionDate(new Date());

            dbUtils.openASession().save(transaction);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     *
     * @param accountNumber target account
     * @return account balance
     */
    public double getBalance(int accountNumber){

        /**
         *TODO
         *  find the account by this account Number
         *  sum total of transactions belong to account
         *  return sum of amount
         *
         */
        String hql = "SELECT SUM(transactionAmount) FROM BankTransaction WHERE customerAccount.accountNumber = :accountNumber";
        Double balance = dbUtils.openASession()
                .createQuery(hql,CustomerAccount.class)
                .setParameter("accountNumber",accountNumber)
                .getSingleResult().getAccountBalance();

        return balance != null ? balance : 0d;
    }


    /**
     * TODO implement this functions
     * ADVANCE TASK
     * @param accountNumber accountNumber
     * @return HashMap [key: date , value: double]
     */
    public Map<Date,Double> getDateBalance(int accountNumber){
        /**
         *TODO
         * get all bank Transactions for this account number
         * Create map , Each Entry should hold a Date as a key and value as balance on key date from start of account
         * Example data [01/01/1992 , 2000$] balance 2000$ that date 01/01/1992
         */

        return null;
    }


}
