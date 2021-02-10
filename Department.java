import java.util.ArrayList;
public class Department {
	
	private String abbreviation;
	private ArrayList<Course> listOfCourses;
	
	
	Department(String abbreviation) {
		this(abbreviation, new ArrayList<Course>());
	}
	
	Department(String abbreviation, ArrayList<Course> listOfCourses) {
		this.abbreviation = abbreviation;
		this.listOfCourses = listOfCourses;
	}
	
	public String getAbbreviation() {
		return this.abbreviation;
	}
	
	public ArrayList<Course> getList() {
		return this.listOfCourses;
	}
	
	public void addCourse(Course newCourse) {
		this.listOfCourses.add(newCourse);
	}
	
	@Override
	public String toString() {
		
		String list = "";
		for (int i = 0; i < listOfCourses.size(); i++) {
			if (i != 0)
				list += "\n,\n";
			list += abbreviation + " " + listOfCourses.get(i).toString();
		}
		
		return abbreviation +  " Department:\n\n" +
				list;
	}
}