package Lab13;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class evaluator extends ViewableAtomic
{
	int solved;//ó�� �Ϸ��� job�� ��.
	int loss;//ó�� �Ϸ����� ���� job�� ��
	int arrive; //generator�� ������ ��� job�� ��.
	
	public evaluator()
	{
		this("evaluator");
	}

	public evaluator(String name)
	{
		super(name);	
    
		addInport("arrive");
		addInport("solved");
		addInport("loss");
		
		
		addOutport("out");
	}
  
	public void initialize()
	{
		arrive=0;
		solved=0;
		loss=0;
		
		holdIn("active", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("active"))
		//passive���¶��
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "arrive", i))
				//message x�� in ��Ʈ�� �����Ѵٸ�
				{
					arrive++;
				}
				else if (messageOnPort(x, "solved", i))
					//message x�� in ��Ʈ�� �����Ѵٸ�
					{
					solved++;
					}
				else if (messageOnPort(x, "loss", i))
					//message x�� in ��Ʈ�� �����Ѵٸ�
					{
					loss++;
					}
			}
		}
	}
  
	public void deltint()
	{
	}

	public message out()
	{
		message m = new message();
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "total: " + arrive
		+ "\n" + "solved: " + solved
		+ "\n" + "loss: " + loss;
	}

}

