import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;


/**
 * Created by ShangJu on 2/18/15.
 */
public class ClientB {
    static Socket clientB;


//    1. The main thread will first sequentially create 100 accounts on the server.
//    2. The main thread will then sequentially deposit 100 in each of these accounts.
//    3. It will now sequentially execute getBalance on each account, and print sum of the
//    total balance of all accounts. This value should be 10,000.
//    4. The main thread will now create the specified number of client threads, and each
//    thread will perform the following sequence of operations for the specified number of iterations::
//    a. Randomly pick two accounts and transfer 10 from one to another. In case the operation fails due to insufficient balance,
//    the client thread will write to the clientLogfile the response status and the IDs of the two accounts.
//    b. After performing the specified number of iterations of step (a) above, the client thread will terminate.
//    5. The main thread will wait for the completion of all of the client threads created in step 4 above.
//    6. The main thread will now sequentially execute getBalance on each account, and print sum of the total balance of all accounts.
//    This value should be again 10,000.

    public static int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
        final Logger logger = new Logger("ClientBLog");
        if (args.length != 4) {
            logger.log("Fail to start");
            logger.closelogger();
            throw new IllegalArgumentException("Syntax: ClientB serverHostName serverPortNumber threadCount iterationCount");
        }
        final int port = Integer.parseInt(args[1]);
        final String host = args[0];

        clientB = new Socket(args[0], Integer.parseInt(args[1]));
        final ObjectOutputStream outToServer = new ObjectOutputStream(clientB.getOutputStream());
        final ObjectInputStream inFromServer = new ObjectInputStream(clientB.getInputStream());
        Request request;
        Response response;
        final int[] ids = new int[100];
        Thread[] threads = new Thread[Integer.parseInt(args[2])];
        final int n_iter = Integer.parseInt(args[3]);

        // 1. The main thread will first sequentially create 100 accounts on the server.
        for (int i = 0; i < 100; i++) {
            request = new NewAccountRequest("Shang" + String.valueOf(i), "Ju" + String.valueOf(i), String.valueOf(i) + "Washington Ave");
            outToServer.writeObject(request);
            response = (NewAccountResponse)inFromServer.readObject();
            ids[i] = ((NewAccountResponse)response).id;
            logger.log("NEW ACCOUNT REQUEST " + " firstName=" + "Shang" + String.valueOf(i) + " lastName=" + "Ju" + String.valueOf(i) + " address=" + String.valueOf(i) + "Washington Ave" + " id=" + ids[i] +" - OK");
        }

        // 2. The main thread will then sequentially deposit 100 in each of these accounts.
        for (int i = 0; i < 100; i++) {
            request = new DepositRequest(ids[i], 100);
            outToServer.writeObject(request);
            response = (DepositResponse)inFromServer.readObject();
            logger.log("DEPOSIT REQUEST " + " id=" + ids[i] + " amount=100" + " - OK");
        }

        // 3. It will now sequentially execute getBalance on each account, and print sum of the
        // total balance of all accounts. This value should be 10,000.
        int sum = 0;

        for (int i = 0; i < 100; i++) {
            request = new BalanceRequest(ids[i]);
            outToServer.writeObject(request);
            response = (BalanceResponse)inFromServer.readObject();
            sum += ((BalanceResponse)response).amount;
            logger.log("BALANCE REQUEST " + " id=" + ids[i] + " - OK");
        }

        System.out.println(sum);

        // 4. The main thread will now create the specified number of client threads, and each
        // thread will perform the following sequence of operations for the specified number of iterations
        for (int i = 0; i < Integer.parseInt(args[2]); i++) {
            /*threads[i] = new Thread(new ClientBWorker(n_iter, ids, outToServer, inFromServer));*/
            threads[i] = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                    try {
                        Socket client = new Socket(host, port);
                        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                        for (int j = 0; j < n_iter; j++) {



                                int idA = randInt(0, 99);
                                int idB = randInt(0, 99);
                                Request req = new TransferRequest(ids[idA], ids[idB], 10);
                                Response res;

                                out.writeObject(req);
                                res = (TransferResponse) in.readObject();


                                if (((TransferResponse) res).status == Response.Status.FAILED) {
                                    logger.log("TRANSFER REQUEST " + " from=" + idA + " to=" + idB + " amount=10" + " - FAILED");
                                } else {
                                    logger.log("TRANSFER REQUEST " + " from=" + idA + " to=" + idB + " amount=10" + " - OK");
                                }




                        }
                        out.writeObject(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }

        // 5. The main thread will wait for the completion of all of the client threads created in step 4 above.
        for (int i = 0; i < Integer.parseInt(args[2]); i++) {
            threads[i].join();
        }

        // 6. The main thread will now sequentially execute getBalance on each account, and print sum of the total balance of all accounts.
        //This value should be again 10,000.

        sum = 0;

        for (int i = 0; i < 100; i++) {
            request = new BalanceRequest(ids[i]);
            outToServer.writeObject(request);
            response = (BalanceResponse)inFromServer.readObject();
            sum += ((BalanceResponse)response).amount;
        }

        System.out.println(sum);
        outToServer.writeObject(null);
        outToServer.flush();
        outToServer.close();
        inFromServer.close();


    }
}
