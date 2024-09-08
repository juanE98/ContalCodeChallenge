package net.contal.demo.services;

import net.contal.demo.AccountNumberUtil;
import net.contal.demo.DbUtils;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * complete this service class
 * use BankServiceTest class
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
     * implement the rest , populate require fields for CustomAccount (Generate Back account by using AccountNumberUtil )
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
         *
         * Find and account by using accountNumber (Only write  the query in hql String  )
         * create Transaction for account with provided  amount
         * return true if added , return false if account dont exist , or amount is null
         */

        if (amount == null) {
            return false;
        }

        try {
            String hql = "FROM CustomerAccount WHERE accountNumber = :accountNumber";
            List<CustomerAccount> accounts = this.dbUtils.openASession()
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

        /**acc
         *
         *  find the account by this account Number
         *  sum total of transactions belong to account
         *  return sum of amount
         *
         */
        String hql = "SELECT SUM(transactionAmount) FROM BankTransaction WHERE customerAccount.accountNumber = :accountNumber";
        List<CustomerAccount> result = this.dbUtils.openASession()
                .createQuery(hql, CustomerAccount.class)
                .setParameter("accountNumber", accountNumber)
                .getResultList();

        return (result.isEmpty() || result.get(0) == null) ? 0d : result.get(0).getAccountBalance();
    }

    /**
     * get account details of customer by account number
     *
     * @param accountNumber accountNumber
     * @return CustomerAccount
     */
    public CustomerAccount getAccountDetails(int accountNumber) {
        String hql = "FROM CustomerAccount WHERE accountNumber = :accountNumber";
        List<CustomerAccount> accounts = this.dbUtils.openASession()
                .createQuery(hql, CustomerAccount.class)
                .setParameter("accountNumber", accountNumber)
                .getResultList();

        return accounts.isEmpty() ? null : accounts.get(0);
    }


    /**
     * implement this functions
     * ADVANCE TASK
     * @param accountNumber accountNumber
     * @return HashMap [key: date , value: double]
     */
    public Map<Date,Double> getDateBalance(int accountNumber) {
        /**
         *
         * get all bank Transactions for this account number
         * Create map , Each Entry should hold a Date as a key and value as balance on key date from start of account
         * Example data [01/01/1992 , 2000$] balance 2000$ that date 01/01/1992
         */
        try {
            String hql = "FROM BankTransaction WHERE customerAccount.accountNumber = :accountNumber ORDER BY transactionDate";
            List<BankTransaction> transactions = this.dbUtils.openASession()
                    .createQuery(hql, BankTransaction.class)
                    .setParameter("accountNumber", accountNumber)
                    .getResultList();

            Map<Date, Double> dateBalance = new HashMap<>();
            double totalBalance = 0;

            for (BankTransaction transaction : transactions) {
                totalBalance += transaction.getTransactionAmount();
                dateBalance.put(transaction.getTransactionDate(), totalBalance);
            }
            return dateBalance;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
