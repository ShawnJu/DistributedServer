/**
 * Created by ShangJu on 2/13/15.
 */

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;


public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {
    protected static Accounts accounts = new Accounts();
    protected static Logger log;

        public RMIServerImpl() throws RemoteException {
            super(5106);
        }

        public Response Worker (Request request) throws ServerNotActiveException{
            try {
                Request req = request;
                switch (req.type) {
                    case NEW:
                        String firstName = ((NewAccountRequest)req).firstName;
                        String lastName = ((NewAccountRequest)req).lastName;
                        String address = ((NewAccountRequest)req).address;
                        int id = accounts.CreateAccount(firstName, lastName, address);
                        NewAccountResponse new_response = new NewAccountResponse(Response.Status.OK, id);
                        log.log("["+UnicastRemoteObject.getClientHost()+"]"+"  NEW ACCOUNT REQUEST " + "firstName=" + firstName + " lastName=" + lastName + " address=" + address + " - OK");
                        return new_response;

                    case BALANCE:   int req_id = ((BalanceRequest)req).accountID;
                        BalanceResponse bal_response = new BalanceResponse(Response.Status.OK, accounts.getBalance(req_id));
                        log.log("["+UnicastRemoteObject.getClientHost()+"]"+"  BALANCE REQUEST " + " accountID=" + req_id + " - OK");

                        return bal_response;

                    case DEPOSIT:   int depositAmount = ((DepositRequest)req).amount;
                        int to = ((DepositRequest)req).accountID;
                        accounts.deposit(depositAmount, to);
                        DepositResponse dep_response = new DepositResponse(Response.Status.OK);
                        log.log("["+UnicastRemoteObject.getClientHost()+"]"+"  DEPOSIT REQUEST " + " accountID=" + to + " amount=" + depositAmount + " - OK");

                        return dep_response;

                    case WITHDRAW:  int withdrawAmount = ((WithdrawRequest)req).amount;
                        int from = ((WithdrawRequest)req).accountID;
                        WithdrawResponse wd_response;
                        if (accounts.withdraw(withdrawAmount, from) == 0){
                            wd_response = new WithdrawResponse(Response.Status.OK);
                            log.log("["+UnicastRemoteObject.getClientHost()+"]"+"  WITHDRAW REQUEST " + " accountID=" + from + " amount=" + withdrawAmount + " - OK");

                        }
                        else {
                            wd_response = new WithdrawResponse(Response.Status.FAILED);
                            log.log("["+UnicastRemoteObject.getClientHost()+"]"+"  WITHDRAW REQUEST " + " accountID=" + from + " amount=" + withdrawAmount + " - FAILED");

                        }
                        return wd_response;

                    case TRANSFER:  int transferAmount = ((TransferRequest)req).amount;
                        int transferTo = ((TransferRequest)req).toID;
                        int transferFrom = ((TransferRequest)req).accountID;
                        TransferResponse tr_response;
                        if (accounts.transfer(transferAmount, transferFrom, transferTo) == 0)
                        {
                            tr_response = new TransferResponse(Response.Status.OK);
                            log.log("["+UnicastRemoteObject.getClientHost()+"]"+"  TRANSFER REQUEST " + " from=" + transferFrom +  " to=" + transferTo +" amount=" + transferAmount + " - OK");

                        }
                        else {
                            tr_response = new TransferResponse(Response.Status.FAILED);
                            log.log("["+UnicastRemoteObject.getClientHost()+"]"+"  TRANSFER REQUEST " + " from=" + transferFrom +  " to=" + transferTo +" amount=" + transferAmount + " - FAILED");
                        }
                        return tr_response;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Response(Response.Status.FAILED);
        }

        public static void main (String[] args) throws IOException{
            log = new Logger("RMIServerLog");
            log.log("Be aware, the default port num is 2001.\n" +
                    "To Use another port please select one of the following:\n " +
                    "Microsoft Windows:\n" +
                    "\n" +
                    "start rmiregistry <PORT_NUM>\n" +
                    "\n" +
                    "Solaris OS or Linux:\n" +
                    "\n" +
                    "rmiregistry <PORT_NUM> &\n");
            int portnum = -1;
            if(args[1]!=null){
                portnum = Integer.parseInt(args[1]);
            }
            try {
                /*System.setSecurityManager(new SecurityManager());*/
                Registry localRegistry = LocateRegistry.getRegistry((portnum == -1)?5106:portnum);
                localRegistry.bind("RMIServer", new RMIServerImpl());
            }
            catch (Exception e) {
                log.log("Server Failed to Start");
                log.closelogger();
                e.printStackTrace();
            }
        }


    }
