import java.util.Date;

/**
 * LoanTransaction
 * class to record borrowing of items
 */
public class LoanTransaction {
  private Borrower bdBy;
  private Item item;
  private long timeStamp;

  /**
   * Constructor of Loan record with given Borrower, Item,
   *   long integer timestamp
   */  
  public LoanTransaction(Borrower b, Item i, long ts) {
    bdBy = b;
    item = i;
    timeStamp = ts;
  }
  
  public Borrower getBorrower() { return bdBy; }
  public Item getItem()         { return item; }
  public long getTimeStamp()    { return timeStamp; }

  public String toString() {
    return String.format("Item [%s] borrowed by [%s]; timestamp %d",
                          item, bdBy, timeStamp);
  }

  /**
   * Make a string representation of the Lone without the borrower reference
   *
   * In most contexts, Item has correct Borrower reference
   */
  public String toStringOmitBwr() {
    return String.format("Item [%s] timestamp %d", item, timeStamp);
  }
}
