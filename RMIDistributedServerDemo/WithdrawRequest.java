/**
 * Created by Shang Ju on 2/4/15.
 */
public class WithdrawRequest extends Request {

    protected int amount;

    public WithdrawRequest(int accountID, int amount) {
        super(accountID);
        this.amount = amount;
        this.type = Type.WITHDRAW;
    }
}
