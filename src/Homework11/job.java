package Homework11;
import GenCol.*;

public class job extends entity
{   
	boolean isLast;
	public job(String name)
	{  
		super(name); 
		isLast=false;
	}
	public job(String name,boolean _isLast) {
		super(name);
		isLast = _isLast;
	}
	
}
