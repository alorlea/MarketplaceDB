/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import server.IMarketplace;
import java.rmi.Naming;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.DefaultListModel;


/**
 *
 * @author alberto
 */
public class MarketCommunicator
   extends UnicastRemoteObject
   implements IMarketCommunicator
{
   private String name;
   private String pass;
   private float money;
   private IMarketplace marketplace;
   private MarketClient client;
   


   MarketCommunicator(MarketClient client, String name, String pass)
      throws RemoteException
   {
      this.client = client;
      this.name = name;
      this.pass = pass;


      try
      {

         marketplace = (IMarketplace) Naming.lookup("rmi://localhost/marketplace");
         marketplace.login(this);
         money = marketplace.getMoney(name);
         client.updateAvailableMoney(money);
      }
      catch (Exception e)
      {
         System.err.println("The runtime failed: " + e.getMessage());
         System.exit(0);
      }
      
      
      
   }


   /**
    * Method to call the server that we are selling a new item 
    * in the marketplace
    * @param itemName
    * @param itemPrice 
    */
   void sellItem(String itemName, float itemPrice)
   {
      try
      {
         marketplace.addItem(itemName, itemPrice, name);
      }
      catch (RemoteException e)
      {
         System.err.println(e.getMessage());
      }
   }


   /**
    * Method to add a new wish item
    * @param itemName
    * @param itemPrice 
    */
   void addWish(String itemName, float itemPrice)
   {
      try
      {
         marketplace.addWish(itemName, itemPrice, name);
      }
      catch (RemoteException e)
      {
         System.err.println(e.getMessage());
      }

   }


   /**
    * Method to buy a item from the marketplace
    * @param itemName
    * @param itemPrice
    * @param itemOwner 
    */
   void buyItem(String itemName, float itemPrice)
   {
      try
      {
         boolean ok = marketplace.buyItem(itemName, itemPrice, name);
         if (!ok)
            client.showNotification("It was not possible to make the purchase.");
      }
      catch (RemoteException e)
      {
         System.err.println(e.getMessage());
      }

   }


   /**
    * Method for the marketplace to notify us an update of the wishlist
    * @throws RemoteException 
    */
   @Override
   public synchronized void updateWishList(ArrayList<String> wishNames)
      throws RemoteException
   {
       DefaultListModel list = new DefaultListModel();
       for (String i : wishNames)
          list.addElement(i);
       
      client.updateWishList(list);
   }


   /**
    * Method for the marketplace to notify us of an update in the market list
    * @throws RemoteException 
    */
   @Override
   public synchronized void updateMarketList(ArrayList<String> itemNames)
      throws RemoteException
   {
       DefaultListModel list = new DefaultListModel();
       for (String i : itemNames)
          list.addElement(i);
       
      client.updateMarketPlace(list);
   }


   @Override
   public synchronized String getName()
      throws RemoteException
   {
      return name;
   }
   
   
   @Override
   public synchronized void notifyOfPurchase(String itemName, float itemPrice)
      throws RemoteException
   {
      client.showNotification("You have acquired a " + itemName + " for " + 
                              itemPrice + " SEK.");
      money -= itemPrice;
      client.updateAvailableMoney(money);
   }
   
   
   @Override
   public synchronized void notifyOfSale(String itemName, float itemPrice)
      throws RemoteException
   {
      client.showNotification("You have sold a " + itemName + " for " + 
                              itemPrice + " SEK.");
      money += itemPrice;
      client.updateAvailableMoney(money);
   }


   void unregister()
   {
      unregisterFromMarketplace();
   }
     
   void unregisterFromMarketplace()
   {
      try
      {
         marketplace.logout(this);
      }
      catch (RemoteException ex)
      {
         System.err.println("Error unregistering customer from marketplace.");
      }
   }

    @Override
    public synchronized void updateBoughtItems(int items) throws RemoteException {
        client.updateItemsBought(items);
    }

    @Override
    public synchronized void updateSoldItems(int items) throws RemoteException {
        client.updateSoldItems(items);
    }

    @Override
    public String getPassword() throws RemoteException {
        return pass;
    }

    @Override
    public synchronized void increaseBoughtItems() throws RemoteException {
        client.incrementBoughtItems();
    }

    @Override
    public synchronized void increaseSoldItems() throws RemoteException {
        client.incrementSoldItems();
    }
}
