package Homework12;
import GenCol.*;

public class job_msg extends entity
{   
	boolean IsWork;	//동작여부
	double temp;	//dhseh
	
	public job_msg(String name, boolean _work) //작동여부 메세지 (true이면 on false이면 off)
	{  
		super(name); 
		
		IsWork = _work;
		temp=0;
	}
	
	public job_msg(String name, double _temp) // 온도 전달 메세지.
	{  
		super(name); 
		
		IsWork = true;
		temp=_temp;
	}
	
}
