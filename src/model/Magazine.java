package model;

public class Magazine extends LibraryResource {

	public Magazine(String name, int resID){
		this.resourceName = name;
		this.resourceID = resID;
		this.type = Constants.MAGAZINE;
	}

}