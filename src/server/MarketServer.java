/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;



/**
 *
 * @author Fernando Garcia Sanjuan, <fgs@kth.se>, <fdosanjuan@gmail.com>
 */
public class MarketServer
{
   private static final String USAGE = "java server.MarketServer [<bank_url>]";
   private static final String BANK = "rmi://localhost/bank";
   
   public static void main(String[] args)
   {
      String bankUrl;
      
      switch (args.length)
      {
         case 0:
            bankUrl = BANK;
            createMarket(bankUrl);
            break;
         case 1:
            bankUrl = args[0];
            createMarket(bankUrl);
            break;
         default:
            System.err.println(USAGE);
            System.exit(1);     
      }
   }
   
   
   private static void createMarket(String bankUrl)
   {
      try
      {
         IMarketplace market = new Marketplace(bankUrl);
      }
      catch (RemoteException ex)
      {
         System.err.print("Error creating the Market.");
         System.exit(1);
      }
   }
}
