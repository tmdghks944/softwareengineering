package Lab4;
import GenCol.*;

public class job extends entity
{   
	int num;
	boolean isLast; // 마지막 정수임을 알리는 flag (isLast = true)
	
	public job(String name, int _num)
	{  
		super(name);
		
		num = _num;
		isLast = false;
	}

	public job(String name, int _num, boolean _isLast)
	{  
		super(name);
		
		num = _num;
		isLast = _isLast;
	}
}
