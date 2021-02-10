import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Random;

public final class Bookstore extends User implements mainInterface {
	private static ArrayList<Book> books = new ArrayList<Book>();
	private static ArrayList<Book> marketBooks = new ArrayList<Book>();
	private static ArrayList<Department> deptList = new ArrayList<Department>();
	private static ArrayList<User> user = new ArrayList<User>();
	private ArrayList<masterInventory> marketInventory = new ArrayList<masterInventory>();
	private static final String booksfile = "books.txt";
	private static final String usersfile = "users.txt";
	private static final String coursesname = "courses.txt";
	String pattern = "###,###,##0.00";
	DecimalFormat df = new DecimalFormat(pattern);

	public static void main(String[] args) throws IOException {
		// read in books --> array list of book and array list of items (store array
		// list)
		// read in course --> array list of course
		Bookstore M = new Bookstore();
		// reading courses in
		File courseIn = new File(coursesname);
		Scanner read = new Scanner(courseIn);
        try {
            read = new Scanner(new File("dummyFileName.txt"));
        } catch (FileNotFoundException fnf) {
            System.out.println(fnf + "\nFileNotFoundException encountered, now using proper file name...");
        }

        read = new Scanner(courseIn);
		M.populateCoursesDatabase(read);

		// reading books&items in
		File booksIn = new File(booksfile);
		read = new Scanner(booksIn);
		M.populateInventory(read);

		// read in users --> array list of users
		File file = new File(usersfile);
		read = new Scanner(file);
		
		masterInventory inventory[] = new masterInventory[books.size()];
		for (int i = 0; i < books.size(); i++) {
			int upperbound = 25;

			Random rand = new Random();

			int int_random = rand.nextInt(upperbound);

			while (int_random == 0) {
				int_random = rand.nextInt(upperbound);
			}

			inventory[i] = new masterInventory(books.get(i), int_random);

		}

		for (int i = 0; i < marketBooks.size(); i++) {
			M.marketInventory.add(new masterInventory(marketBooks.get(i), 1));
		}

		M.bubbleSortTitle(inventory);

		M.populateUserDatabase(read);

		for (int i = 0; i < inventory.length; i++) {
			inventory[i].toStringBuilder();
		}

		System.out.print("\nWelcome to the G4 Bookstore!\nWe currently have " + inventory.length + " titles in stock\n"
				+ "What would you like to do?" + "\nLog In (Y)\n" + "Exit (E)\n" + "Your choice: ");
		Scanner userInput = new Scanner(System.in);
		String userOption = userInput.nextLine();
		while (!(userOption.equalsIgnoreCase("e"))) {

			if (userOption.equalsIgnoreCase("y")) {

				while (!(userOption.equalsIgnoreCase("e"))) {
					int userProfileIndex = M.login();
					User thisUser = user.get(userProfileIndex);
					System.out.println("Welcome " + thisUser.getName() + "!" + "\n\nHere is your information:"
							+ thisUser.toString() + "\nHow can we help you today?\n");
					// Options
					M.displayOptions();
					userOption = userInput.nextLine();
					while (!(userOption.equalsIgnoreCase("d"))) {
						if (userOption.equalsIgnoreCase("a")) {
							// Buy books
							M.Buy(userInput, thisUser, inventory);

						} else if (userOption.equalsIgnoreCase("b")) {
							M.Sell(userInput, thisUser, inventory);
							// Marketplace

						} else if (userOption.equalsIgnoreCase("c")) {
							M.addCredit(userInput, thisUser);
						}
						M.displayOptions();
						userOption = userInput.nextLine();
					}
					if(thisUser.getPurchasedBooks().size() > 0) { 
						try {
							M.createReceipt(thisUser);
						} catch (IOException e) {
							System.out.println("Error: Writing failed");
						}
					}
					
					System.out.print("Welcome to the G4 Bookstore! What would you like to do?" + "\nLog In (Y)\n"
							+ "Exit (E)\n" + "Your choice: ");
					userOption = userInput.nextLine();
				}

			} else {
				System.out.println("\nInvalid Entry! Please try it again!\n");
				System.out.print("Welcome to the G4 Bookstore! What would you like to do?" + "\nLog In (Y)\n"
						+ "Exit (E)\n" + "Your choice: ");
				userOption = userInput.nextLine();
			}
		} // end of While Login Loop
		System.out.println("Thank you for stopping by! We hope to see you again soon!");
		// userInput.close();
		// create a market array list of book and items users sell
	}

