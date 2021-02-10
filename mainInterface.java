import java.util.Scanner;

public interface mainInterface {
	public int searchforBook(Book neededBook, masterInventory[] array);
	public void Buy(Scanner userInput, User thisUser, masterInventory[] array);
	public int login();
	public void bubbleSortISBN(masterInventory[] array);
	public void bubbleSortTitle(masterInventory[] array);
	public void populateCoursesDatabase(Scanner read);
	public void populateInventory(Scanner read);
	public void populateUserDatabase(Scanner input);
	public void displayOptions();
	public int linearSearch(String email);
	public void printStock();
}
