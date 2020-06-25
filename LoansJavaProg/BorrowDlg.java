/**
 * Custom dialog class for borrow
 *
 * @author (Lewis Ellison)
 * @version (07/12/17)
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class BorrowDlg extends JDialog implements ActionListener {
    private MainMenu parent;
    private JTextField bID, txtItemID;
    private JButton btnSubmit, btnHide;

    /**
     * Constructor for objects of class BorrowDlg
     */
    public BorrowDlg(MainMenu p)
    {
        
        setTitle("Register lending of an item");
        parent = p;
        
        txtItemID = new JTextField(10); //input field, 10 columns wide
        bID = new JTextField(10); //input field, 10 columns wide
        btnSubmit = new JButton("Submit");
        btnHide   = new JButton("Hide");
        
        JPanel pnl = new JPanel();
        pnl.add(new JLabel("Item code:"));
        pnl.add(txtItemID);
        add(pnl, BorderLayout.WEST);
        
        JPanel pn01 = new JPanel();
        pn01.add(new JLabel("Borrower code:"));
        pn01.add(bID);
        add(pn01, BorderLayout.EAST);

        pnl = new JPanel();
        pnl.add(btnSubmit); //add submit buttons
        pnl.add(btnHide); //add hide button
        add(pnl, BorderLayout.SOUTH);

        setBounds(300, 300, 300, 150);

        //Action
        btnSubmit.addActionListener(this);
        btnHide.addActionListener(this);
        
    }

    /**
     * On click of button "Hide" the window is made invisible
     * On click of button "Submit" the input is checked to see if valid,
     * if the the item is valid it is lent and a record of loan transaction is made.
     */
    public void actionPerformed(ActionEvent evt)
    {
        
        Object source = evt.getSource();
        
        if (source == btnHide)
        {
            
            setVisible(false); //Hides window
            txtItemID.setText("");
            bID.setText("");
            
        }
        
        else if (source == btnSubmit)
        {
            
            processBorrow(); //calls borrow item
            txtItemID.setText("");
            bID.setText("");
            
        }
    }
    
    public void processBorrow()
    {
       try{
        
        Integer itemID = new Integer(txtItemID.getText());
        
        Item item = parent.getItems().get(itemID);
        
        Integer brwrID = new Integer(bID.getText());
        
        Borrower brwr = parent.getBorrowers().get(brwrID);
        
        Borrower borrowedBy = item.getBorrowedBy();
        
        
        if (item == null) {
            
            JOptionPane.showMessageDialog(this, "Item not found", //error
            "Error", JOptionPane.ERROR_MESSAGE);
            return;
            
        }
        
        
        if (borrowedBy != null) {
            
            JOptionPane.showMessageDialog(this, "Item already on loan", //error
            "Error", JOptionPane.ERROR_MESSAGE);
            return;
         
        }   
        
        
        
        
        if (brwr == null) {
            
            JOptionPane.showMessageDialog(this, "Borrower not found", //error
            "Error", JOptionPane.ERROR_MESSAGE);
            return;
            
        }
        
       
        boolean Late = false; // 
        
        if (borrowedBy == null){
            
            for (int i = 0;i<parent.loans.size();i++){ //runs through all loans and checks for late loans
                
                LoanTransaction loanTran = parent.loans.get(i);
                long lentTime = loanTran.getTimeStamp();
                long currentTime = System.currentTimeMillis();
                
                if(currentTime - parent.LOANMAX > lentTime)
                {
                    
                    Borrower BORROW = loanTran.getBorrower(); // Find loaner for late book
                    
                    int BorrowerID = BORROW.getBwrID(); // Find borrower ID 
                    
                    if (BorrowerID == brwrID){ // checks if they are the same ID
                        
                        JOptionPane.showMessageDialog(this, "Book is Overdue for this Borrower",
                        "Error", JOptionPane.ERROR_MESSAGE); // error.
                        Late = true;
                        return;
                        
                    }
                }
            }
            
            if (Late != true){
                
                LoanTransaction loanTran = new LoanTransaction(brwr, item, System.currentTimeMillis()); // new loan transaction
                parent.loans.add(loanTran); //adds loan transaction
                item.setBorrowedBy(brwr); //sets item borrowed by to the borrower ID
                System.out.println("Item " + item + " borrowed by " + brwr);
                System.out.println();
                
            }   
        }
        
       }catch (NumberFormatException ex) {
         
          JOptionPane.showMessageDialog(this, ex.getMessage(),
          "Number format error", JOptionPane.ERROR_MESSAGE);
            
       }
    }
}