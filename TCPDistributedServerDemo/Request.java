import java.io.Serializable;

/**
 * Created by ShangJu on 2/4/15.
 */
public class Request implements Serializable {
    protected int accountID;
    public enum Type {NEW, BALANCE, DEPOSIT, WITHDRAW, TRANSFER}
    public Type type;
    protected Request(){}
    protected Request(int accountID) {
        this.accountID = accountID;
    }
}
