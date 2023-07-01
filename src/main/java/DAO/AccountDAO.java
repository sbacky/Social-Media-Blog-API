package DAO;

import Model.Account;
import java.util.List;

public interface AccountDAO {

    /**
     * Get list of all registered accounts
     * @return list of registered accounts
     */
    List<Account> getAccounts();

    /**
     * Get registered account by ID
     * @param id
     * @return account at id, null if no account found
     */
    Account getAccountByID(int id);
    
    /**
     * Register a new account
     * 
     * @param account (Username and password only!)
     * @return new account with account_id, if successfully registered, null otherwise
     */
    Account addAccount(Account account);

    // Account updateAccount(Account account);

    // boolean deleteAccount(int id);

}
