package businesslogic;


import java.util.*;


import model.Constants;
import dao.Admin;
import model.LibraryInfo;
import model.LibraryResource;
import model.LibraryUser;
import model.Fine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Library implements LibraryInfo {

	
	public static Calendar calendar = new GregorianCalendar();
	String libraryName;

	static Library lib;

	
	public static int nextResID = 1001;
	public static int nextUserID = 14100180;
	//next fine ID... check  void updateFines() function
	public static int nextFineID = 0;		
	
	Set<Fine> toBeFined;	



	public ArrayList<LibraryResource> resources;

	public ArrayList<LibraryUser> users;

	private Library(String name) {
		calendar = Calendar.getInstance();
		this.libraryName = name;
		toBeFined = new HashSet<Fine>();



		resources = new ArrayList<LibraryResource>();
		users = new ArrayList<LibraryUser>();


		Admin admin = new Admin("shrikant", "12345", Constants.ADMIN,true);
		//add the new admin user to the list of the users
		users.add(admin);

	}


	public static Library getInstance(String name){
		if (lib == null){
			lib = new Library(name);
		}
		return lib;
	}

	public String getLibraryName(){
		return this.libraryName;
	}

	public void getLibraryStats(){

		int admin=0,faculty=0,students=0;

		//counts the number of admins, faculty and students in users ArrayList
		for(int i=0;i<users.size();i++){
			if(users.get(i).type == Constants.ADMIN)
				admin++;
			else if(users.get(i).type == Constants.FACULTY)
				faculty++;
			else
				students++;
		}

		
		
		
		
		
		
		int books=0,magazines=0,course_packs=0;

		int issued=0,overdue=0,request=0;
		Borrowable borrowable;

		Date date = calendar.getTime();

		// Counts the number of books,magazines and course packs. Also counts issued and over due resources.
		for(int i=0;i<resources.size();i++){
			if(resources.get(i).type == Constants.BOOK){
				books++;
				borrowable = (Borrowable)resources.get(i);
				if(borrowable.requests.size()>0)
					request++;
				if(borrowable.getReturnDate1() != null && date.compareTo(borrowable.getReturnDate1()) > 0)
					overdue++;
				if(!borrowable.checkStatus())
					issued++;
			}
			else if(resources.get(i).type == Constants.COURSE_PACK){
				course_packs++;
				borrowable = (Borrowable)resources.get(i);
				if(borrowable.requests.size() > 0)
					request++;
				if(borrowable.getReturnDate1() != null && date.compareTo(borrowable.getReturnDate1()) > 0)
					overdue++;
				if(!borrowable.checkStatus())
					issued++;
			}
			else
				magazines++;
		}
		
		System.out.println("\t\t\t\t*** Library System Stats ***\n\nNo. of Users:\t" + users.size());
		System.out.println("\tAdministrators: " + admin + "\n\tFaculty: " + faculty + "\n\tStudents: " + students);

		System.out.println("\nNumber of Resources: " + resources.size() + "\n\tBooks: " + books + "\n\tCourse Packs: " +
				course_packs + "\n\tMagazines: " + magazines);

		System.out.println("\n\tItems Issued: " + issued + "\n\tItems Overdue: " + overdue + "\n\tTotal Requests: " + request);
	}



	//Library users search functions

	public LibraryUser findUser(String name){

		//Checks all the users by their userNames
		for(int i=0;i<this.users.size();i++){
			if(users.get(i).userName.equals(name)){
				return users.get(i);
			}
		}
		return null;
	}

	public LibraryUser findUser(int userID){
		//Checks all the users using their userID's
		for(int i=0;i<users.size();i++){
			if(users.get(i).userID == userID){
				return users.get(i);
			}
		}
		return null;
	}

	/*******Library user removal function*******/

	public boolean removeUser(int userID){

		
		LibraryUser user = findUser(userID);
		if(user.type != Constants.ADMIN){
			Borrower borrower = (Borrower)user;
			for(int i=0;i<borrower.issuedResources.size();i++){
				lib.removeResource(borrower.issuedResources.get(i));
			}
		}


		for(int i=0;i<this.users.size();i++){
			if(users.get(i).userID == userID){
				users.remove(i);
				return true;
			}
		}
		return false;
	}

	/*******Resource Search Functions***********/


	LibraryResource findResource(int resID){
		//find resource by resourceID
		for(int i=0;i<resources.size();i++){
			if(resources.get(i).resourceID == resID){
				return resources.get(i);
			}
		}
		return null;
	}

	public ArrayList<LibraryResource> findResource(String resName){
		//find resource by resourceName
		ArrayList<LibraryResource> list = new ArrayList<LibraryResource>();
		for(int i=0;i<resources.size();i++){
			if(resources.get(i).resourceName.equals(resName)){
				list.add(resources.get(i));
			}
		}
		return list;
	}

	/*********Resource Removal Function*********/

	public boolean removeResource(int resourceID){

		LibraryResource res = findResource(resourceID);
		if(res == null)
			return false;
		if(res.type != Constants.MAGAZINE){

			Borrowable bor = (Borrowable)res;
			for(int i=0;i<bor.requests.size();i++){
				((Borrower)findUser(bor.requests.get(i))).deleteRequest(resourceID);
			}
			if(bor.checkStatus()){
				Borrower borrower = (Borrower)findUser(bor.issuedTo);
				borrower.tryReturn(bor.getResourceID());
			}
		}

		for(int i=0;i<resources.size();i++){
			if(resources.get(i).resourceID == resourceID){
				resources.remove(i);
				return true;
			}
		}
		return false;
	}

	/************ To Update Fines ************/


	public void updateFines(){

		

		Date today = Library.calendar.getTime();
		int amount = 0,days = 0;
		Borrower borrower;
		Borrowable borrowable;

		for(Fine fine: toBeFined){

			LibraryResource res = findResource(fine.resourceID);
			if(res == null){
				removeToBeFined(fine);
				continue;
			}
			borrowable = (Borrowable)res;

			if(today.compareTo(borrowable.getReturnDate1()) > 0){
				borrower = (Borrower)findUser(fine.userID);
				days = today.getDate()- borrowable.getReturnDate1().getDate();
				if(borrowable.type == Constants.BOOK){
					amount = days*100;
					fine.updateFine(amount);
				}
				else if(borrowable.type == Constants.COURSE_PACK){
					amount = days*500;
					fine.updateFine(amount);
				}
			}
		}
	}




	boolean removeToBeFined(Fine fine){

		//removes Fine(s) from toBeFined if they are returned!
		if(toBeFined.contains(fine)){
			toBeFined.remove(fine);
			return true;
		}
		else
			return false;
	}

	boolean addToBeFined(Fine fine){

		//adds Fine to toBeChecked collection
		if(!toBeFined.contains(fine)){
			toBeFined.add(fine);
			return true;
		}
		else
			return false;
	}
}
