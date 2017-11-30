package Homework11;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class queue extends ViewableAtomic
{
	
	protected Queue q;
	protected job job;
	protected double processing_time;
	
	public queue()
	{
		this("procQ", 20);
	}

	public queue(String name, double Processing_time)
	{
		super(name);
    
		addInport("in_1"); //GENERATOR와 연결된 포트
		addInport("in_2"); //PROCESSOR와 연결된 포트
		addOutport("out");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		q = new Queue();
		job = new job("");
		
		holdIn("passive", INFINITY); //초기 상태.
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
					job = (job)x.getValOnPort("in_1", i);
					
					holdIn("forwarding", 0); //forwarding : processor로 job이 전송이 가능한 상태.
				}
			}
		}
		else if (phaseIs("queuing")) //queuing : processor로 job이 전송이 불가능한 상태.
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in_1", i))
				{
					job temp = (job)x.getValOnPort("in_1", i);
					q.add(temp);
				}
				if (messageOnPort(x, "in_2", i))
				{
					if(q.isEmpty()) //queue안에 더이상 전송할 job이 없으면
					{
						holdIn("stop",INFINITY);
					}
					
					else //그렇지 않을 경우
					{
						holdIn("forwarding",0);
					}
				}
			}
		}
	}
	
	public void deltint()
	{
		if (phaseIs("forwarding"))//processor에게 job을 전송한 후 자동으로 passive로 전이.
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
				job = (job)q.removeFirst();
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



