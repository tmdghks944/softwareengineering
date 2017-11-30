package Lab13;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class scheduler extends ViewableAtomic
{
  
	protected entity job;
	//generator�κ��� ���� job
	protected double scheduling_time;
	//job�� ����ð�.
	
	int node;
	//
	int way;
	//
	
	public scheduler()
	{
		this("proc", 0,5);
	}

	public scheduler(String name, double Scheduling_time, int _node)
	{
		super(name);	
		
		node = _node;
		addInport("in");
		for(int i=1;i<=node;i++){
			addOutport("out"+i);
		}
		scheduling_time = Scheduling_time;
	}
  
	public void initialize()
	{
		way=1; //way���� �����ϸ鼭 (1->2->3->4->5->1....)������ job�� ������.
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
					holdIn("busy", scheduling_time);
					//busy���·� ��ȯ ��, processing_time��ŭ ó���Ѵ�.
				}
			}
		}
	}
  
	public void deltint()
	{
		way++;
		if(way>node) {
			way=1;
		}
		
		holdIn("passive", INFINITY);
		job = new entity("");
		//passive���·� ��ȯ ��, job�� ���� ������ ������ ���.
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		//busy���¶��
		{
			m.add(makeContent("out"+way, job)); //RR�����ٸ� ��Ģ�� ���� JOB ����..
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

