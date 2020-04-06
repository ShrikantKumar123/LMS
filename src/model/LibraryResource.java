package model;

public class LibraryResource {


	//name of a library resource, must be unique
	public String resourceName;

	//unique id of a library resource
	public int resourceID;

	public int type;

	//return gives the unique id of the resource
	public int getResourceID() {
		return this.resourceID;
	}
}
