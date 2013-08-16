/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 *
 * @author Alberto Lorente Leal <albll@kth.se>, <a.lorenteleal@gmail.com>
 */
public interface IMarketCommunicator
   extends Remote
{
   public void updateWishList(ArrayList<String> wishNames)
      throws RemoteException;


   public void updateMarketList(ArrayList<String> itemNames)
      throws RemoteException;
   
   
   public String getName()
      throws RemoteException;
   
   
   public void notifyOfPurchase(String itemName, float itemPrice)
      throws RemoteException;
   
   public String getPassword()
           throws RemoteException;
   
   public void notifyOfSale(String itemName, float itemPrice)
      throws RemoteException;
   public void updateBoughtItems(int items) 
           throws RemoteException;
   public void updateSoldItems(int items) 
           throws RemoteException;
   
   public void increaseBoughtItems() throws RemoteException;
   
   public void increaseSoldItems() throws RemoteException;
   
}
