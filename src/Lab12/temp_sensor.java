package Lab12;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class temp_sensor extends ViewableAtomic
{
  
	protected job_msg job;
	double[] current_temp = {15, 15, 19, 24, 25, 24, 22}; //현재온도.
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
		//passive상태, generator로부터 작업이 전송될 때까지 대기.
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("off"))
		//passive상태라면
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//message x가 in 포트에 존재한다면
				{
					job = (job_msg)x.getValOnPort("in", i);
					// in : controller로 부터 메세지를 받는 포트.
					holdIn("on", processing_time);
					// on상태로 전이.
				}
			}
		}
		else if(phaseIs("on")) 
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//message x가 in 포트에 존재한다면
				{
					job = (job_msg)x.getValOnPort("in", i);
					// in : controller로 부터 메세지를 받는 포트.
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
		//busy상태라면
		{
			holdIn("on", processing_time);
			//passive상태로 변환 후, job이 들어올 때까지 무한정 대기.
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("on"))	//on상태이면 
		//busy상태라면
		{
			m.add(makeContent("out", new job_msg("current_temp : "+ current_temp[count],current_temp[count])));
			count++;
		}
		return m;
	}
}

