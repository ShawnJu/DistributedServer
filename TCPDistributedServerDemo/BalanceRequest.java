/**
 * Created by ShangJu on 2/19/15.
 */
public class BalanceRequest extends Request {
    protected BalanceRequest() {}
    public BalanceRequest(int accountID) {
        super(accountID);
        this.type = Type.BALANCE;
    }
}
