import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Shang Ju on 2/4/15.
 */
public class Account {
    protected int accountID;
    protected AtomicInteger balance = new AtomicInteger();
    protected String firstName;
    protected String lastName;
    protected String address;

    private Account() {
    }

    public Account(String firstName, String lastName, String address, int accountID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.accountID = accountID;
    }

    public void deposit(int amount) {
        this.balance.addAndGet(amount);
    }

    public int withdraw(int amount) {
        if (this.balance.get() - amount < 0) {
            return -1;
        }
        this.balance.addAndGet(0 - amount);
        return amount;
    }

    public int getBalance() {
        return this.balance.get();
    }
}

