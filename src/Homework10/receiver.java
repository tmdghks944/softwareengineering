package Homework10;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class receiver extends ViewableAtomic
{
  
	protected packet packet;
	//generator�κ��� ���� job
	protected double processing_time;
	//job�� ����ð�.
	
	public receiver()
	{
		this("proc", 20);
	}

	public receiver(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		packet = new packet("",0);
		
		holdIn("passive", INFINITY);
		//passive����, generator�κ��� �۾��� ���۵� ������ ���.
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		//passive���¶��
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//message x�� in ��Ʈ�� �����Ѵٸ�
				{
					packet = (packet)x.getValOnPort("in", i);
					//in ��Ʈ�� ���� �޼����� �޾ƿͼ�
					holdIn("busy", processing_time);
					//busy���·� ��ȯ ��, processing_time��ŭ ó���Ѵ�.
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{	
			holdIn("passive", INFINITY);
			//���ο� packet�� �޾� ó���ϱ� ���� �ʱ���·� ����.
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		//busy���¶��
		{
			m.add(makeContent("out", packet));
			//out��Ʈ�� job�� ���.
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + packet.getName();
	}

}

