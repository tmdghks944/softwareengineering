package Lab11;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class Prosessor extends ViewableAtomic
{
  
	protected entity job;
	//generator�κ��� ���� job
	protected double processing_time;
	//job�� ����ð�.
	
	public Prosessor()
	{
		this("proc", 20);
	}

	public Prosessor(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in");
		
		addOutport("out_1"); //generator���� ó���� job�� ��ȯ�� ��Ʈ
		addOutport("out_2"); //queue���� done�޼����� ���� ��Ʈ.
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job = new entity("");
		
		holdIn("passive", INFINITY);
		
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
		{
			job = new entity("");
			
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		//���� event�� busy�����϶�.
		if (phaseIs("busy"))
		//busy���¶��
		{
			m.add(makeContent("out_1",job)); //generator���� ó�� �Ϸ��� job�� ��ȯ
			m.add(makeContent("out_2",new entity("done"))); //queue���� done�޼����� ����.
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

