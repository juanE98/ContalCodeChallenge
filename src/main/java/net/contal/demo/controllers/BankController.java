package net.contal.demo.controllers;

import net.contal.demo.modal.CustomerAccount;
import net.contal.demo.services.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banks")
public class BankController {
    final Logger logger = LoggerFactory.getLogger(BankController.class);
    final BankService dataService;

    public BankController(BankService dataService) {
        this.dataService = dataService;
    }


    /**
     *  call properiate method in dataService to create an bank account , return generated bank account number
     * @param account {firstName:"" , lastName:"" }
     * @return bank account number
     */
    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public long createBankAccount(@RequestBody CustomerAccount account){
        logger.info("Creating account for: {}" ,account.getId());
        return Long.parseLong(dataService.createAnAccount(account));
    }

    /**
     * call properiate method in dataService to return customer account details
     * @param accountNumber BankAccount number
     * @return CustomerAccount customer account details
     */
    @RequestMapping(method = RequestMethod.POST, value = "/account")
    public CustomerAccount getAccountDetails(@RequestParam("accountNumber") String accountNumber){
        logger.info("Fetching details for account number :{}",accountNumber);
        return dataService.getAccountDetails(Integer.parseInt(accountNumber));
    }


    /**
     * call related Method from Service class to do the process
     * @param accountNumber BankAccount number
     * @param amount Amount as Transaction
     */
    @RequestMapping(method = RequestMethod.POST,value = "/transaction")
    public void addTransaction(@RequestParam("accountNumber") String accountNumber, @RequestParam("amount") Double amount){
        logger.info("Bank Account number is :{} , Transaction Amount {}",accountNumber,amount);
        dataService.addTransactions(Integer.parseInt(accountNumber), amount);
    }


    /**
     * call related Method from Service class to do the process
     * @param accountNumber customer  bank account  number
     * @return balance
     */
    @RequestMapping(method = RequestMethod.POST,value = "/balance")
    public Double getBalance(@RequestParam("accountNumber") String accountNumber){
        logger.info("Bank Account number is :{}",accountNumber);
        return dataService.getBalance(Integer.parseInt(accountNumber));
    }
}
