/**
 * Item
 * Encapsulates items in the library collection
 */
public class Item {
  private Integer itemID;
  private String title;
  private String author;
  private Borrower borrowedBy;
  
  /**
   * Constructor for Item with given item ID, title, author
   */
  public Item(Integer iID, String ttl, String aut)
                    throws IllegalArgumentException {
    if (!dataValid(iID, ttl, aut)) {
      throw new IllegalArgumentException("Bad arg(s) in Item constructor");
    }
    borrowedBy = null; //initially, not out on loan
    itemID = iID;
    title = ttl;
    author = aut;
  }
  
  public Integer getItemID() { return itemID; } 

  public Borrower getBorrowedBy()           { return borrowedBy; }
  public void     setBorrowedBy(Borrower b) { borrowedBy = b;    }
  public void     clearBorrowedBy()         { borrowedBy = null; }

  public String getTitle()  { return title;  }
  public String getAuthor() { return author; }

  public String toString() {
    if (borrowedBy != null)
      return String.format("%05d: %s by %s: \non loan to borrower %s",
          itemID, title, author, borrowedBy);
    else
      return String.format("%05d: %s by %s: not on loan", itemID, title, author);
  }

  /**
   * Test validity of Item data:
   *   title, author are strings of length at least 1
   *   ID is not negative.
   */
  public static boolean dataValid(
              int id, String ttl, String aut) {
    if (ttl.length() == 0) return false;
    if (aut.length() == 0) return false;
    if (id < 0) return false;
    return true;
  }

}