	/*
	 * Needs editing Finds target department in department ArrayList
	 * 
	 * @param ArrayList of Dept, String of target department name
	 * 
	 */
	public void populateCoursesDatabase(Scanner read) {
		int numDept = read.nextInt();

		for (int i = 0; i < numDept; i++) {
			String deptName, classCode, className;
			int numCourse;
			deptName = read.next();
			Department newDept = new Department(deptName);

			numCourse = read.nextInt();
			for (int j = 0; j < numCourse; j++) {
				classCode = read.next();
				className = read.nextLine();
				Course newCourse = new Course(classCode, className);
				newDept.addCourse(newCourse);
			}
			deptList.add(newDept);
		} // end outer for loop

		read.close();
	}

	/*
	 * Finds target department in department ArrayList
	 * 
	 * @param ArrayList of Dept, String of target department name
	 * 
	 * @return index of target dept in ArrayList
	 */
	public static int findDeptIndex(ArrayList<Department> deptList, String target) {
		for (int j = 0; j < deptList.size(); j++) {
			if (deptList.get(j).getAbbreviation().equals(target)) {
				return j;
			}
		}
		return -1;
	}

	/*
	 * Needs editing Finds target department in department ArrayList
	 * 
	 * @param ArrayList of Dept, String of target department name
	 * 
	 */
	public void populateInventory(Scanner read) {
		String book = "Book", code, dept, itemType;
		String title, author, id;
		double cost;
		int num;
		int deptIndex = 0;
		while (read.hasNextLine()) {
			itemType = read.next();
			if (itemType.equals(book)) {

				read.nextLine();

				title = read.nextLine();

				cost = read.nextDouble();

				read.nextLine();

				author = read.nextLine();

				id = read.next();

				Book newBook = new Book(title, author, id, cost, true);

				books.add(newBook);

				num = read.nextInt();

				dept = read.next();

				// find department in existing department ArrayList
				deptIndex = findDeptIndex(deptList, dept);

				for (int i = 0; i < num; i++) { // for every class code listed in text file
					code = read.next();
					// compare class code in txt file to class code in department's course list
					for (int j = 0; j < deptList.get(deptIndex).getList().size(); j++) {
						Course thisCourse = deptList.get(deptIndex).getList().get(j);
						if (thisCourse.getCode().equals(code)) {
							// add book to that classes ArrayList of required items
							thisCourse.getReqItemList().add(newBook);

						}
					}
				} // end for loop to add to list of required items for each class

			} // end of book read in

		}
		read.close();// end while loop for reading in items
	}

	/*
	 * Needs editing Finds target department in department ArrayList
	 * 
	 * @param ArrayList of Dept, String of target department name
	 * 
	 */
	public void populateUserDatabase(Scanner input) {
		while (input.hasNextLine()) {
			boolean admin = false;
			String name = input.nextLine();

			String email = input.nextLine();

			String password = input.nextLine();

			String coursesStr = input.nextLine();

			String coursesArray[] = coursesStr.split(", ");

			String isAdmin = input.nextLine();

			if (isAdmin.equals("Yes")) {
				admin = true;
			}

			double userCredit = Double.parseDouble(input.nextLine());
			if (input.hasNextLine()) {
				input.nextLine();
			}

			input.skip("[ \n]*");

			ArrayList<Course> userCourses = new ArrayList<Course>();

			for (int i = 0; i < coursesArray.length; i++) {

				String array[] = coursesArray[i].split(" ");

				int deptIndex = findDeptIndex(deptList, array[0]);

				Department list = deptList.get(deptIndex);

				int index = findCourse(array[1], list);

				Course classes = list.getList().get(index);

				userCourses.add(classes);
			}

			User profile = new User(name, email, password, userCourses, admin, userCredit);

			user.add(profile);

			if (!(input.hasNext())) {
				break;
			}
		}
		// input.close();
	}

