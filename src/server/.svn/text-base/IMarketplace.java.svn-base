/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.IMarketCommunicator;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 *
 * @author alberto
 */
public interface IMarketplace
   extends Remote
{
   public float getMoney(String customerName)
      throws RemoteException;
   
   
   public boolean registerClient(String name, String password)
      throws RemoteException;
   
   
   public void login(IMarketCommunicator com)
      throws RemoteException;
   
   
   public void logout(IMarketCommunicator com)
      throws RemoteException;


   public void addItem(String name, float price, String ownerId)
      throws RemoteException;


   public void addWish(String name, float price, String customerId)
      throws RemoteException;


   public boolean buyItem(String name, float price, String ownerId)
      throws RemoteException;

   public boolean isUserRegistered(String name, String password) 
      throws RemoteException;
}
