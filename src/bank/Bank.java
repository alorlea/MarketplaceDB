package bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
public class Bank
   extends UnicastRemoteObject
   implements IBank
{
   private String bankname;
   private Map<String, IAccount> accounts = new HashMap<String, IAccount>();
   private Statement sqlStatement;
   private String datasource = "mpDB";
   private String dbms = "derby";


   public Bank(String bankName, String datasource, String dbms)
      throws RemoteException
   {
      super();
      this.bankname = bankName;
      this.datasource = datasource;
      this.dbms = dbms;
      createDatasource();
   }


   private void createDatasource()
   {
      try
      {
         Connection connection = getConnection();
         connection.setAutoCommit(true);
         sqlStatement = connection.createStatement();
         boolean exist = false;
         int tableNameColumn = 3;
         DatabaseMetaData dbm = connection.getMetaData();
         for (ResultSet rs = dbm.getTables(null, null, null, null); rs.next();)
         {
            String tableName = rs.getString(tableNameColumn);
            if (tableName.equalsIgnoreCase(bankname))
            {
               exist = true;
               rs.close();
               break;
            }
         }
         if (!exist)
         {
            sqlStatement.executeUpdate("CREATE TABLE " + bankname
                                       + " (name VARCHAR(32) PRIMARY KEY, balance FLOAT)");
         }
      }
      catch (Exception e)
      {
         System.err.println("Error retrieving the database.");
         System.exit(1);
      }
   }


   private Connection getConnection()
      throws ClassNotFoundException, SQLException
   {
      if (dbms.equalsIgnoreCase("access"))
      {
         Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
         return DriverManager.getConnection("jdbc:odbc:" + datasource);
      }
      else if (dbms.equalsIgnoreCase("cloudscape"))
      {
         Class.forName("COM.cloudscape.core.RmiJdbcDriver");
         return DriverManager.getConnection(
            "jdbc:cloudscape:rmi://localhost:1099/" + datasource
            + ";create=true;");
      }
      else if (dbms.equalsIgnoreCase("pointbase"))
      {
         Class.forName("com.pointbase.jdbc.jdbcUniversalDriver");
         return DriverManager.getConnection(
            "jdbc:pointbase:server://localhost:9092/" + datasource
            + ",new", "PBPUBLIC", "PBPUBLIC");
      }
      else if (dbms.equalsIgnoreCase("derby"))
      {
         Class.forName("org.apache.derby.jdbc.ClientXADataSource");
         return DriverManager.getConnection(
            "jdbc:derby://localhost:1527/" + datasource + ";create=true");
      }
      else if (dbms.equalsIgnoreCase("mysql"))
      {
         Class.forName("com.mysql.jdbc.Driver");
         return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/" + datasource, "root", "javajava");
      }
      else
      {
         return null;
      }
   }


   @Override
   public synchronized String[] listAccounts()
   {
      return accounts.keySet().toArray(new String[1]);
   }


   @Override
   public synchronized IAccount newAccount(String name)
      throws RemoteException,
             RejectedException
   {
      Account account = (Account) accounts.get(name);
      if (account != null)
      {
         System.out.println("Account [" + name + "] exists!!!");
         throw new RejectedException("Rejected: Bank: " + bankname
                                     + " Account for: " + name + " already exists: " + account);
      }
      ResultSet result = null;
      try
      {
         result = sqlStatement.executeQuery(
            "SELECT * from " + bankname + " WHERE NAME='" + name + "'");

         if (result.next())
         {
            // account exists, instantiate, put in cache and throw exception.
            account = new Account(name, bankname, result.getFloat("balance"),
                                  getConnection());
            accounts.put(name, account);
            throw new RejectedException("Rejected: Account for: " + name
                                        + " already exists");
         }
         result.close();

         // create account.
         int rows = sqlStatement.executeUpdate("INSERT INTO " + bankname
                                               + " VALUES ('" + name + "', 10000)");
         if (rows == 1)
         {
            account = new Account(name, bankname, getConnection());
            accounts.put(name, account);
            System.out.println("Bank: " + bankname + " Account: " + account
                               + " has been created for " + name);
            return account;
         }
         else
         {
            throw new RejectedException("Cannot create an account for " + name);
         }
      }
      catch (Exception e)
      {
         throw new RejectedException("Cannot create an account for " + name, e);
      }
   }


   @Override
   public synchronized IAccount getAccount(String name)
      throws RemoteException,
             RejectedException
   {
      if (name == null)
      {
         return null;
      }

      IAccount acct = accounts.get(name);
      if (acct == null)
      {
         try
         {
            ResultSet result = sqlStatement.executeQuery("SELECT * FROM "
                                                         + bankname + " WHERE name ='" + name
                                                         + "'");
            if (result.next())
            {
               acct = new Account(result.getString("name"), bankname,
                                  result.getFloat("balance"), getConnection());
               result.close();
               accounts.put(name, acct);
            }
            else
            {
               return null;
            }
         }
         catch (Exception e)
         {
            throw new RejectedException("Unable to find account for " + name, e);
         }
      }
      return acct;
   }


   @Override
   public synchronized boolean deleteAccount(String name)
      throws RejectedException
   {
      if (!hasAccount(name))
      {
         return false;
      }
      accounts.remove(name);
      try
      {
         int rows = sqlStatement.executeUpdate("DELETE FROM " + bankname
                                               + " WHERE name='" + name + "'");
         if (rows != 1)
         {
            throw new RejectedException("Unable to delete account..." + name);
         }
      }
      catch (SQLException e)
      {
         System.out.println("Unable to delete account for " + name + ": "
                            + e.getMessage());
         throw new RejectedException("Unable to delete account..." + name, e);
      }
      System.out.println("Bank: " + bankname
                         + " Account for " + name + " has been deleted");
      return true;
   }


   private boolean hasAccount(String name)
   {
      if (accounts.get(name) == null)
      {
         return false;
      }
      else
      {
         return true;
      }
   }
}
