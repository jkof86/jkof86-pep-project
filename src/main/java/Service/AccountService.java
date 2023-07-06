package Service;

import java.util.List;

import DAO.AccountDao;
import Model.Account;

public class AccountService {

    public AccountDao accDao;

    public AccountService() {
        this.accDao = new AccountDao();
    }

    public List<Account> getAllAccounts(){
        return this.accDao.getAllAccounts();
    }
    
    public Account register(Account a){
        return this.accDao.register(a);
    }
    
    public Account login(Account a){
        return this.accDao.login(a);
    }
    
}
