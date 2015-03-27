/**
 * Created by ShangJu on 2/16/15.
 */
public class NewAccountResponse extends Response{
    public int id;
    public NewAccountResponse(Status status, int id) {
        this.status = status;
        this.id = id;
    }

    protected NewAccountResponse() {};
}
