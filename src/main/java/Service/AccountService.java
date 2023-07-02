package Service;

import java.util.Objects;
import java.util.Optional;

import DAO.AccountDAO;
import DAO.AccountDAOImpl;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAOImpl();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Verify account username and password are a valid combination and
     * return verified account with account_id or null if not valid.
     * @param account (Username and password only!)
     * @return account with account_id
     */
    public Account loginAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        // Check if username and password is valid
        Optional<Account> checkAccount = accountDAO.getAccounts().stream()
                                .filter(a -> Objects.equals(a.getUsername(), username) && Objects.equals(a.getPassword(), password))
                                .reduce((a, b) -> {throw new IllegalStateException();});
        // Return account if valid or null otherwise
        if (checkAccount.isPresent()) {
            return checkAccount.get();
        } else {
            return null;
        }
    }

    /**
     * Create account if username and password are valid.<br><br>
     * Username is valid if field is not blank and username has not
     * been used by another account.
     * Password is valid if it is 4 characters or longer.
     * @param account (Username and passowrd only!)
     * @return account with account_id
     */
    public Account createAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        // Check if username is blank
        if (username.isBlank()) {
            return null;
        }
        // Check if password is atleast 4 characters long
        if (password.length() < 4) {
            return null;
        }
        // Check if username has not already been used by another accoount
        Optional<Account> checkAccount = accountDAO.getAccounts().stream()
                                .filter(a -> Objects.equals(a.getUsername(), username))
                                .reduce((a, b) -> {throw new IllegalStateException();});
        // Create and return account with account_id if username and password are valid, null otherwise
        if (checkAccount.isEmpty()) {
            return accountDAO.addAccount(account);
        } else {
            return null;
        }
    }
    
}
