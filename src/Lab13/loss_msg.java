package Lab13;
import GenCol.*;

public class loss_msg extends entity
{   
	int loss; //loss �޼��� ��
	int who; //�p�κ��� loss�� �޾Ҵ°�.
	public loss_msg(String name,int _who, int _loss)
	{  
		super(name);  
		loss = _loss;
		who = _who;
	}
}
