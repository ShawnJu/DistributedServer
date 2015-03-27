/**
 * Created by ShangJu on 2/19/15.
 */
public class BalanceResponse extends Response {
    public int amount;

    protected BalanceResponse() {}
    public BalanceResponse(Status status, int amount) {
        this.status = status;
        this.amount = amount;
    }
}
