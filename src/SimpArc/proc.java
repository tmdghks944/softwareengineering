package SimpArc;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class proc extends ViewableAtomic
{
  
	protected entity job;
	//generator�κ��� ���� job
	protected double processing_time;
	//job�� ����ð�.
	
	public proc()
	{
		this("proc", 20);
	}

	public proc(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job = new entity("");
		
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
					job = x.getValOnPort("in", i);
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
		//busy���¶��
		{
			job = new entity("");
			
			holdIn("passive", INFINITY);
			//passive���·� ��ȯ ��, job�� ���� ������ ������ ���.
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		//busy���¶��
		{
			m.add(makeContent("out", job));
			//out��Ʈ�� job�� ���.
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + job.getName();
	}

}

