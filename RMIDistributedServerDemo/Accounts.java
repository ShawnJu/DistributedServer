import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ShangJu on 2/4/15.
 */
public class Accounts {
    protected static AtomicInteger AccountCounter = new AtomicInteger();
    private ConcurrentHashMap<Integer, Account> Database = new ConcurrentHashMap<Integer, Account>();
    private Logger log = Logger.getAnonymousLogger();

    public Accounts(){
    }

    public int CreateAccount (String firstName, String lastName, String address){
        int AccountId = AccountCounter.incrementAndGet();
        Database.put(AccountId, new Account(firstName, lastName, address, AccountId));
        return AccountId;
    }

    public void deposit(int amount, int AccountId) {
        if (Database.get(AccountId) != null){
            Database.get(AccountId).deposit(amount);
        }
        else {
            log.log(Level.INFO, "No Such Account exists");
        }
    }

    public int withdraw(int amount, int AccountId) {
        if (Database.get(AccountId) == null){
            log.log(Level.INFO, "No Such Account exists");
            return -1;
        }
        return Database.get(AccountId).withdraw(amount);

    }

    public int getBalance(int AccountId) {
        if (Database.get(AccountId) == null) {
            log.log(Level.INFO, "No Such Account exists");
            return -1;
        }
        return Database.get(AccountId).getBalance();
    }

    public int transfer(int amount, int from, int to) {
        if (Database.get(from) == null && Database.get(to) == null) {
            log.log(Level.INFO, "No Such Account exists");
            return -1;
        }
        int val = Database.get(from).withdraw(amount);
        if (val > 0) {
            Database.get(to).deposit(val);
            return 0;
        }
        return -1;

    }
}
