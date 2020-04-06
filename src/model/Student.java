package model;

import java.util.HashSet;

import businesslogic.Borrower;

public class Student extends Borrower{

	public Student(String name,String pass){
		this.userName = name;
		this.password = pass;
		this.type = Constants.STUDENT;
		this.maxResources = 3;
		this.fines = new HashSet<Fine>();
	}

}
