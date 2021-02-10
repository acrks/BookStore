/*
 * Courses.java
 * @author Takumi Imanaka
 */
import java.util.ArrayList; 
public class Course{

	private String classCode;
	private String className;
	ArrayList<Book> reqBook;
	
	Course() {
		this("No class code", "No class name", new ArrayList<Book>());
	}
	
	Course(String classCode, String className) {
		this.classCode = classCode;
		this.className = className;
		this.reqBook = new ArrayList<Book>();
	}
	
	Course(String classCode, String className, ArrayList<Book> reqBook) {
		this.classCode = classCode;
		this.className = className;
		this.reqBook = reqBook;
	}
	
	public void setName(String name) {
		this.className = name;
	}
	
	public void setCode(String code) {
		this.classCode = code;
	}
	
	public String getName() {
		return className;
	}
	
	public String getCode() { 
		return classCode;
	}
	
	public ArrayList<Book> getReqItemList() {
		return this.reqBook;
	}
	@Override 
	public boolean equals(Object o) {
		if (this == o) 
			return true;
		else if ( !(o instanceof Course)) {
			return false;
		}
		else {
			Course c = (Course) o;
			return (this.classCode.equals(c.classCode) && this.className.equals(c.className));
		}
	}
	
	@Override 
	public String toString() {
		return classCode + className;
	}
}