/**
 * Created by Shang Ju on 2/4/15.
 */
public class TransferRequest extends Request{
    protected int amount;
    protected int toID;

    public TransferRequest(int fromID, int toID, int amount) {
        super(fromID);
        this.toID = toID;
        this.amount = amount;
        this.type = Type.TRANSFER;
    }
}
