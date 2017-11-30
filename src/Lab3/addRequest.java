package Lab3;
import GenCol.*;

public class addRequest extends entity // entity를 상속받은 addRequest 클래스 생성
{
	
	int num1; // num1 
	int num2; // num2
	
	
	public addRequest(String name, int _num1, int _num2) // addRequest 생성자
	{  
		super(name);

		num1 = _num1;
		num2 = _num2;
	}

}