	/*
	 * Needs editing Finds target department in department ArrayList
	 * 
	 * @param ArrayList of Dept, String of target department name
	 * 
	 * @return index of target dept in ArrayList
	 */
	public int login() {
		Scanner input = new Scanner(System.in);
		System.out.print("\nEnter your email address: ");
		String userEmail = input.nextLine();
		int userIndex = linearSearch(userEmail);
		int emailAttempts = 0;
		while (userIndex == -1) {
			if (emailAttempts < 3) {
				emailAttempts++;
				System.out.println("We could not find that email address in our system. You have " + (3 - emailAttempts)
						+ " attempts left.");
			} else {
				System.out.println(
						"We could not find that email in the database. Please contact the system administrator to request an account.");
				System.exit(0);
			}
			System.out.print("Please enter your email address again: ");
			userEmail = input.nextLine();
			userIndex = linearSearch(userEmail);
		}
		int loginAttempts = 0;
		System.out.print("Enter your password: ");
		String userPassword = input.nextLine();
		boolean userProfileIndex = linearSearch(userIndex, userPassword);
		while (userProfileIndex == false) {
			System.out.println("We could not find your information on file!");
			//
			if (loginAttempts == 5) {
				System.out.println(
						"\nYou have surpassed the number of attempts to login with this password.\nPlease contact the website administrator to request a new password.");
				System.exit(0);
			} else {
				System.out.println("Incorrect Password! Please enter your password again");

			}
			System.out.print("Enter your password: ");
			userPassword = input.nextLine();
			userProfileIndex = linearSearch(userIndex, userPassword);
			loginAttempts++;
		}
		// input.close();
		return userIndex;

	}

	public void displayOptions() {
		System.out.println("A: Purchase materials" + "\nB: Sell Book" + "\nC: Add Credit" + "\nD: Log Out");
		System.out.print("\nEnter your choice: ");
	}

	public int findCourse(String courseCode, Department dept) {
		for (int j = 0; j < dept.getList().size(); j++) {
			Course thisCourse = dept.getList().get(j);
			if (thisCourse.getCode().equalsIgnoreCase(courseCode)) {
				return j;
			}
		}
		return -1;
	}

	public static void remove(masterInventory array[], int numElements, int indexToRemove) {
		for (int i = indexToRemove; i < numElements - 1; i++) {
			array[i] = array[i + 1];
		}
		return;
	}

	public void sortUser() {
		for (int i = 0; i < user.size() - 1; i++) {
			for (int j = 0; j < user.size() - i - 1; j++) {
				if (user.get(j).compareTo(user.get(j + 1)) > 0) {
					User temp = user.get(j);
					user.set(j, user.get(j + 1));
					user.set(j + 1, temp);

				}
			}
		}
	}

	public int linearSearch(String email) {
		for (int i = 0; i < user.size(); i++) {
			User testUser = user.get(i);
			if (testUser.getEmail().equalsIgnoreCase(email)) {
				return i;
			}
		}
		return -1;
	}

	public boolean linearSearch(int n, String password) {
		User testUser = user.get(n);
		if (testUser.getPassword().equalsIgnoreCase(password)) {
			return true;
		}
		return false;
	}

	/**
	 * Bubble sorts the books arraylist
	 * 
	 */

