/**
 * ReturnDlg
 * Custom dialog class with methods to get details of a loan,
 *  and to cancel it, deleting the loan transaction record 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*; //Date, collection classes

public class ReturnDlg extends JDialog implements ActionListener {
  private MainMenu parent;
  private JTextField txtItemID;
  private JButton btnSubmit, btnHide;

  public ReturnDlg(MainMenu p) {
    setTitle("Register Return of an Item");
    parent = p; //data structures are here

    //Components -
    txtItemID = new JTextField(10); //input field, 10 columns wide
    btnSubmit = new JButton("Submit");
    btnHide   = new JButton("Hide");

    //Layout -
    JPanel pnl = new JPanel();
    pnl.add(new JLabel("Item code:"));
    pnl.add(txtItemID);
    add(pnl, BorderLayout.CENTER);

    pnl = new JPanel();
    pnl.add(btnSubmit);
    pnl.add(btnHide);
    add(pnl, BorderLayout.SOUTH);

    setBounds(100, 100, 400, 100);


    //Action
    btnSubmit.addActionListener(this);
    btnHide.addActionListener(this);
  } //end constructor

  /**
   * Actions: on click of 'Submit', find loan record and delete from database;
   *          on click of 'Hide', hide the dialogue window.
   */
  public void actionPerformed(ActionEvent evt) {
    Object src = evt.getSource();
    if (src == btnHide) {
      setVisible(false);
      txtItemID.setText("");
    }
    else if (src == btnSubmit) {
      processReturn();
      txtItemID.setText("");
    }
  } //end actionPerformed


  /**
   * Does the actual work of finding a loan record and deleting it; 
   * Also updates 'borrowedBy' field of Item.
   */
  public void processReturn() {
    try {
      Integer itmID = new Integer(txtItemID.getText());
      Item itm = parent.getItems().get(itmID);
      if (itm == null) {
      JOptionPane.showMessageDialog(this, "Item not found",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      Borrower bby = itm.getBorrowedBy();
      if (bby == null) {
        JOptionPane.showMessageDialog(this, "Not on loan",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      //Find LoanTransaction ...
      LoanTransaction mrkr = null;
      for (LoanTransaction t: parent.getLoans()) {
        if (t.getBorrower() == bby && t.getItem() == itm) {
          mrkr = t;
          break;
        }
      }
      if (mrkr == null) {
        JOptionPane.showMessageDialog(this, "Loan transaction not found",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      //Otherwise ...
      parent.getLoans().remove(mrkr);
      itm.clearBorrowedBy();
      System.out.printf("Item returned: [%s]\n\n", itm);
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
            "Number format error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
