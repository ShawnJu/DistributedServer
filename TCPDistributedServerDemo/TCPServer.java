/**
 * Created by ShangJu on 2/9/15.
 */

import java.net.*;
import java.io.*;

public class TCPServer extends Thread{
    protected Socket clientSocket;
    protected static Accounts accounts = new Accounts();
    protected Logger logger;


    public TCPServer(Socket s, Logger log) throws IOException{
        this.logger = log;
        this.clientSocket = s;
    };

    public void run() {
        try {
            logger.log("Accept connection from"+ clientSocket.getInetAddress());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            Request req;
            while ((req = (Request)(inputStream.readObject())) != null)

                switch (req.type) {
                    case NEW:
                        String firstName = ((NewAccountRequest) req).firstName;
                        String lastName = ((NewAccountRequest) req).lastName;
                        String address = ((NewAccountRequest) req).address;
                        int id = accounts.CreateAccount(firstName, lastName, address);
                        NewAccountResponse new_response = new NewAccountResponse(Response.Status.OK, id);
                        outputStream.writeObject(new_response);
                        logger.log("[" + clientSocket.getInetAddress() + "]" + "  NEW ACCOUNT REQUEST " + "firstName=" + firstName + " lastName=" + lastName + " address=" + address + " - OK");
                        break;

                    case BALANCE:
                        int req_id = ((BalanceRequest) req).accountID;
                        BalanceResponse bal_response = new BalanceResponse(Response.Status.OK, accounts.getBalance(req_id));
                        outputStream.writeObject(bal_response);
                        logger.log("[" + clientSocket.getInetAddress() + "]" + "  BALANCE REQUEST " + " accountID=" + req_id + " - OK");
                        break;


                    case DEPOSIT:
                        int depositAmount = ((DepositRequest) req).amount;
                        int to = ((DepositRequest) req).accountID;
                        accounts.deposit(depositAmount, to);
                        DepositResponse dep_response = new DepositResponse(Response.Status.OK);
                        outputStream.writeObject(dep_response);
                        logger.log("[" + clientSocket.getInetAddress() + "]" + "  DEPOSIT REQUEST " + " accountID=" + to + " amount=" + depositAmount + " - OK");
                        break;

                    case WITHDRAW:
                        int withdrawAmount = ((WithdrawRequest) req).amount;
                        int from = ((WithdrawRequest) req).accountID;
                        WithdrawResponse wd_response;
                        if (accounts.withdraw(withdrawAmount, from) == 0) {
                            wd_response = new WithdrawResponse(Response.Status.OK);
                            logger.log("[" + clientSocket.getInetAddress() + "]" + "  WITHDRAW REQUEST " + " accountID=" + from + " amount=" + withdrawAmount + " - OK");
                        } else {
                            wd_response = new WithdrawResponse(Response.Status.FAILED);
                            logger.log("[" + clientSocket.getInetAddress() + "]" + "  WITHDRAW REQUEST " + " accountID=" + from + " amount=" + withdrawAmount + " - FAILED");
                        }
                        outputStream.writeObject(wd_response);

                        break;

                    case TRANSFER:
                        int transferAmount = ((TransferRequest) req).amount;
                        int transferTo = ((TransferRequest) req).toID;
                        int transferFrom = ((TransferRequest) req).accountID;
                        TransferResponse tr_response;
                        if (accounts.transfer(transferAmount, transferFrom, transferTo) == 0) {
                            tr_response = new TransferResponse(Response.Status.OK);
                            logger.log("[" + clientSocket.getInetAddress() + "]" + "  TRANSFER REQUEST " + " from=" + transferFrom +  " to=" + transferTo +" amount=" + transferAmount + " - OK");
                        } else {
                            tr_response = new TransferResponse(Response.Status.FAILED);
                            logger.log("[" + clientSocket.getInetAddress() + "]" + "  TRANSFER REQUEST " + " from=" + transferFrom +  " to=" + transferTo +" amount=" + transferAmount + " - FAILED");
                        }
                        outputStream.writeObject(tr_response);

                        break;


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }



    public static void main (String[] args) throws IOException, InterruptedException{
        if (args.length != 1) {
            System.out.println("Usage: TCPServer <port_number>");
            //logger.log("Fail to start ");
            //logger.closelogger();
            System.exit(0);
        }
        ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
        Logger logger = new Logger("serverLogfile");
        logger.log("Starting listening on port " + args[0]);
        while (true) {
            logger.log("Waiting for client...");
            Socket client = server.accept();
            TCPServer s = new TCPServer(client, logger);
            s.start();
        }

    }

}