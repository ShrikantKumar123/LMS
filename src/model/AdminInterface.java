package model;
//This interface must be implemented by the Admin class
public interface AdminInterface {

	int addUser(String userName, String password, int type);

	boolean removeUser(int userID);
	boolean removeResource(int resourceID);
	int addResource(String name, int type);

	

}
