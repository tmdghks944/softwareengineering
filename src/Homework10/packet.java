package Homework10;
import GenCol.*;

public class packet extends entity
{   
	int arrival;//������ receiver ��ȣ
	
	public packet(String name, int _arrival)
	{  
		super(name);
		arrival = _arrival;
	}
	public int getArrival() //�� ��Ŷ�� receiver ��ȣ�� �������� ����
	{ 
		return arrival;
	}
}
