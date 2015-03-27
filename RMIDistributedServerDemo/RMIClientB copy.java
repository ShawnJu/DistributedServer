import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 * Created by ShangJu on 2/19/15.
 */
public class RMIClientB {
    protected static Logger log;
    public static int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static void main(String[] args) throws Exception {

        log = new Logger("RMIClientLog");

        if (args.length != 3) {
            throw new RuntimeException("Syntax: ClientB serverHostName threadCount iterationCount");
        }

        /*if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }*/

        final RMIServer server = (RMIServer) Naming.lookup("//" + args[0] + "/RMIServer");
        Request request;
        Response response;

        final int[] ids = new int[100];
        Thread[] threads = new Thread[Integer.parseInt(args[1])];
        final int n_iter = Integer.parseInt(args[2]);

        // 1. The main thread will first sequentially create 100 accounts on the server.
        for (int i = 0; i < 100; i++) {
            request = new NewAccountRequest("Shang" + String.valueOf(i), "Ju" + String.valueOf(i), String.valueOf(i) + "Washington Ave");
            response = server.Worker(request);
            ids[i] = ((NewAccountResponse)response).id;
            log.log("NEW ACCOUNT REQUEST " + " firstName=" + "Shang" + String.valueOf(i) + " lastName=" + "Ju" + String.valueOf(i) + " address=" + String.valueOf(i) + "Washington Ave" + " id=" + ids[i] +" - OK");
        }

        // 2. The main thread will then sequentially deposit 100 in each of these accounts.
        for (int i = 0; i < 100; i++) {
            request = new DepositRequest(ids[i], 100);
            response = server.Worker(request);
            log.log("DEPOSIT REQUEST " + " id=" + ids[i] + " amount=100" + " - OK");
        }

        // 3. It will now sequentially execute getBalance on each account, and print sum of the
        // total balance of all accounts. This value should be 10,000.
        int sum = 0;

        for (int i = 0; i < 100; i++) {
            request = new BalanceRequest(ids[i]);
            response = server.Worker(request);
            sum += ((BalanceResponse)response).amount;
            log.log("BALANCE REQUEST " + " id=" + ids[i] + " - OK");
        }

        System.out.println(sum);

        // 4. The main thread will now create the specified number of client threads, and each
        // thread will perform the following sequence of operations for the specified number of iterations
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < n_iter; j++) {
                        try {
                            int idA = randInt(0, 99);
                            int idB = randInt(0, 99);
                            Request request = new TransferRequest(ids[idA], ids[idB], 10);
                            Response response = server.Worker(request);
                            if (((TransferResponse) response).status == Response.Status.FAILED) {
                                log.log("TRANSFER REQUEST " + " from=" + idA + " to=" + idB + " amount=10" + " - FAILED");
                            } else {
                                log.log("TRANSFER REQUEST " + " from=" + idA + " to=" + idB + " amount=10" + " - OK");
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            });
            threads[i].start();
        }

        // 5. The main thread will wait for the completion of all of the client threads created in step 4 above.

        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            threads[i].join();
        }

        // 6. The main thread will now sequentially execute getBalance on each account, and print sum of the total balance of all accounts.
        //This value should be again 10,000.

        sum = 0;

        for (int i = 0; i < 100; i++) {
            request = new BalanceRequest(ids[i]);
            response = server.Worker(request);
            sum += ((BalanceResponse)response).amount;
        }

        System.out.println(sum);
    }
}