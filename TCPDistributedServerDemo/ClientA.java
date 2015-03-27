
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by ShangJu on 2/18/15.
 */
public class ClientA{


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 2) {
            throw new RuntimeException("Syntax: ClientA serverHostName serverPortNumber");
        }

        Socket clientA = new Socket(args[0], Integer.parseInt(args[1]));

//        1. Create two accounts on the server
//        2. Deposit 100 in each of these two accounts
//        3. Invoke getBalance on each of these accounts and print the returned response
//        4. Transfer 100 from one account to the other
//        5. Repeat step 3 above, and print the returned status and values
//        6. Withdraw 100 from each of these two accounts – one should fail and others should
//        return OK.
//        7. Repeat step 4 above (transfer operation); it should return fail status
//        8. Repeat step 3 above, and print the returned status and values
//        client.

        ObjectOutputStream outToServer = new ObjectOutputStream(clientA.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientA.getInputStream());
        Response response;
        Request req;
        int idA = -1, idB = -1;

        // 1. Create two accounts on the server
        req = new NewAccountRequest("Shang","Ju","University Ave");

        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();
        if (response.status == Response.Status.OK)
            idA = ((NewAccountResponse)response).id;

        req = new NewAccountRequest("Aaron","Jiang","Carl St");
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();
        if (response.status == Response.Status.OK)
            idB = ((NewAccountResponse)response).id;

        // 2. Deposit 100 in each of these two accounts
        int balanceA = -1, balanceB = -1;
        req = new DepositRequest(idA, 100);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();

        req = new DepositRequest(idB, 100);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();

        // 3. Invoke getBalance on each of these accounts and print the returned response
        req = new BalanceRequest(idA);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();
        if (response.status == Response.Status.OK)
            balanceA = ((BalanceResponse)response).amount;
        System.out.println(balanceA);

        req = new BalanceRequest(idB);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();
        if (response.status == Response.Status.OK)
            balanceB = ((BalanceResponse)response).amount;
        System.out.println(balanceB);

        // 4. Transfer 100 from one account to the other
        req = new TransferRequest(idA, idB, 100);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();

        // 5. Repeat step 3 above, and print the returned status and values
        req = new BalanceRequest(idA);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();
        if (response.status == Response.Status.OK)
            balanceA = ((BalanceResponse)response).amount;
        System.out.println(balanceA);

        req = new BalanceRequest(idB);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();
        if (response.status == Response.Status.OK)
            balanceB = ((BalanceResponse)response).amount;
        System.out.println(balanceB);

        // 6. Withdraw 100 from each of these two accounts – one should fail and others should
//        return OK.
        req = new WithdrawRequest(idA, 100);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();

        req = new WithdrawRequest(idB, 100);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();

        // 7. Repeat step 4 above (transfer operation); it should return fail status
        req = new TransferRequest(idA, idB, 100);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();

        // 8. Repeat step 3 above, and print the returned status and values
//        client.
        req = new BalanceRequest(idA);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();
        if (response.status == Response.Status.OK)
            balanceA = ((BalanceResponse)response).amount;
        System.out.println(balanceA);

        req = new BalanceRequest(idB);
        outToServer.writeObject(req);
        response = (Response)inFromServer.readObject();
        if (response.status == Response.Status.OK)
            balanceB = ((BalanceResponse)response).amount;
        System.out.println(balanceB);

        outToServer.writeObject(null);



    }


}
