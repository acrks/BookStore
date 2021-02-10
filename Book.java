/*
 * @author Team 4
 * CIS 36B - Book Store project
 */
import java.text.DecimalFormat;
import java.util.Random;

public class Book extends Items {
	
	private String author;
    private boolean isNew;
   
    public Book() {
       this("name unknown", "author unknown", "ISBN unknown", 0.0, false);
       
    }
   
    public Book(String name, String author, String itemID, double price, boolean isNew) {
      super(name, itemID, price);
      this.author = author;
      this.isNew = isNew;
      
    }
    
    public Book(String title, String author, String isbn) {
       this.setName(title);
       this.author = author;
    }
    
    public String getAuthor() {
        return author;
    }
   
    public String getTitle() {
        return getName();
    }
   
    public boolean isNew() {
		if(isNew == true) {
			return true;
		}
		return false;
	}
    
    public void changeCondition(String str) {
		if(str.equalsIgnoreCase("used")) {
			isNew = false;
		}
		else {
			isNew = true;
		}	
	}
    
    public void setAuthor(String author) {
    	this.author = author;
    }
	
    @Override public boolean equals(Object o) {
    	if (this == o)
    		return true;
    	else if (!(o instanceof Book ))
    		return false;
    	else {
    		Book b = (Book) o;
    		return (this.getName().equals(b.getTitle()) && this.author.equals(b.getAuthor()));
    	}
    }
    
    public int compareToISBN(Book b) {
		if (this.equals(b)) {
			return 0;
		}else if(!(getItemID().equals(b.getItemID()))) {
				return getItemID().compareTo(b.getItemID());
		} else if (!(getTitle().equals(b.getTitle()))) {
			return getTitle().compareTo(b.getTitle());	
		} else if (!(author.equals(b.author))) {
			return author.compareTo(b.author);
		}  else if(!(getPrice()!= b.getPrice())) {
			return Double.compare(getPrice(), b.getPrice());
		}
		else {
			return author.compareTo(b.author);
		}
	}
    
    public int compareToTitle(Book b) {
		if (this.equals(b)) {
			return 0;
		} else if (!(getTitle().equals(b.getTitle()))) {
			return getTitle().compareTo(b.getTitle());	
		} else if(!(getItemID().equals(b.getItemID()))) {
			return getItemID().compareTo(b.getItemID());
		} else if (!(author.equals(b.author))) {
			return author.compareTo(b.author);
		}  else if(!(getPrice()!= b.getPrice())) {
			return Double.compare(getPrice(), b.getPrice());
		}
		else {
			return author.compareTo(b.author);
		}
	}

    @Override public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        
        return "\nTitle: " + this.getName()
        		+ "\nAuthor: " + author
        		+ "\nPrice: $" + df.format(super.getPrice())
        		+ "\nISBN #: " + super.getItemID();
        
    }

	
   
}