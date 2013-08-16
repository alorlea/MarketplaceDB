/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MarketClient.java
 *
 * Created on 16-nov-2011, 12:59:43
 */
package client;

import java.awt.Color;
import java.rmi.RemoteException;
import javax.swing.DefaultListModel;

/**
 *
 * @author Alberto Lorente Leal <albll@kth.se>, <a.lorenteleal@gmail.com>
 */
public class MarketClient extends javax.swing.JFrame {
    private MarketCommunicator market;
    private String name;
    private String pass;

    /** Creates new form MarketClient */
    public MarketClient(String name, String pass) {
       this.name = name;
       this.pass= pass;
       
        initComponents();
        try{
            market = new MarketCommunicator(this, name, pass);
        }
        catch(RemoteException e){
            System.err.println("Error creating the communicator with the server.");
            System.exit(1);
        }
        wishList.setEnabled(false);
        wishes.setEnabled(false);
        buyItemButton.setEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        marketplaceProductsList = new javax.swing.JScrollPane();
        marketPlaceList = new javax.swing.JList();
        marketplaceProductsLabel = new javax.swing.JLabel();
        wishlistLabel = new javax.swing.JLabel();
        wishList = new javax.swing.JScrollPane();
        wishes = new javax.swing.JList();
        sellItemLabel = new javax.swing.JLabel();
        sellNameTextField = new javax.swing.JTextField();
        sellNameLabel = new javax.swing.JLabel();
        sellPriceTextField = new javax.swing.JTextField();
        sellPriceLabel = new javax.swing.JLabel();
        wishItemLabel = new javax.swing.JLabel();
        wishNameLabel = new javax.swing.JLabel();
        wishPriceLabel = new javax.swing.JLabel();
        wishNameTextField = new javax.swing.JTextField();
        wishPriceTextField = new javax.swing.JTextField();
        sellButton = new javax.swing.JButton();
        wishButton = new javax.swing.JButton();
        buyItemButton = new javax.swing.JButton();
        moneyLabel = new javax.swing.JLabel();
        amountMoneyLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        itemsBoughtLabel = new javax.swing.JLabel();
        itemsSoldLabel = new javax.swing.JLabel();
        bought = new javax.swing.JLabel();
        sold = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Marketplace Client");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onClosingWindow(evt);
            }
        });

        marketPlaceList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                marketPlaceListValueChanged(evt);
            }
        });
        marketplaceProductsList.setViewportView(marketPlaceList);

        marketplaceProductsLabel.setText("Marketplace Products");

        wishlistLabel.setText("Wish List");

        wishList.setViewportView(wishes);

        sellItemLabel.setText("Sell Item");

        sellNameTextField.setName("sellNameTextField"); // NOI18N

        sellNameLabel.setText("Name:");

        sellPriceLabel.setText("Price:");

        wishItemLabel.setText("Wish Item");

        wishNameLabel.setText("Name:");

        wishPriceLabel.setText("Price:");

        sellButton.setText("Sell");
        sellButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellItem(evt);
            }
        });

        wishButton.setText("Wish");
        wishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wishItem(evt);
            }
        });

        buyItemButton.setText("Buy");
        buyItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyItem(evt);
            }
        });

        moneyLabel.setText("Money:");

        statusLabel.setForeground(new java.awt.Color(0, 29, 255));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusLabel.setText("Ready.");
        statusLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        itemsBoughtLabel.setText("Items Bought:");
        itemsBoughtLabel.setName("itemsBoughtLabel"); // NOI18N

        itemsSoldLabel.setText("Items Sold:");
        itemsSoldLabel.setName("itemsSoldLabel"); // NOI18N

        bought.setText("0");
        bought.setName("bought"); // NOI18N

        sold.setText("0");
        sold.setName("sold"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(buyItemButton)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(marketplaceProductsList, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                .addComponent(marketplaceProductsLabel)))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(wishlistLabel)
                            .addComponent(wishList, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(wishItemLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(wishNameLabel)
                                    .addComponent(wishPriceLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(wishPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(wishNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(wishButton, javax.swing.GroupLayout.Alignment.TRAILING))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(moneyLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(amountMoneyLabel))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(itemsBoughtLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(bought, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(sellItemLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sellNameLabel)
                                        .addComponent(sellPriceLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sellPriceTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                        .addComponent(sellNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(itemsSoldLabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(sold, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                                .addComponent(sellButton))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {amountMoneyLabel, sellNameTextField, sellPriceTextField, wishNameTextField, wishPriceTextField});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buyItemButton, sellButton, wishButton});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {marketplaceProductsList, wishList});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(marketplaceProductsLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(wishlistLabel)
                        .addComponent(moneyLabel)
                        .addComponent(amountMoneyLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(wishList, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                            .addComponent(marketplaceProductsList, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buyItemButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemsBoughtLabel)
                            .addComponent(bought))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemsSoldLabel)
                            .addComponent(sold))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sellItemLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sellNameLabel)
                            .addComponent(sellNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sellPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sellPriceLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sellButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wishItemLabel)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(wishNameLabel)
                            .addComponent(wishNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(wishPriceLabel)
                            .addComponent(wishPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wishButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLabel))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void wishItem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wishItem
        String itemName = wishNameTextField.getText().trim();
        String itemPrice = wishPriceTextField.getText().trim();
        
        try 
        {
           float actualPrice = Float.parseFloat(itemPrice);
           
           if (!itemName.isEmpty() && !itemPrice.isEmpty())
           {
              market.addWish(itemName, actualPrice);
              wishNameTextField.setText("");
              wishPriceTextField.setText("");
           }
        }
        catch (NumberFormatException e)
        {
           statusLabel.setText("Wrong format of number for the price.");
        }
    }//GEN-LAST:event_wishItem

    private void sellItem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellItem
        String itemName = sellNameTextField.getText().trim();
        String itemPrice = sellPriceTextField.getText().trim();
        
        try
        {
           float actualPrice = Float.parseFloat(itemPrice);
           
           if(!itemName.isEmpty() && !itemPrice.isEmpty()){
            market.sellItem(itemName, actualPrice);
            sellNameTextField.setText("");
            sellPriceTextField.setText("");
           }
        }
        catch (NumberFormatException e)
        {
           statusLabel.setText("Wrong format of number for the price.");
        }
    }//GEN-LAST:event_sellItem

    private void buyItem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyItem
        Object selection = marketPlaceList.getModel().getElementAt(
                marketPlaceList.getSelectedIndex());
        
        String [] item = ((String)selection).split(" - ");
        String itemName = item[0];
        String itemPrice = (item[1].replaceAll("SEK.", "")).trim();
        
        if(!itemName.isEmpty() && !itemPrice.isEmpty()){
            market.buyItem(itemName, Float.parseFloat(itemPrice));
            marketPlaceList.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_buyItem

    private void marketPlaceListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_marketPlaceListValueChanged
        if(marketPlaceList.getSelectedIndex()!=-1){
            buyItemButton.setEnabled(true);
        }
        else{
            buyItemButton.setEnabled(false);
            
        }
            
    }//GEN-LAST:event_marketPlaceListValueChanged

   private void onClosingWindow(java.awt.event.WindowEvent evt)//GEN-FIRST:event_onClosingWindow
   {//GEN-HEADEREND:event_onClosingWindow
      market.unregister();
   }//GEN-LAST:event_onClosingWindow

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amountMoneyLabel;
    private javax.swing.JLabel bought;
    private javax.swing.JButton buyItemButton;
    private javax.swing.JLabel itemsBoughtLabel;
    private javax.swing.JLabel itemsSoldLabel;
    private javax.swing.JList marketPlaceList;
    private javax.swing.JLabel marketplaceProductsLabel;
    private javax.swing.JScrollPane marketplaceProductsList;
    private javax.swing.JLabel moneyLabel;
    private javax.swing.JButton sellButton;
    private javax.swing.JLabel sellItemLabel;
    private javax.swing.JLabel sellNameLabel;
    private javax.swing.JTextField sellNameTextField;
    private javax.swing.JLabel sellPriceLabel;
    private javax.swing.JTextField sellPriceTextField;
    private javax.swing.JLabel sold;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JButton wishButton;
    private javax.swing.JLabel wishItemLabel;
    private javax.swing.JScrollPane wishList;
    private javax.swing.JLabel wishNameLabel;
    private javax.swing.JTextField wishNameTextField;
    private javax.swing.JLabel wishPriceLabel;
    private javax.swing.JTextField wishPriceTextField;
    private javax.swing.JList wishes;
    private javax.swing.JLabel wishlistLabel;
    // End of variables declaration//GEN-END:variables
    
    public void updateMarketPlace(DefaultListModel items){
       marketPlaceList.setModel(items);
       marketplaceProductsList.repaint(500);
    }
    
    public void updateWishList(DefaultListModel items){
       wishes.setModel(items);
       wishList.repaint(500);
    }
    
    public void updateAvailableMoney(float Money){
        if(Money!=-1){
            amountMoneyLabel.setText(String.valueOf(Money));
            amountMoneyLabel.repaint();
        }
        else{
            statusLabel.setText("Error while recoverying the money");
            statusLabel.setForeground(Color.red);
        }
    }
    
    public void updateItemsBought(int items){
        bought.setText(String.valueOf(items));
        bought.repaint();
    }
    
    public void updateSoldItems(int items){
        sold.setText(String.valueOf(items));
        sold.repaint();
    }


   void showNotification(String msg)
   {
      statusLabel.setText(msg);
   }

    void incrementSoldItems() {
        int i = Integer.parseInt(sold.getText());
        i++;
        sold.setText(String.valueOf(i));
        sold.repaint();
    }

    void incrementBoughtItems() {
        int i = Integer.parseInt(bought.getText());
        i++;
        bought.setText(String.valueOf(i));
        bought.repaint();
    }
}
