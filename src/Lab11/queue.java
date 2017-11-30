package Lab11;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class queue extends ViewableAtomic
{
	
	protected Queue q;
	protected entity job;
	protected double processing_time;
	
	public queue()
	{
		this("procQ", 20);
	}

	public queue(String name, double Processing_time)
	{
		super(name);
    
		addInport("in_1"); //GENERATOR�� ����� ��Ʈ
		addInport("in_2"); //PROCESSOR�� ����� ��Ʈ
		addOutport("out");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		q = new Queue();
		job = new entity("");
		
		holdIn("passive", INFINITY); //�ʱ� ����.
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in_1", i))
				{
					job = x.getValOnPort("in_1", i);
					
					holdIn("forwarding", 0); //forwarding : processor�� job�� ������ ������ ����.
				}
			}
		}
		else if (phaseIs("queuing")) //queuing : processor�� job�� ������ �Ұ����� ����.
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in_1", i))
				{
					entity temp = x.getValOnPort("in_1", i);
					q.add(temp);
				}
				if (messageOnPort(x, "in_2", i))
				{
					if(q.isEmpty()) //queue�ȿ� ���̻� ������ job�� ������
					{
						holdIn("stop",INFINITY);
					}
					
					else //�׷��� ���� ���
					{
						holdIn("forwarding",0);
					}
				}
			}
		}
	}
	
	public void deltint()
	{
		if (phaseIs("forwarding"))//processor���� job�� ������ �� �ڵ����� passive�� ����.
		{
			holdIn("queuing", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("forwarding"))
		{
			if(!q.isEmpty()) {
				job = (entity)q.removeFirst();
			}
			m.add(makeContent("out", job));
		}
		return m;
	}	
	
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + "queue length: " + q.size()
        + "\n" + "queue itself: " + q.toString();
	}

}



