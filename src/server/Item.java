/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;



/**
 *
 * @author Fernando Garcia Sanjuan, <fgs@kth.se>, <fdosanjuan@gmail.com>
 */
public class Item
{
   String name;
   String owner;
   float price;


   public Item(String name, float price, String owner)
   {
      super();  
      
      this.name = name;
      this.owner = owner;
      this.price = price;
   }
   
   
   public String getName()
   {
      return name;
   }


   public float getPrice()
   {
      return price;
   }


   public String getOwner()
   {
      return owner;
   }


   @Override
   public String toString()
   {
      return (name + " - " + price + " SEK.");
   }
   
   
}
