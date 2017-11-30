package Lab13;
import GenCol.*;

public class loss_msg extends entity
{   
	int loss; //loss 메세지 수
	int who; //어떤p로부터 loss를 받았는가.
	public loss_msg(String name,int _who, int _loss)
	{  
		super(name);  
		loss = _loss;
		who = _who;
	}
}
