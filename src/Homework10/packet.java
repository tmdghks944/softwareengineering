package Homework10;
import GenCol.*;

public class packet extends entity
{   
	int arrival;//목적지 receiver 번호
	
	public packet(String name, int _arrival)
	{  
		super(name);
		arrival = _arrival;
	}
	public int getArrival() //각 패킷의 receiver 번호를 가져오기 위해
	{ 
		return arrival;
	}
}
