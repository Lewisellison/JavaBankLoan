import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*; //ArrayList; HashMap; LinkedList;

/**
 * Graphical user interface
 * Also contains Map of Integers (borrower IDs) to Borrowers,
 *               Map of Integers (item IDs) to Items
 *               List of Loan records
 */
public class MainMenu extends JFrame implements ActionListener {
  public static final long LOANMAX = 1814400000; //ms = 21 days
  // data collections
  private Map<Integer, Borrower> borrowers;
  private Map<Integer, Item> items;
  public List<LoanTransaction> loans;
  
  //GUI
  private ReturnDlg returnDlg;
  private BorrowDlg borrowDlg;
  private JButton btnReadData, btnSaveLoans, btnLendItems,
  btnReturnItems, btnListLoans, btnListODLoans;

  public static void main(String[] args) {
    MainMenu app = new MainMenu();
    app.setVisible(true);
  }

  /**
   * Initialise the data stores and main menu 
   *   Menu consists of JButtons with current class as ActionListener
   */
  public MainMenu() { //constructor
    // Database
    borrowers = new HashMap<Integer, Borrower>();
    items = new HashMap<Integer, Item>();
    loans = new LinkedList<LoanTransaction>();

    // GUI - create custom dialog instances
    returnDlg = new ReturnDlg(this);
    borrowDlg = new BorrowDlg(this);

    // GUI - set window properties
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 100, 250, 300);

    //GUI - main menu buttons    
    JPanel mainPnl = new JPanel();
    mainPnl.setLayout(new GridLayout(3,1));

    btnReadData = new JButton("Read Data");
    btnReadData.addActionListener(this);
    mainPnl.add(btnReadData);
    
    btnLendItems = new JButton("Loan Items");
    btnLendItems.addActionListener(this);
    mainPnl.add(btnLendItems);
    
    btnReturnItems = new JButton("Return Items");
    btnReturnItems.addActionListener(this);
    mainPnl.add(btnReturnItems);
    
    btnListLoans = new JButton("List Loans");
    btnListLoans.addActionListener(this);
    mainPnl.add(btnListLoans);
    
    btnListODLoans = new JButton("List Overdue loans");
    btnListODLoans.addActionListener(this);
    mainPnl.add(btnListODLoans);
    
    btnSaveLoans = new JButton("Save Loans");
    btnSaveLoans.addActionListener(this);
    mainPnl.add(btnSaveLoans);

