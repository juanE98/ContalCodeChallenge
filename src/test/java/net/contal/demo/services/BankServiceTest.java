
package net.contal.demo.services;

import net.contal.demo.AccountNumberUtil;
import net.contal.demo.DbUtils;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BankServiceTest {

    private DbUtils dbUtils;
    private BankService bankService;
    private Session session;
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        dbUtils = mock(DbUtils.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);
        when(dbUtils.openASession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(transaction);
        bankService = new BankService(dbUtils);
    }

    @Test
    public void testCreateAnAccount() {
        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setFirstName("John");
        customerAccount.setLastName("Doe");

        String accountNumber = bankService.createAnAccount(customerAccount);

        verify(session, times(1)).persist(any(CustomerAccount.class));
        verify(transaction, times(1)).commit();
        assertNotNull(accountNumber);
    }

//    @Test
//    public void testAddTransactions() {
//        CustomerAccount customerAccount = new CustomerAccount();
//        customerAccount.setAccountNumber(12345);
//
//        when(session.createQuery(anyString(), eq(CustomerAccount.class)))
//                .thenReturn(mock(org.hibernate.query.Query.class));
//        when(session.createQuery(anyString(), eq(CustomerAccount.class)).setParameter(anyString(), anyInt()))
//                .thenReturn(mock(org.hibernate.query.Query.class));
//        when(session.createQuery(anyString(), eq(CustomerAccount.class)).setParameter(anyString(), anyInt()).getResultList())
//                .thenReturn(Arrays.asList(customerAccount));
//
//        boolean result = bankService.addTransactions(12345, 100.0);
//
//        verify(session, times(1)).persist(any(BankTransaction.class));
//        verify(transaction, times(1)).commit();
//        assertTrue(result);
//    }
//
//    @Test
//    public void testGetBalance() {
//        when(session.createQuery(anyString(), eq(Double.class)))
//                .thenReturn(mock(org.hibernate.query.Query.class));
//        when(session.createQuery(anyString(), eq(Double.class)).setParameter(anyString(), anyInt()))
//                .thenReturn(mock(org.hibernate.query.Query.class));
//        when(session.createQuery(anyString(), eq(Double.class)).setParameter(anyString(), anyInt()).getResultList())
//                .thenReturn(Arrays.asList(100.0));
//
//        double balance = bankService.getBalance(12345);
//
//        assertEquals(100.0, balance);
//    }
//
//    @Test
//    public void testGetAccountDetails() {
//        CustomerAccount customerAccount = new CustomerAccount();
//        customerAccount.setAccountNumber(AccountNumberUtil.generateAccountNumber());
//
//        when(session.createQuery(anyString(), eq(CustomerAccount.class)))
//                .thenReturn(mock(org.hibernate.query.Query.class));
//        when(session.createQuery(anyString(), eq(CustomerAccount.class)).setParameter(anyString(), anyInt()))
//                .thenReturn(mock(org.hibernate.query.Query.class));
//        when(session.createQuery(anyString(), eq(CustomerAccount.class)).setParameter(anyString(), anyInt()).getResultList())
//                .thenReturn(Arrays.asList(customerAccount));
//
//        CustomerAccount result = bankService.getAccountDetails(12345);
//
//        assertNotNull(result);
//        assertEquals(12345, result.getAccountNumber());
//    }
//
//    @Test
//    public void testGetDateBalance() {
//        CustomerAccount customerAccount = new CustomerAccount();
//        customerAccount.setAccountNumber(12345);
//
//        BankTransaction transaction1 = new BankTransaction();
//        transaction1.setTransactionDate(new Date());
//        transaction1.setTransactionAmount(100.0);
//        transaction1.setCustomerAccount(customerAccount);
//
//        BankTransaction transaction2 = new BankTransaction();
//        transaction2.setTransactionDate(new Date());
//        transaction2.setTransactionAmount(200.0);
//        transaction2.setCustomerAccount(customerAccount);
//
//        when(session.createQuery(anyString(), eq(BankTransaction.class)))
//                .thenReturn(mock(org.hibernate.query.Query.class));
//        when(session.createQuery(anyString(), eq(BankTransaction.class)).setParameter(anyString(), anyInt()))
//                .thenReturn(mock(org.hibernate.query.Query.class));
//        when(session.createQuery(anyString(), eq(BankTransaction.class)).setParameter(anyString(), anyInt()).getResultList())
//                .thenReturn(Arrays.asList(transaction1, transaction2));
//
//        Map<Date, Double> dateBalance = bankService.getDateBalance(12345);
//
//        assertNotNull(dateBalance);
//        assertEquals(2, dateBalance.size());
//    }
}