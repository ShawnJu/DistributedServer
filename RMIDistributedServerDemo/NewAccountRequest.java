/**
 * Created by ShangJu on 2/4/15.
 */
public class NewAccountRequest extends Request {
    protected String firstName;
    protected String lastName;
    protected String address;

    public NewAccountRequest(String firstName, String lastName, String address) {
        this.type = Type.NEW;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
}
