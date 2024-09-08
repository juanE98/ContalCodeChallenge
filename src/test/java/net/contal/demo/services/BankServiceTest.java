
package net.contal.demo.services;

import net.contal.demo.AccountNumberUtil;
import net.contal.demo.DbUtils;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

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

    @Test
    public void testGetAccountDetails() {
        CustomerAccount mockAccount = new CustomerAccount();
        mockAccount.setAccountNumber(12345);

        org.hibernate.query.Query<CustomerAccount> query = mock(org.hibernate.query.Query.class);
        when(session.createQuery(anyString(), eq(CustomerAccount.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(mockAccount));

        CustomerAccount result = bankService.getAccountDetails(12345);

        assertNotNull(result);
        assertEquals(12345, result.getAccountNumber());
    }

    @Test
    public void testAddTransactions_ValidAccount() {
        CustomerAccount mockAccount = new CustomerAccount();
        mockAccount.setAccountNumber(12345);

        org.hibernate.query.Query<CustomerAccount> query = mock(org.hibernate.query.Query.class);
        when(session.createQuery(anyString(), eq(CustomerAccount.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(mockAccount));

        boolean result = bankService.addTransactions(12345, 100.0);

        assertTrue(result);
        verify(session, times(1)).createQuery(anyString(), eq(CustomerAccount.class));
        verify(query, times(1)).setParameter("accountNumber", 12345);
        verify(query, times(1)).getResultList();
    }

    @Test
    public void testAddTransactions_InvalidAccount() {
        org.hibernate.query.Query<CustomerAccount> query = mock(org.hibernate.query.Query.class);
        when(session.createQuery(anyString(), eq(CustomerAccount.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        boolean result = bankService.addTransactions(12345, 100.0);

        assertFalse(result);
        verify(session, times(1)).createQuery(anyString(), eq(CustomerAccount.class));
        verify(query, times(1)).setParameter("accountNumber", 12345);
        verify(query, times(1)).getResultList();
    }

    @Test
    public void testAddTransactions_NullAmount() {
        boolean result = bankService.addTransactions(12345, null);

        assertFalse(result);
        verify(session, never()).createQuery(anyString(), eq(CustomerAccount.class));
    }

}