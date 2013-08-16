package bank;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBank extends Remote {
    public IAccount newAccount(String name) throws RemoteException, RejectedException;
    public IAccount getAccount(String name) throws RemoteException, RejectedException;
    public boolean deleteAccount(String name) throws RemoteException, RejectedException;
    public String[] listAccounts() throws RemoteException;
}
