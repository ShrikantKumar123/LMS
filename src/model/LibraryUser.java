package model;



public class LibraryUser{

	public String userName;

	protected String password;

	public int userID;

	public int type;

	int getUserID(){
		return userID;
	}


	public boolean login(String userName, String password){

		if((userName.equals(this.userName) && password.equals(this.password))){
			return true;
		}
		else{
			System.out.println("The username or password entered was not correct!");
			return false;
		}
	}


	public boolean logout() {


		return true;
	}
}