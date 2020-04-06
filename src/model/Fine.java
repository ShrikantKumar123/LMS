package model;

import java.util.*;

import businesslogic.Library;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Fine{

	public int fineID;
	public int resourceID;
	public int userID;
	public int fine;

	//Constructor for Fine class...
	public Fine(int _resID,int userID,int _fine){
		fineID = Library.nextFineID;
		Library.nextFineID++;
		this.resourceID = _resID;
		this.fine = _fine;
		this.userID = userID;
	}

	//to update fine.
	public void updateFine(int fine){
		this.fine = fine;
	}

}