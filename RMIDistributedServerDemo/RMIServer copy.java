/**
 * Created by ShangJu on 2/13/15.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface RMIServer extends Remote
{
    Response Worker(Request request) throws RemoteException, ServerNotActiveException;
}