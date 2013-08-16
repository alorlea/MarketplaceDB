package bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class Account extends UnicastRemoteObject implements IAccount {
	private float balance;
	private String name;
	private String bankname;
	private Connection connection;
	private PreparedStatement updateStatement;

	public Account(String name, String bankname, float balance, Connection connection) 
			throws RemoteException, RejectedException {
		super();
		this.name = name;
		this.bankname = bankname;
		this.balance = balance;
		this.connection = connection;
		try {
			updateStatement = connection.prepareStatement("UPDATE " + bankname +
					" SET balance=? WHERE name='" + name + "'");
		} catch (SQLException sqle){
			throw new RejectedException("Unable to instantiate account", sqle);
		}
	}

	public Account(String name2, String bankname, Connection connection)
			throws RemoteException, RejectedException {
		this(name2, bankname, 10000, connection);
	}

	public synchronized void deposit(float value) throws RemoteException, 
	                                                     RejectedException {
		if (value < 0) {
			throw new RejectedException("Rejected: Account " + name + 
					": Illegal value: " + value);
		}
		
		boolean success = false;
		try {
			balance += value;
			updateStatement.setDouble(1, balance);
			int rows = updateStatement.executeUpdate();
			if (rows != 1){
				throw new RejectedException("Unable to deposit into account: " + name);
			} else {
				success = true;
			}
			System.out.println("Transaction: Account " + name + ": deposit: $" +
					value + ", balance: $" + balance);
		} catch (SQLException sqle) {
			throw new RejectedException("Unable to deposit into account: " + name, sqle);
		} finally {
			if (!success) {
				balance -= value;
			}
		}
	}

	public synchronized void withdraw(float value) throws RemoteException, 
	                                                      RejectedException {
		if (value < 0) {
			throw new RejectedException("Rejected: Account " + name + 
					": Illegal value: " + value);
		}

		if ((balance - value) < 0) {
			throw new RejectedException("Rejected: Account " + name + 
					": Negative balance on withdraw: " +
					(balance - value));
		}

        boolean success = false;
        try {
            balance -= value;
            updateStatement.setDouble(1, balance);
            int rows = updateStatement.executeUpdate();
            if (rows != 1){
                throw new RejectedException("Unable to deposit into account: " + name);
            } else {
            	success = true;
            }
            System.out.println("Transaction: Account " + name + ": deposit: $" +
                    value + ", balance: $" + balance);
        } catch (SQLException sqle) {
            throw new RejectedException("Unable to deposit into account: " + name, sqle);
        } finally {
            if (!success) {
                balance += value;
            }
        }
	}

	public synchronized float getBalance() throws RemoteException {
		return balance;
	}
}