    add(mainPnl, BorderLayout.CENTER);
  } //end constructor

  /**
   * Accessors for data structures
   */
  public Map<Integer, Borrower>  getBorrowers() { return borrowers; }
  public Map<Integer, Item>      getItems()     { return items; }
  public List<LoanTransaction> getLoans()       { return loans; }

  /**
   * Actions in response to buttons
   */
  public void actionPerformed(ActionEvent evt) {
      
    Object src = evt.getSource();
    //read borrowers, items, loans JUST ONCE to initialise the system
    
    if (src == btnReadData) { 
        
      readBorrowerData(); 
      listBorrowers();
      readItemData(); 
      readLoans(); // saved from a previous session 
      listItems(); // AFTER loans reloaded
      
      btnReadData.setEnabled(false);     
      
    } else if (src == btnLendItems) { // borrowDlg dialog will do multiple loans
        
      borrowDlg.setVisible(true);
      
    } else if (src == btnReturnItems) { // returnDlg will do multiple returns
        
      returnDlg.setVisible(true);
      
    } else if (src == btnListLoans){ //lists current loans
        
      listLoans();
        
    } else if (src == btnListODLoans){ //lists overdue loans
        
      listODLoans();
        
    } else if (src == btnSaveLoans){ //saves loans in file loans.txt
        
      SaveLoans();
        
    }
  }
  
  /**
   * Read data from borrowers.txt using a Scanner; unpack and populate
   *   borrowers Map. List displyed on console.  
   */
  public void readBorrowerData() {
      
    String fnm="", snm="", pcd="";
    int num=0, id=1;
    
    try {
      Scanner scnr = new Scanner(new File("borrowers.txt"));
      scnr.useDelimiter("\\s*#\\s*");
      while (scnr.hasNextInt()) {
        id  = scnr.nextInt();
        snm = scnr.next();
        fnm = scnr.next();
        num = scnr.nextInt();
        pcd = scnr.next();
        borrowers.put(new Integer(id), new Borrower(id, snm, fnm, num, pcd));
      }
      scnr.close();
    } catch (NoSuchElementException e) {
      System.out.printf("%d %s %s %d %s\n", id, snm, fnm, num, pcd);
      JOptionPane.showMessageDialog(this, e.getMessage(),
          "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Borrowers file not found",
          "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
  } //end readBorrowerData
  
  /**
   * List Borrowers on console
   */
  public void listBorrowers() {
      
    System.out.println("Borrowers:");
    
    for (Borrower b: borrowers.values()) {
        
      System.out.println(b);
      
    }
    
    System.out.println();
  }

  /**
   * Read data from items.txt using a Scanner; unpack and populate
   *   items Map. List displyed on console.  
   */
  public void readItemData() {
    String ttl="", aut="";
    int id=1;
    try {
      Scanner scnr = new Scanner(new File("items.txt"));
      scnr.useDelimiter("\\s*#\\s*");
      while (scnr.hasNextInt()) {
        id  = scnr.nextInt();
        ttl = scnr.next();
        aut = scnr.next();
        items.put(new Integer(id), new Item(id, ttl, aut));
      }
      scnr.close();
    } catch (NoSuchElementException e) {
      System.out.printf("%d %s %s\n", id, ttl, aut);
      JOptionPane.showMessageDialog(this, e.getMessage(),
          "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Items file not found",
          "Unable to open data file", JOptionPane.ERROR_MESSAGE);
    }
  } //end readItemData

  /**
   * List Items on console
   */
  public void listItems() {
      
    System.out.println("Items:");
    
    for (Item i: items.values()) {
        
      System.out.println(i);
      
    }
    
    System.out.println();
    
  }

  //Assumes borrowers, items have been loaded
  public void readLoans() {
      
    if (loans.size() > 0) {
        
      JOptionPane.showMessageDialog(this, "Already some loans!",
      "Error", JOptionPane.ERROR_MESSAGE);
      return;
      
    }
    
    try {
        
      Scanner scnr = new Scanner(new File("loans.txt"));
      
      while (scnr.hasNext()) {
          
        Borrower b = borrowers.get(scnr.nextInt());
        Item i = items.get(scnr.nextInt());
        LoanTransaction t = new LoanTransaction(b, i, scnr.nextLong());
        loans.add(t);
        i.setBorrowedBy(b);
        
      }
      
      System.out.printf("Loan Added \n", loans.size());
      
    } catch (IOException e) {
        
      JOptionPane.showMessageDialog(this, "File not found",
          "Error", JOptionPane.ERROR_MESSAGE);
          
    }
  }     //end readloans
  
  /**
   * List loans 
   */
  public void listLoans(){
      
      System.out.println("Loans:");
      
      for (int i = 0; i < loans.size(); i++){ //loops loans
          
          System.out.println(loans.get(i)); //prints each loan on a new line
          
      }
      
      System.out.println();
      
  }     //end list loans
  
  /**
   * List overdue loans
   */
  public void listODLoans()
  {
      System.out.println("Overdue Loans:");
      
      for (int i = 0; i < loans.size(); i++){ //loops loans
          
          LoanTransaction loanTran = loans.get(i); //created new loan transaction for loan
          long lentTime = loanTran.getTimeStamp(); // shows time stamps
          long currentTime = System.currentTimeMillis(); //saves the current time
          
          if (currentTime - LOANMAX > lentTime) { //If lendtime is greater than the mac loan time then print to console
              
              System.out.println(loans.get(i));
              
          }
      }
      System.out.println();
  }     //end listODLoans
  
  /**
   * Saves loans to file loans
   */
  public void SaveLoans()
  {
      try{
          
          FileOutputStream output = new FileOutputStream(new File("loans.txt")); //creates file output stream
          PrintWriter printWriter = new PrintWriter (output); //creates printWriter
          
          for (int i = 0;i<loans.size();i++) //loops through each loan
          {
              
              LoanTransaction loanTran = loans.get(i);
              int borrower = loanTran.getBorrower().getBwrID(); //gets borrower ID
              int item = loanTran.getItem().getItemID(); //gets ID 
              long time = loanTran.getTimeStamp(); // gets timestamp
              printWriter.println(borrower);// prints borrower ID
              printWriter.println(item);// prints ID
              printWriter.println(time);// Timestamp Print
              
          }
          
          printWriter.close(); //close printwriter
          output.close(); //close printwriter
          
      } catch (IOException e) {
          
          JOptionPane.showMessageDialog(this, "Failed", "PrintWriter Could Not Be Opened", JOptionPane.ERROR_MESSAGE);
          
      }
      
      System.out.println("Loans saved succesfully");
      System.out.println();
      
  } //end SaveLoans
} //end class
