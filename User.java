import java.util.ArrayList;
import java.text.DecimalFormat;



public class User implements Comparable<User>{
    String pattern = "###,###,###.00";
	DecimalFormat df = new DecimalFormat(pattern);
	private String name;
	private String email;
	private String password;
	private ArrayList<Course> userCourses;
	private boolean admin;
	private double creditApplied;
	private double userCredit;
	ArrayList<Book> purchased;
	
	
	public User() {
		this("Name unknown", "Email unknown", "Password unknown", null, false, 0.0);
	}
	
	public User(String theName, String theEmail, String password, ArrayList<Course> userCourses, boolean isAdmin, double theCredit) {
		this.name = theName;
		this.email = theEmail;
		this.password = password;
		this.userCourses = userCourses;
		this.admin = isAdmin;
		this.userCredit = theCredit;
		this.purchased = new ArrayList<Book>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
 	
	public boolean isAdmin() {
		return admin;
	}
	
	public double getUserCredit() {
		return userCredit;
	}
	
	public Course getUserCourses(int n) {
			return userCourses.get(n);
	}
	
	public ArrayList<Book> getPurchasedBooks() {
		return this.purchased;
	}
	
	public void addToPurchased(Book purchasedBook) {
		this.purchased.add(purchasedBook);
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUserCredit(double credit) {
		this.userCredit = credit;
	}
	
	public void addCredit(double credit) {
		userCredit +=credit;
	}
	
	public void setUserCourses(Course course) {
		this.userCourses.add(course);
	}
	
	public String getCoursesString() {
		String userCoursesStr = "";
		for(int i = 0; i < userCourses.size(); i++) {
			userCoursesStr += ((i + 1) + ". " + userCourses.get(i).toString() + "\n");
		}
		return userCoursesStr;
	}
	
	public int getCoursesNum() {
        return userCourses.size();
    }
	
	public double getCreditApplied() {
		return this.creditApplied;
	}
	public void incrementCreditApplied(double applied) {
		this.creditApplied += applied;
	}
	
	@Override public String toString() {
		String userCoursesStr = getCoursesString();
		String finalStr = "\nName: " + name 
				+ "\nEmail: " + email
				+ "\nCourse List:\n" + userCoursesStr
				+ "\nCredit: " + df.format(userCredit);
		
		if(admin == true) {
			String finalStrAlt = "\n***You have admin privileges for this platform***\n";
			finalStrAlt += finalStr;
			finalStr = finalStrAlt;
		}

		return finalStr;
	}
	
    @Override public boolean equals(Object o) {
    	if (o == this) {
			return true;
		}else if(!(o instanceof User)) {
			return false;
		}else {
			User c = (User) o;
			return name.equals(c.name) && email.equals(c.email) && admin == c.admin 
					&& userCourses.equals(c.userCourses) && userCredit == c.userCredit;
		}
	}
    
   
    /**
     * Compares this to another Car
     * according to 1. make, 2. model,
     * 3. mileage, 4. isUsed
     * @param c another Car
     * @return an integer comparison of the
     * two Cars
     */
    @Override public int compareTo(User c) {
    	if (this.equals(c)) {
      	   return 0;
         }else if(!(name.equals(c.name))) {
         	return name.compareTo(c.name);
         }else if(!(email.equals(c.email))) {
         	return email.compareTo(c.email);
         }else if(!(admin == c.admin)) {
        	 return Boolean.compare(admin, c.admin);
         }
    	//Took classes out until further notice
         else {
        	 return Double.compare(userCredit, c.userCredit);
         }
    }
	
}