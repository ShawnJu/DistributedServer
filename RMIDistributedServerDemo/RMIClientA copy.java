/**
 * Created by ShangJu on 2/13/15.
 */

import java.rmi.Naming;

public class RMIClientA {
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
        public static void main(String args[]) throws Exception {

            if (args.length != 1) {
                throw new RuntimeException("Syntax: RMIClientA serverHostName");
            }

            /*if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }*/

            RMIServer server = (RMIServer) Naming.lookup("//" + args[0] + "/RMIServer");
            Request req;
            Response response;
            int idA = -1, idB = -1;
            int balanceA = -1, balanceB = -1;

            // 1. Create two accounts on the server
            req = new NewAccountRequest("Shang","Ju","University Ave");
            response = server.Worker(req);
            if (response.status == Response.Status.OK)
                idA = ((NewAccountResponse)response).id;

            req = new NewAccountRequest("Aaron","Jiang","Carl St");
            response = server.Worker(req);
            if (response.status == Response.Status.OK)
                idB = ((NewAccountResponse)response).id;

            // 2. Deposit 100 in each of these two accounts
            req = new DepositRequest(idA, 100);
            response = server.Worker(req);

            req = new DepositRequest(idB, 100);
            response = server.Worker(req);

            // 3. Invoke getBalance on each of these accounts and print the returned response
            req = new BalanceRequest(idA);
            response = server.Worker(req);
            if (response.status == Response.Status.OK)
                balanceA = ((BalanceResponse)response).amount;
            System.out.println(balanceA);

            req = new BalanceRequest(idB);
            response = server.Worker(req);
            if (response.status == Response.Status.OK)
                balanceB = ((BalanceResponse)response).amount;
            System.out.println(balanceB);

            // 4. Transfer 100 from one account to the other
            req = new TransferRequest(idA, idB, 100);
            response = server.Worker(req);

            // 5. Repeat step 3 above, and print the returned status and values
            req = new BalanceRequest(idA);
            response = server.Worker(req);
            if (response.status == Response.Status.OK)
                balanceA = ((BalanceResponse)response).amount;
            System.out.println(balanceA);

            req = new BalanceRequest(idB);
            response = server.Worker(req);
            if (response.status == Response.Status.OK)
                balanceB = ((BalanceResponse)response).amount;
            System.out.println(balanceB);

            // 6. Withdraw 100 from each of these two accounts – one should fail and others should
            // return OK.
            req = new WithdrawRequest(idA, 100);
            response = server.Worker(req);

            req = new WithdrawRequest(idB, 100);
            response = server.Worker(req);

            // 7. Repeat step 4 above (transfer operation); it should return fail status
            req = new TransferRequest(idA, idB, 100);
            response = server.Worker(req);

            // 8. Repeat step 3 above, and print the returned status and values
            // client.
            req = new BalanceRequest(idA);
            response = server.Worker(req);
            if (response.status == Response.Status.OK)
                balanceA = ((BalanceResponse)response).amount;
            System.out.println(balanceA);

            req = new BalanceRequest(idB);
            response = server.Worker(req);
            if (response.status == Response.Status.OK)
                balanceB = ((BalanceResponse)response).amount;
            System.out.println(balanceB);


        }

}
