package Lab13;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class processor extends ViewableAtomic
{
  
	protected entity job;
	//generator�κ��� ���� job
	protected double processing_time;
	//job�� ����ð�.
	protected Queue q;
	
	int size; 
	// maximum queue size
	int loss; 
	//ó������ ���� job�� ����.
	int who; 
	//job�� ó������ ���� processor
	double temp_time;
	//processor�� sigma �� ���� ����
	
	
	public processor()
	{
		this("proc", 20,3);
	}

	public processor(String name, double Processing_time,int _size)
	{
		super(name);	
    
		addInport("in");
		
		addOutport("out1");
		//solved�� ���� (jobó���Ϸ�)
		addOutport("out2");
		//loss port�� ���� (loss msg����)
		
		who = Integer.parseInt(name.substring(9));
		//substring () ���� ��ġ �������� ���� ���ڿ��� ������ ���ڿ��� �������� �޼ҵ�.
		//processor #? ���� ?�� ������.
		size = _size;
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		q = new Queue();
		loss=0;
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
		else if(phaseIs("busy")) 
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if(q.size()<size) 
					//queue size <maximum queue size
				{
					entity job = x.getValOnPort("in", i);
					q.add(job);
				}
				else
				{
					loss++;
					temp_time = sigma;
					holdIn("loss",0);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("loss"))
		{
			job = new entity("");
			
			holdIn("busy", temp_time);
		}
		//busy
		else 
		{
			if(!q.isEmpty()) {
				job = (entity)q.removeFirst();
				holdIn("busy",processing_time);
			}
			else {
				holdIn("passive",INFINITY);
			}
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		//busy���¶��
		{
			m.add(makeContent("out1", job)); //ó�� �Ϸ��� JOB�� EVALUATOR���� ����/
			//out��Ʈ�� job�� ���.
		}
		if(phaseIs("loss")) 
			//processor�� queue���� ���̻� ������ �������� job�� ó���Ҽ����°��.
		{
			m.add(makeContent("out2", new loss_msg(who + ":"+loss,who,loss)));
			//loss msg�� evaluator���� ����.
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

