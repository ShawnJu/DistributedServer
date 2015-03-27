import java.io.Serializable;

/**
 * Created by ShangJu on 2/16/15.
 */
public class Response implements Serializable {
    public enum Status {OK, FAILED}
    public Status status;

    protected Response(Status status) {
        this.status = status;
    }
    protected Response() {}
}
