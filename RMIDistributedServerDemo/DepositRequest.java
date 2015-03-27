/**
 * Created by ShangJu on 2/4/15.
 */
public class DepositRequest extends Request {

    protected int amount;

    public DepositRequest(int accountID, int amount) {
        super(accountID);
        this.type = Type.DEPOSIT;
        this.amount = amount;
    }
}
