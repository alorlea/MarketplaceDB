/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import bank.IBank;
import bank.RejectedException;
import client.IMarketCommunicator;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 *
 * @author Fernando Garcia Sanjuan, <fgs@kth.se>, <fdosanjuan@gmail.com>
 */
public class Marketplace
   extends UnicastRemoteObject
   implements IMarketplace
{
   static final String NAME = "rmi://localhost/marketplace";
   private String datasource = "mpDB";
   private String dbms = "derby";
   Statement sqlStatement;
   IBank bank;
   Map<String, IMarketCommunicator> customers = new HashMap<String, IMarketCommunicator>();
   List<Item> items = new ArrayList<Item>();
   List<Item> wishes = new ArrayList<Item>();
   
   
   public Marketplace(String bankName)
      throws RemoteException
   {
      super();
      
      try
      {
         bank = (IBank) Naming.lookup(bankName);
      }
      catch (Exception ex)
      {
         System.err.println("Error looking for the bank given the URL: "
                            + bankName);
         System.exit(1);
      }
      
      try
      {
         Naming.rebind(NAME, this);
      }
      catch (MalformedURLException ex)
      {
         System.err.println("Error registering the object Marketplace.");
         System.exit(1);
      }
      
      createDatabaseIfNeeded();
      fillItems();
      fillWishes();
      System.out.println("Server ready.");
   }


   @Override
   public synchronized void login(IMarketCommunicator com)
      throws RemoteException
   {
      customers.put(com.getName(), com);
      System.out.println("Logging in customer: " + com.getName());
      
      System.out.println("Sending items list to the new customer.");
      updateCustomerItemList(com);
      
      updateCustomerWishList(com);
      
      updateCustomerCounters(com);
   }
   
   
   @Override
   public synchronized void logout(IMarketCommunicator com)
      throws RemoteException
   {
      customers.remove(com.getName());
      System.out.println("Logging out customer: " + com.getName());
   }
   
   


   @Override
   public synchronized boolean registerClient(String name, String password)
      throws RemoteException
   {
      try
      {
         ResultSet result = sqlStatement.executeQuery(
            "SELECT * FROM customer WHERE name = '" + name + "'");
         System.out.println(result);

         if (result.next())
         {
            result.close();
            return false;
         }
         else  // Not registered yet
         {
            result.close();
            int rows = sqlStatement.executeUpdate("INSERT INTO customer " +
                                                   "(name, password, sold, bought) " +
                                                    "VALUES ('" + name + "','" + 
                                                            password + "',0,0)");
            if (rows == 1){
               bank.newAccount(name);
               return true;
            }
            else
               return false;
         }
      }
      catch (SQLException ex)
      {
         return false;
      }
      catch (RejectedException e){
          System.out.println("Error creating the new account in the bank");
          System.exit(0);
      }
      return false;
   }


   @Override
   public synchronized void addItem(String name, float price, String ownerId)
      throws RemoteException
   {
      if (addItemToDatabase(name, price, ownerId, false) == false)
      {
         throw new RemoteException();
      }
      else // item added successfully
      {
         Item newItem = new Item(name, price, ownerId);
         items.add(newItem);

         System.out.println("Adding new item from " + ownerId + " for sale: "
                            + name + " - " + price + " SEK.");

         updateCustomersItemLists();

         manageWishes();
      }
   }


   @Override
   public synchronized void addWish(String name, float price, String customerId)
      throws RemoteException
   {
      if (addItemToDatabase(name, price, customerId, true) == false)
      {
         throw new RemoteException();
      }
      else // wish added successfully
      {
         Item newWish = new Item(name, price, customerId);
         wishes.add(newWish);

         System.out.println("Adding new wish from " + customerId + ": "
                            + name + " - " + price + " SEK.");

         IMarketCommunicator customer = customers.get(customerId);
         updateCustomerWishList(customer);

         manageWishes();
      }
   }


   @Override
   public synchronized boolean buyItem(String name, float price, String customerId)
      throws RemoteException
   {
      Item selected = findItem(name, price);
      
      if (selected == null)
      {
         return false;
      }
      else
      {
         String sellerName = selected.getOwner();
         
         boolean result = makePurchase(selected, customerId, sellerName);
         if (result == true)
            manageWishes();
         return result;
      }
   }


   @Override
   public synchronized float getMoney(String customerName)
      throws RemoteException
   {
      try
      {
         float money = bank.getAccount(customerName).getBalance();
         return money;
      }
      catch (RejectedException ex)
      {
         return -1;
      }
   }


   private synchronized ArrayList<Item> getWishList(String ownerId)
   {
      ArrayList<Item> ret = new ArrayList<Item>();
      
      for (Item wish : wishes)
      {
         if (wish.getOwner().equals(ownerId))
            ret.add(wish);
      }
      
      return ret;
   }
   
   
   private synchronized Item findItem(String name, float price)
   {
      for (Item it : items)
      {
         if ((it.getName().equals(name)) && (it.getPrice() == price))
         {
            return it;
         }
      }
      
      return null;
   }
   
   
   private synchronized void updateCustomersItemLists()
   {
      System.out.println("Updating customers' lists of items.");
      
      for (IMarketCommunicator cust : customers.values())
      {
         updateCustomerItemList(cust);
      }
   }
   
   
   private synchronized void updateCustomerItemList(IMarketCommunicator cust)
   {
      try
      {
         cust.updateMarketList(getItemNames());
      }
      catch (RemoteException ex)
      {
         System.err.println("Error updating a customer's list of items.");
      }
   }
   
   
   private synchronized void updateCustomerWishList(IMarketCommunicator customer)
   {
      try
      {
         String custName = customer.getName();
         
         System.out.println("Sending list of wishes to the customer: " + custName);
         
         customer.updateWishList(getWishNames(custName));
      }
      catch (RemoteException ex)
      {
         System.err.println("Error updating a customer's list of wishes.");
      }
   }
   
   
   // Inefficient. O(n^2)
   private synchronized void manageWishes()
   {
      IMarketCommunicator buyer = null;
      boolean any = false;
      
      ArrayList<Item> newsWishes = new ArrayList<Item>();
      
      for (int i = 0; i < wishes.size(); ++i)
      {
         Item wish = wishes.get(i);
         newsWishes.add(wish);
         
         List<Item> workingItems = items;
         
         for (int j = 0; j < workingItems.size(); ++j)
         {
            //buyer = null;
            Item item = workingItems.get(j);
            
            if (item.getName().equals(wish.getName()))
            {
               if (item.getPrice() <= wish.getPrice())
               {
                  boolean ok = makePurchase(item, wish.getOwner(), item.getOwner());
                  if (!ok)
                  {
                     System.err.println("Error making the purchase of a wish.");
                  }
                  else
                  {
                     buyer = customers.get(wish.getOwner());
                     if (buyer == null)
                     {
                        System.err.println("Error updating buyer wish list.");
                     }
                     else
                     {
                        newsWishes.remove(wish);
                        deleteWishFromDatabase(wish);
                        any = true;
                        break;
                     }
                  }
               }
            }
         }
      }
      
      if (any)
      {
         wishes = newsWishes;
         updateCustomerWishList(buyer);
      }
   }


   private synchronized boolean makePurchase(Item item, String customerName, String sellerName)
   {
      IMarketCommunicator buyer = customers.get(customerName);
      IMarketCommunicator seller = customers.get(sellerName);

      if (buyer == null)
      {
         return false;
      }
      else
      {
         try
         {
            float price = item.getPrice();
            
            bank.getAccount(customerName).withdraw(price);
            

            bank.getAccount(sellerName).deposit(price);
            buyer.notifyOfPurchase(item.getName(), item.getPrice());
            if (seller != null)
               seller.notifyOfSale(item.getName(), item.getPrice());

            deleteItemFromDatabase(item);
            items.remove(item);

            increaseBoughtItemsInDatabase(customerName);
            increaseSoldItemsInDatabase(sellerName);
            
            buyer.increaseBoughtItems();
            if (seller != null)
               seller.increaseSoldItems();
            
            updateCustomersItemLists();
         }
         catch (Exception ex)
         {
            return false;
         }
      }
      
      System.out.println("Purchase made from " + customerName);
      
      return true;
   }
   
   
   private synchronized ArrayList<String> getWishNames(String customer)
   {
      ArrayList<Item> wishList = getWishList(customer);
      ArrayList<String> ret = new ArrayList<String>();
         
      for (Item w : wishList)
         ret.add(w.toString());
      
      return ret;
   }
   
   
   private synchronized ArrayList<String> getItemNames()
   {
      ArrayList<String> ret = new ArrayList<String>();
      
      for (Item i : items){
         ret.add(i.toString());
         System.out.println(i.toString());
      }
      return ret;
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


   private void createDatabaseIfNeeded()
   {
      try
      {
         Connection connection = getConnection();
         connection.setAutoCommit(true);
         sqlStatement = connection.createStatement();
         
         boolean clientExists = false, itemExists = false;
         
         int tableNameColumn = 3;
         DatabaseMetaData dbm = connection.getMetaData();
         for (ResultSet rs = dbm.getTables(null, null, null, null); rs.next();)
         {
            String tableName = rs.getString(tableNameColumn);
            
            if (tableName.equalsIgnoreCase("customer"))
            {
               clientExists = true;
               
               if (itemExists)
               {
                  rs.close();
                  break;
               }
            }
            else if (tableName.equalsIgnoreCase("item"))
            {
               itemExists = true;
               
               if (clientExists)
               {
                  rs.close();
                  break;
               }
            }
         }
         
         if (!clientExists)
         {
            sqlStatement.executeUpdate("CREATE TABLE customer (" + 
                                          "name VARCHAR(32) PRIMARY KEY," + 
                                          "password VARCHAR(32)," +
                                          "sold INTEGER," +
                                          "bought INTEGER" +
                                       ")" );
         }
         
         if (!itemExists)
         {
            sqlStatement.executeUpdate("CREATE TABLE item (" + 
                                          "id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                                          "itemName VARCHAR(32)," + 
                                          "price FLOAT," +
                                          "name VARCHAR(32)," +
                                          "wish INTEGER" +
                                       ")");
         }
      }
      catch (Exception e)
      {
         System.err.println("Error retrieving the database.");
         System.exit(1);
      }
   }
   
   
   private void fillItems()
   {
      try
      {
         ResultSet result = sqlStatement.executeQuery(
            "SELECT * FROM item WHERE wish = 0");
         
         while (result.next())
         {
            String name = result.getString("itemName");
            float price = result.getFloat("price");
            String owner = result.getString("name");
            Item i = new Item(name, price, owner);
            
            items.add(i);
         }
         result.close();
      }
      catch (SQLException ex)
      {
         System.err.println("Error retrieving the items from the database.");
         System.exit(1);
      }
   }
   
   
   private void fillWishes()
   {
      try
      {
         ResultSet result = sqlStatement.executeQuery(
            "SELECT * FROM item WHERE wish = 1");

         while (result.next())
         {
            String name = result.getString("itemName");
            float price = result.getFloat("price");
            String owner = result.getString("name");
            Item i = new Item(name, price, owner);

            wishes.add(i);
         }
         result.close();
      }
      catch (SQLException ex)
      {
         System.err.println("Error retrieving the items from the database.");
         System.exit(1);
      }
   }


   @Override
   public boolean isUserRegistered(String name, String password)
      throws RemoteException
   {
      try
      {
         ResultSet result = sqlStatement.executeQuery(
            "SELECT * FROM customer WHERE name = '" + name + "'");
         
         if (result.next())
         {
            String storedPassword = result.getString("password");
            if (storedPassword.equals(password))
            {
               result.close();
               return true;
            }
         }
         else
            result.close();
      }
      catch (SQLException ex)
      {
         System.err.println("Error accessing to the customer.");
      }
      
      return false;
   }


   private boolean addItemToDatabase(String name, float price, String ownerId, boolean isWish)
   {
      try
      {
         int wishV = (isWish) ? 1 : 0;
         
         int rows = sqlStatement.executeUpdate("INSERT INTO item "
                                               + "(itemName, price, name, wish) "
                                               + "VALUES ('" + name + "',"
                                               + String.valueOf(price) + ",'"
                                               + ownerId + "'," + wishV + ")");
         if (rows == 1)
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      catch (SQLException ex)
      {
         return false;
      }
   }


   private void updateCustomerCounters(IMarketCommunicator com)
   {
      try
      {
         ResultSet result = sqlStatement.executeQuery(
            "SELECT * FROM customer WHERE name = '" + com.getName() + "'");

         if (result.next())
         {
            int sales = result.getInt("sold");
            int acquisitions = result.getInt("bought");
            
            com.updateBoughtItems(acquisitions);
            com.updateSoldItems(sales);
         }
         
         result.close();
      }
      catch (Exception ex)
      {
         System.err.println("Error accessing to the customer.");
      }
   }


   private void deleteWishFromDatabase(Item wish)
   {
      try
      {
         // Find the first occurrence:
         ResultSet result = sqlStatement.executeQuery(
            "SELECT * FROM item WHERE itemName = '" + wish.getName() + "'" + 
                                    " AND price = " + String.valueOf(wish.getPrice()) + 
                                    " AND name = '" + wish.getOwner() + "'" + 
                                    " AND wish = 1");
         
         if (result.next())
         {
            int selectedId = result.getInt("id");
            
            sqlStatement.executeUpdate("DELETE FROM item "
                                       + "WHERE id = " + String.valueOf(selectedId));
         }
      }
      catch (SQLException ex)
      {
      }
   }


   private void deleteItemFromDatabase(Item item)
   {
      try
      {
         // Find the first occurrence:
         ResultSet result = sqlStatement.executeQuery(
            "SELECT * FROM item WHERE itemName = '" + item.getName() + "'" + 
                                    " AND price = " + String.valueOf(item.getPrice()) + 
                                    " AND name = '" + item.getOwner() + "'" + 
                                    " AND wish = 0");
         
         if (result.next())
         {
            int selectedId = result.getInt("id");
            
            sqlStatement.executeUpdate("DELETE FROM item "
                                       + "WHERE id = " + String.valueOf(selectedId));
         }
      }
      catch (SQLException ex)
      {
      }
   }


   private void increaseBoughtItemsInDatabase(String customerName)
   {
      try
      {
         sqlStatement.executeUpdate(
            "UPDATE customer SET bought = bought + 1 "
            + "WHERE name = '" + customerName + "'");
      }
      catch (SQLException ex)
      {
      }
   }
   
   
   private void increaseSoldItemsInDatabase(String customerName)
   {
      try
      {
         sqlStatement.executeUpdate(
            "UPDATE customer SET sold = sold + 1 "
            + "WHERE name = '" + customerName + "'");
      }
      catch (SQLException ex)
      {
      }
   }
}
