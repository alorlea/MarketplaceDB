package bank;


public class Server
{
   private static final String USAGE = "java bank.Server [rmi-URL of a bank] "
                                       + "[database] [dbms: access, derby, pointbase, cloudscape, mysql]";
   private static final String BANK = "rmi://localhost/bank";
   private static final String BANKNAME = "bank";
   private static final String DATASOURCE = "mpDB";
   private static final String DBMS = "derby";


   public Server(String bankName, String datasource, String dbms)
   {
      try
      {
         IBank bankobj = new Bank(BANKNAME, datasource, dbms);
         // Register the newly created object at rmiregistry.
         java.rmi.Naming.rebind(bankName, bankobj);
         System.out.println(bankobj + " is ready.");
      }
      catch (Exception e)
      {
         System.err.println("Error registering the bank into the rmiregistry.");
      }
   }


   public static void main(String[] args)
   {
      if (args.length > 3 || (args.length > 0 && args[0].equalsIgnoreCase("-h")))
      {
         System.out.println(USAGE);
         System.exit(1);
      }

      String bankName = null;
      if (args.length > 0)
      {
         bankName = args[0];
      }
      else
      {
         bankName = BANK;
      }

      String datasource = null;
      if (args.length > 1)
      {
         datasource = args[1];
      }
      else
      {
         datasource = DATASOURCE;
      }

      String dbms = null;
      if (args.length > 2)
      {
         dbms = args[2];
      }
      else
      {
         dbms = DBMS;
      }

      new Server(bankName, datasource, dbms);
   }
}
