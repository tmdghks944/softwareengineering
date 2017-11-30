package Homework12;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class temp_sensor extends ViewableAtomic
{
  
	protected job_msg job;
	double[] current_temp = {15, 15, 19, 24, 25, 24, 22}; //����µ�.
	int count;
	
	
	protected double processing_time;
	
	public temp_sensor()
	{
		this("proc",10);
	}

	public temp_sensor(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job = new job_msg("off",false);
		count = 0;
		holdIn("off", INFINITY);
		//passive����, generator�κ��� �۾��� ���۵� ������ ���.
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("off"))
		//passive���¶��
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//message x�� in ��Ʈ�� �����Ѵٸ�
				{
					job = (job_msg)x.getValOnPort("in", i);
					// in : controller�� ���� �޼����� �޴� ��Ʈ.
					holdIn("on", processing_time);
					// on���·� ����.
				}
			}
		}
		else if(phaseIs("on")) 
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//message x�� in ��Ʈ�� �����Ѵٸ�
				{
					job = (job_msg)x.getValOnPort("in", i);
					// in : controller�� ���� �޼����� �޴� ��Ʈ.
					if(job.IsWork==false) 
					{
						holdIn("off", processing_time);
					}
					
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("on"))
		//busy���¶��
		{
			holdIn("on", processing_time);
			//passive���·� ��ȯ ��, job�� ���� ������ ������ ���.
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("on"))	//on�����̸� 
		//busy���¶��
		{
			m.add(makeContent("out", new job_msg("current_temp : "+ current_temp[count],current_temp[count])));
			count++;
		}
		return m;
	}
}

