package model;

import java.util.HashSet;

import businesslogic.Borrower;

public class Faculty extends Borrower{

	public Faculty(String name,String pass){
		this.userName = name;
		this.password = pass;
		this.type = Constants.FACULTY;
		this.maxResources = 6;
		this.fines = new HashSet<Fine>();
	}

}