import java.util.Random;

public class masterInventory {
		private Book books;
	    private int totalCopies = 0;
		private int newCopies = 0;
	    private int usedCopies = 0;
	   
	    public masterInventory() {
	       this(null, 0);
	    }
	    
	    public masterInventory(Book thisBook, int numCopies) {
	    	this.books = thisBook;
	    	totalCopies = numCopies;
	    	int upperbound = numCopies;
	        Random rand = new Random();
	        int int_random = rand.nextInt(upperbound);
	        newCopies = numCopies - int_random;
	        usedCopies = numCopies - newCopies;
	    }
	    
	    public void updateNumItems(int n) {
	    	totalCopies += n;
	    }
	    
	    public void updateUsedCopies(int n) {
	    	this.usedCopies+= n;
	    	totalCopies += n;
	    }
	    
	    public void updateNewCopies(int n) {
	    	this.newCopies += n;
	    	updateNumItems(n);
	    }
	    
	    public int getTotalCopies() {
	    	return totalCopies;
	    }
	    
	    public int getUsedCopies() {
	    	return usedCopies;
	    }
	    
	    public int getNewCopies() {
	    	return newCopies;
	    }
	    
	    public void stockInventory(Book thisBook, int numCopies) {
	    	this.books = thisBook;
	    	this.totalCopies = numCopies;
	    }
	    
	    public Book getBooks() {
	    	return books;
	    }
	    
	    public void resetStock() {
	    	usedCopies = 1;
	    	newCopies = 0;
	    }
 
	    public StringBuilder toStringBuilder() {
	    	StringBuilder sb = new StringBuilder();
	        
	        String nCopies = Integer.toString(newCopies), uCopies = Integer.toString(usedCopies);
	       
	        if(newCopies <= 0) {
	        	nCopies = "SOLD OUT"; 
	        }
	        if(usedCopies <= 0) {
	        	uCopies = "SOLD OUT";
	        }
	        
	        sb.append(books.toString() + "\nTotal Copies: " + totalCopies + "\nNew Copies: " + nCopies
	        		+ "\nUsed Copies: " + uCopies);
	        
	        if(totalCopies < 5) {
	        	sb.append("\nBUY QUICK! This book is almost completely out of stock!");
	        }
	        
	        return sb;
	    }
}