	public void bubbleSortISBN(masterInventory[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = 0; j < array.length - i - 1; j++) {
				if (array[j].getBooks().compareToISBN(array[j + 1].getBooks()) > 0) {
					masterInventory temp = array[j + 1];
					array[j + 1] = array[j];
					array[j] = temp;
				}
			}
		}
	}

	public void bubbleSortTitle(masterInventory[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = 0; j < array.length - i - 1; j++) {
				if (array[j].getBooks().compareToTitle(array[j + 1].getBooks()) > 0) {
					masterInventory temp = array[j + 1];
					array[j + 1] = array[j];
					array[j] = temp;
				}
			}
		}
	}

	/**
	 * Prints the catalogue of books from the arraylist
	 * 
	 */
	public void printStock() {
		for (int i = 0; i < books.size(); i++) {
			System.out.println(books.get(i) + "\n");
		}
	}

	public int searchforBookMI(String neededBook, ArrayList<masterInventory> array) {
		int low = 0;
		int high = array.size() - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			int res = array.get(mid).getBooks().getItemID().compareTo(neededBook);
			if (res == 0) {
				return mid;
			} else if (res > 0) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	public int searchforBookInventory(String neededBook, masterInventory[] array) {
		int low = 0;
		int high = array.length - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			int res = array[mid].getBooks().getItemID().compareTo(neededBook);
			if (res == 0) {
				return mid;
			} else if (res > 0) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	public int searchforBook(Book neededBook, masterInventory[] array) {
		int low = 0;
		int high = array.length - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			int res = array[mid].getBooks().compareToISBN(neededBook);
			if (res == 0) {
				return mid;
			} else if (res > 0) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	public int searchforBook(String neededBook, masterInventory[] array) {
		int low = 0;
		int high = array.length - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			int res = array[mid].getBooks().getItemID().compareTo(neededBook);
			if (res == 0) {
				return mid;
			} else if (res > 0) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	public int searchforBook(String neededBook, ArrayList<Book> arrayList) {
		int low = 0;
		int high = arrayList.size() - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			int res = arrayList.get(mid).getItemID().compareTo(neededBook);
			if (res == 0) {
				return mid;
			} else if (res > 0) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	public void bubbleSortISBN() {
		for (int i = 0; i < books.size() - 1; i++) {
			for (int j = 0; j < books.size() - i - 1; j++) {
				if (books.get(j).compareToISBN(books.get(j + 1)) > 0) {
					Book temp = books.get(j);
					books.set(j, books.get(j + 1));
					books.set(j + 1, temp);
				}
			}
		}
	}

	public void bubbleSortISBNMarket() {
		for (int i = 0; i < marketBooks.size() - 1; i++) {
			for (int j = 0; j < marketBooks.size() - i - 1; j++) {
				if (marketBooks.get(j).compareToISBN(marketBooks.get(j + 1)) > 0) {
					Book temp = books.get(j);
					marketBooks.set(j, marketBooks.get(j + 1));
					marketBooks.set(j + 1, temp);
				}
			}
		}
	}

	public int searchInventory(Book neededBook, masterInventory[] array) {
		int low = 0;
		int high = array.length - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			int res = array[mid].getBooks().compareToISBN(neededBook);
			if (res == 0) {
				return mid;
			} else if (res > 0) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	public int searchInventory(Book neededBook, ArrayList<masterInventory> array) {
		int low = 0;
		int high = array.size() - 1;
		int mid;
		while (low <= high) {
			mid = (low + high) / 2;
			int res = array.get(mid).getBooks().compareToTitle(neededBook);
			if (res == 0) {
				return mid;
			} else if (res > 0) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return -1;
	}

	public void Buy(Scanner userInput, User thisUser, masterInventory[] array) 
	{
		System.out.println("Reminder: All used copies of our books are 75% of the price in new condition"
				+ "\nWhat would you like to search by?\n" + "\nD: Course " + "\nE: Book info");
		System.out.print("\nEnter your choice: ");
		String userOption = userInput.nextLine();
		if (userOption.equalsIgnoreCase("D")) 
		{
			System.out.print("\nWhich course would you like to purchase books for?\n" + thisUser.getCoursesString()
					+ "\nYour choice: ");
			String tempChoice = userInput.nextLine();
            while (Integer.parseInt(tempChoice)>thisUser.getCoursesNum() || Integer.parseInt(tempChoice) < 0) {
                System.out.println("Invalid Entry. Please try it again!");
                System.out.print("\nWhich course would you like to purchase books for?\n" + thisUser.getCoursesString()
                + "\nYour choice: ");
                tempChoice = userInput.nextLine();
            }
			int c = Integer.parseInt(tempChoice) - 1;
			ArrayList<Book> reqItems = thisUser.getUserCourses(c).getReqItemList();
			masterInventory userItems[] = new masterInventory[reqItems.size()];
			bubbleSortISBN(array);
			for (int i = 0; i < reqItems.size(); i++) 
			{
				int s = searchInventory(reqItems.get(i), array);
				userItems[i] = array[s];
			}
			if (reqItems.size() == 0) 
			{
				System.out.print("There are no required materials for this course");
			} 
			else if (reqItems.size() == 1) 
			{
				System.out.print("\nYou wish to purchase: " + reqItems.get(0).getName() + "\n[Y/N]:");
				userOption = userInput.nextLine();
				if (userOption.equalsIgnoreCase("Y")) 
				{
					MakePurchase(userInput, thisUser, reqItems.get(0), array);
					thisUser.addToPurchased(userItems[0].getBooks());
				} 
				else 
				{	
					System.out.println("Thank you for your interest!");
				}
			} 
			else 
			{
				bubbleSortTitle(userItems);
				for (int i = 0; i < userItems.length; i++) {
					System.out.println(userItems[i].toStringBuilder());
				}
				double totalPrice = 0;
				for (int j = 0; j < reqItems.size(); j++) {
					totalPrice += reqItems.get(j).getPrice();
				}
				System.out.print("\n\nPurchase all items for $" + df.format(totalPrice)
						+ " or place an order for an individual item?:" + "\nA: Purchase all"
						+ "\nB: Purchase individual item" + "\n\nOption: ");
				userOption = userInput.nextLine();
				if (userOption.equalsIgnoreCase("A")) {
					System.out.print(
							"You have chosen to purchase all the required items for your materials.\nIs this correct?\n[Y/N]: ");
					userOption = userInput.nextLine();
					if (userOption.equalsIgnoreCase("Y")) {
						MakePurchaseAll(userInput, thisUser, totalPrice, userItems, array);
					}
				} else if (userOption.equalsIgnoreCase("B")) {
					bubbleSortISBN(array);
					System.out.print("Enter the ISBN of your desired book: ");
					userOption = userInput.nextLine();
					int k = searchforBookInventory(userOption, array);
					int bookLocation = searchInventory(array[k].getBooks(), array);
					Book neededItem = array[bookLocation].getBooks();
					System.out.print("\nYou wish to purchase: " + neededItem.toString() + "\n[Y/N]\nOption:");
					userOption = userInput.nextLine();
					if (userOption.equalsIgnoreCase("Y")) {
						// int m = searchInventory(neededItem, array);
						MakePurchase(userInput, thisUser, neededItem, array);
					}
				}
			}
		} 
		else if (userOption.equalsIgnoreCase("E")) 
		{
			bubbleSortTitle(array);
			System.out.println("Here is a full list of the books you can choose from: ");
			for (int i = 0; i < array.length; i++) 
			{
				System.out.println(array[i].toStringBuilder());
			}
			bubbleSortISBN(array);
			System.out.print("Enter the ISBN of your desired book: ");
			userOption = userInput.nextLine();
			int bookLocation = searchforBookInventory(userOption, array);
			//int bookLocation = searchInventory(array[k].getBooks(), array);
			if (bookLocation == -1) 
			{
				System.out.println(
						"Sorry, we don't carry that book at this moment!");
			} 
			else if (array[bookLocation].getTotalCopies() == 0) 
			{
				System.out.println(
						"This item is completely sold out! We apologize for the inconvenience\nPlease check the marketplace to see if there are any copies available there.");
				 
				
					Book desiredBook = array[bookLocation].getBooks();
					System.out.print("\nYou wish to purchase: \n" + desiredBook.toString() + "\n[Y/N]\nOption: ");
					userOption = userInput.nextLine();
					if (userOption.equalsIgnoreCase("Y")) 
					{
						MakePurchase(userInput, thisUser, desiredBook, array);
					} 
					else 
					{
						System.out.println("Thank you for visiting the bookstore!");
					}
				
			} 
			else 
			{
				bubbleSortISBN(array);
				int k = searchforBookInventory(userOption, array);
				bookLocation = searchInventory(array[k].getBooks(), array);
				Book neededItem = array[bookLocation].getBooks();
				System.out.print("You wish to purchase: " + neededItem.toString() + "\n[Y/N]\nOption:");
				userOption = userInput.nextLine();
				if (userOption.equalsIgnoreCase("Y")) 
				{
					// int m = searchInventory(neededItem, array);
					MakePurchase(userInput, thisUser, neededItem, array);
				}
			}
		}
//			try {
//				createReceipt(thisUser);
//			} catch (IOException e) {
//				System.out.println("Error: Writing failed");
//			}
		// search book
	}

	public void MakePurchase(Scanner userInput, User thisUser, Book neededItem, masterInventory[] inventory) {
		boolean used = false;
		double price = neededItem.getPrice();
		int entry = searchInventory(neededItem, inventory);
		masterInventory catalg = inventory[entry];
		if (catalg.getTotalCopies() == 0) {
			System.out.println("We are very sorry! All our copies of " + neededItem.getName()
					+ " are completely sold out. Would you like to look on the Marketplace?");
		} else {
			System.out
					.print("Would you like to purchase new or used copies of the material?\nNEW: N\nUSED: U\n[N/U]: ");
			String userOption = userInput.nextLine();
			if (userOption.equalsIgnoreCase("U")) {
				if (catalg.getUsedCopies() == 0) {
					System.out.println(
							"Sorry, this book in used condition is sold out.\nPlease repeat the steps to purchase a copy of the book in new condition");
				} else {
					price = neededItem.getUsedPrice();
					used = true;
					System.out.print("Would you like to apply your credit to the purchase" + "?\n[Y/N]\nOption:  ");
					userOption = userInput.nextLine();
					if (userOption.equalsIgnoreCase("Y")) {
						ApplyCredit(userInput, thisUser, price);
					} 
					catalg.updateUsedCopies(-1);
					thisUser.addToPurchased(catalg.getBooks());
					
				}
			} else {
				if (catalg.getNewCopies() == 0) {
					System.out.println(
							"Sorry, this book in new condition is sold out. Please repeat the steps to purchase a copy of the book in used condition");
				}
				else {
					System.out.print("Would you like to apply your credit to the purchase" + "?\n[Y/N]\nOption:  ");
					userOption = userInput.nextLine();
					if (userOption.equalsIgnoreCase("Y")) {
						ApplyCredit(userInput, thisUser, price);
					}
					catalg.updateNewCopies(-1);
					thisUser.addToPurchased(catalg.getBooks());
				}
			}
		}
		System.out.println("Your order for " + neededItem.getName() + " for $" + df.format(price) + " was placed");
	}

	public void MakePurchaseAll(Scanner userInput, User thisUser, double price, masterInventory[] userItems,
			masterInventory[] array) {
		System.out.print(
				"\nWould you like to purchase new or used copies of all the books?\nNew: N\nUsed: U\nChoose Individually: C\n[N/U/C]: ");
		String userOption = userInput.nextLine();
		double totalPrice = price;
		if (userOption.equalsIgnoreCase("u")) {
			totalPrice *= 0.75;
			for (int m = 0; m < userItems.length; m++) {
				userItems[m].getBooks().changeCondition("used");
				int s = searchInventory(userItems[m].getBooks(), array);
				if (s > 0) {
					if (array[s].getUsedCopies() == 0) {
						System.out.print("We do not have anymore used copies of " + array[s].getBooks().getName()
								+ ".\nWould you prefer to purchase a new copy instead? [Y/N]");
						userOption = userInput.nextLine();
						if (userOption.equalsIgnoreCase("y")) {
							array[s].getBooks().changeCondition("new");
							totalPrice -= array[s].getBooks().getUsedPrice();
							totalPrice += array[s].getBooks().getPrice();

						} else {
							remove(userItems, userItems.length, m);
							totalPrice -= array[s].getBooks().getUsedPrice();

						}
						System.out.println("Your total is now " + df.format(price));
					}
				}
			}
		} else if (userOption.equalsIgnoreCase("n")) {
			for (int m = 0; m < userItems.length; m++) {
				bubbleSortISBN(array);
				userItems[m].getBooks().changeCondition("new");
				int s = searchInventory(userItems[m].getBooks(), array);
				if (s > 0) {
					if (array[s].getNewCopies() == 0) {
						System.out.print("We do not have anymore new copies of " + array[s].getBooks().getName()
								+ ".\nWould you prefer to purchase a used copy instead? [Y/N]");
						userOption = userInput.nextLine();
						if (userOption.equalsIgnoreCase("y")) {
							userItems[m].getBooks().changeCondition("used");
							totalPrice -= array[s].getBooks().getPrice();
							totalPrice += array[s].getBooks().getUsedPrice();

						} else {
							remove(userItems, userItems.length, m);
							totalPrice -= array[s].getBooks().getPrice();

						}
						System.out.println("Your total is now " + df.format(price));
					}
				}
			}
		} else if (userOption.equalsIgnoreCase("c")) {
			totalPrice = 0;
			for (int m = 0; m < userItems.length; m++) {
				System.out.print("Would you like to purchase " + userItems[m].getBooks().getName()
						+ " used or new?\n[New: N/Used: U]: ");
				userOption = userInput.nextLine();
				if (userOption.equalsIgnoreCase("N")) {
					userItems[m].getBooks().changeCondition("new");
					totalPrice += userItems[m].getBooks().getPrice();
					int s = searchInventory(userItems[m].getBooks(), array);
					if (s > 0) {
						if (array[s].getNewCopies() == 0) {
							System.out.print("We do not have anymore new copies of " + array[s].getBooks().getName()
									+ ".\nWould you prefer to purchase a used copy instead? [Y/N]");
							userOption = userInput.nextLine();
							if (userOption.equalsIgnoreCase("y")) {
								userItems[m].getBooks().changeCondition("used");
								totalPrice -= array[s].getBooks().getPrice();
								totalPrice += array[s].getBooks().getUsedPrice();
							} else {
								remove(userItems, userItems.length, m);
							}
							System.out.println("Your total is now " + df.format(totalPrice));
						}
					}
				} else if (userOption.equalsIgnoreCase("U")) {
					userItems[m].getBooks().changeCondition("used");
					totalPrice += userItems[m].getBooks().getUsedPrice();
					int s = searchInventory(userItems[m].getBooks(), array);
					if (array[s].getUsedCopies() == 0) {
						System.out.print("We do not have anymore used copies of " + array[s].getBooks().getName()
								+ ".\nWould you prefer to purchase a new copy instead? [Y/N]");
						userOption = userInput.nextLine();
						if (userOption.equalsIgnoreCase("y")) {
							array[s].getBooks().changeCondition("new");
							totalPrice -= array[s].getBooks().getUsedPrice();
							totalPrice += array[s].getBooks().getPrice();
						} else {
							remove(userItems, userItems.length, m);
							totalPrice -= array[s].getBooks().getUsedPrice();
						}
					}
					System.out.println("Your total is now " + df.format(totalPrice));
				}
			}
		} // end of option
		System.out.print("\nWould you like to apply your credit to the purchase" + "?\n[Y/N]\nOption: ");
		userOption = userInput.nextLine();
		if (userOption.equalsIgnoreCase("Y")) {
			ApplyCredit(userInput, thisUser, price);
		} else {
			System.out.println("Thank you! Your purchase of $" + df.format(totalPrice) + " was placed");
			for (int i = 0; i < userItems.length; i++) {
				int s = searchInventory(userItems[i].getBooks(), array);
				if (userItems[i].getBooks().isNew() == true) {
					array[s].updateNewCopies(-1);
					thisUser.addToPurchased(array[s].getBooks());
				} else {
					array[s].updateUsedCopies(-1);
					thisUser.addToPurchased(array[s].getBooks());
				}
			}
		} //
	}// proper end

	public void ApplyCredit(Scanner userInput, User thisUser, double price) {
		if (thisUser.getUserCredit() == 0.00) {
			System.out.println(
					"You have no credit remaining for your account. Please visit the main menu to add more credit to your account.");
		} else {
			System.out.print("\nHow much of your " + df.format(thisUser.getUserCredit())
					+ " credit balance would you like to put towards the purchase? ");
			double userCredit = Double.parseDouble(userInput.nextLine());
			while (userCredit > thisUser.getUserCredit()) {
				System.out.print("\nThe amount you entered exceeds your user credit balance of: "
						+ df.format(thisUser.getUserCredit())
						+ "\n\nWould you like to apply more credit to your account or enter a smaller amount?\n\nApply More Credit: A\nEnter Smaller Amount: B\n\nOption: ");
				String userOption = userInput.nextLine();
				if (userOption.equalsIgnoreCase("A")) {
					addCredit(userInput, thisUser);
					userCredit = thisUser.getUserCredit();
				} else {
					System.out.print("Enter the amount you would like to apply to the order: ");
					userCredit = Double.parseDouble(userInput.nextLine());
				}
			}

			// print total cost after credit is applied
			if (price >= userCredit) {
				System.out.println("$" + df.format(userCredit)
						+ " will be deducted from the purchase. The new total for this order is $"
						+ df.format(price - userCredit) + "\n");
			} else {
				System.out.println(
						"\n" + userCredit + " will be deducted from the purchase. The new total for this order is $"
								+ df.format(0) + "\n\n");
			}
			thisUser.incrementCreditApplied(userCredit);
			if (userCredit > price) {
				thisUser.setUserCredit(thisUser.getUserCredit() - price);
				price = 0.0;
			} else if (userCredit < price) {
				price -= userCredit;
				thisUser.setUserCredit(thisUser.getUserCredit() - userCredit);
			} else {
				price = 0.0;
				thisUser.setUserCredit(0);
			}

		}
	}

	public void Sell(Scanner userInput, User thisUser, masterInventory[] inventory) {
		System.out.println("We have these books on demand. Please take a look at the list below\n");
		bubbleSortISBN();
		bubbleSortISBNMarket();
		//bubbleSortTitle(inventory);
		printStock();
		bubbleSortISBN(inventory);
		System.out.print("Please enter the ISBN of the book you want to sell: ");
		String desiredISBN = userInput.next();
		userInput.nextLine();
		int bookLocation = searchforBook(desiredISBN, books);
		double sellPrice = 0.0;
		if (bookLocation == -1) {
			System.out.print("Sorry, our bookstore does not have a demand on this book at this moment!");
			System.out.println("Would you like to sell it on the market place? (y/n): ");
			String tempChoice = userInput.next();
			if (tempChoice.equalsIgnoreCase("y")) {
				System.out.println("Welcome to G4 Bookstore market place!\n");
				bookLocation = searchforBook(desiredISBN, marketBooks);
				if (bookLocation == -1) {
					System.out.println("Please enter the book information you want to sell");
					String id = desiredISBN;
					System.out.print("\nTitle: ");
					userInput.nextLine();
					String name = userInput.nextLine();
					System.out.print("Author: ");
					String author = userInput.nextLine();
					System.out.print("Please enter the price you paid: $");
					double price = Double.parseDouble(userInput.nextLine());
					sellPrice = price * 0.25;
					Book newAddBook = new Book(name, author, id, price, false);
					marketBooks.add(newAddBook);
					masterInventory newInventory = new masterInventory(newAddBook, 1);
					newInventory.resetStock();
					marketInventory.add(newInventory);
				} else {
					sellPrice = marketBooks.get(bookLocation).getPrice() * 0.25;
					int inventoryLocation = searchInventory(marketBooks.get(bookLocation), marketInventory);
					marketInventory.get(inventoryLocation).updateUsedCopies(1);
				}

			}
		} else {
			sellPrice = books.get(bookLocation).getPrice() * 0.5;
			int inventoryLocation = searchInventory(books.get(bookLocation), inventory);
			inventory[inventoryLocation].updateUsedCopies(1);
		}
		System.out.print("\nThank you! We will purchase the book for $" + df.format(sellPrice) + "\n");
		thisUser.addCredit(sellPrice);
		System.out.println("You credit has been added. Now you have $" + df.format(thisUser.getUserCredit())
				+ " credit in your account.");
	}

	public void addCredit(Scanner userInput, User thisUser) {
		System.out.print("\nHow much credit would you like to apply to your user credit balance today? $");
		double userDouble = Double.parseDouble(userInput.nextLine());
		System.out.print(
				"\nPer California state law, we are required to inform you that any amount you pay towards your student credit balance is non-refundable and will remain in the hands of the G4 Bookstore upon your graduation"
						+ "\n\nContinue?: ");
		String userOption = userInput.nextLine();
		if (userOption.equalsIgnoreCase("Y")) {
			thisUser.setUserCredit(thisUser.getUserCredit() + userDouble);
			System.out.println("Thank you for your business! Your credit balance is now $"
					+ df.format(thisUser.getUserCredit()) + "\n");
		} else {
			System.out.println("We respect your decision.");
		}
	}

	public void createReceipt(User thisUser) throws IOException {
		String filename = thisUser.getName() + " Receipt.txt";
		File outFile = new File(filename);
		PrintWriter writer = new PrintWriter(outFile);
		double total = 0.0;

		writer.println("~*~*~* G4 Bookstore *~*~*~\n");
		if(thisUser.getPurchasedBooks().size() == 1) {
			writer.println("You purchased " + thisUser.getPurchasedBooks().size() + " book today");
		}
		else {
			writer.println("You purchased " + thisUser.getPurchasedBooks().size() + " books today");
		}
		for (int i = 0; i < thisUser.getPurchasedBooks().size(); i++) {
			if (thisUser.getPurchasedBooks().get(i).isNew() == true) {
				total += thisUser.getPurchasedBooks().get(i).getPrice();
				writer.println("$" + df.format(thisUser.getPurchasedBooks().get(i).getPrice()) + "\t"
						+ "\t" + thisUser.getPurchasedBooks().get(i).getTitle());
			} else {
				total += thisUser.getPurchasedBooks().get(i).getUsedPrice();
				writer.println("$" + df.format(thisUser.getPurchasedBooks().get(i).getPrice()) + "\t"
						+ thisUser.getPurchasedBooks().get(i).getTitle());
			}
		}
		writer.println("Sub-total: $" + df.format(total) + "\n");
		writer.println("Credits Applied: $" + df.format(thisUser.getCreditApplied()));
		writer.println("Total: $" + df.format((total - thisUser.getCreditApplied())) + "\n");
		writer.println("Your remaining credits: $" + df.format(thisUser.getUserCredit()));
		writer.println("Thank you for you purchase, " + thisUser.getName() + "!");
		writer.println("Happy Studying!");
		writer.close();
	}

}// end

// end of class