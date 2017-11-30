package SimpArc;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class proc extends ViewableAtomic
{
  
	protected entity job;
	//generator로부터 받은 job
	protected double processing_time;
	//job의 실행시간.
	
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
		//passive상태, generator로부터 작업이 전송될 때까지 대기.
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		//passive상태라면
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//message x가 in 포트에 존재한다면
				{
					job = x.getValOnPort("in", i);
					//in 포트로 부터 메세지를 받아와서
					holdIn("busy", processing_time);
					//busy상태로 변환 후, processing_time만큼 처리한다.
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		//busy상태라면
		{
			job = new entity("");
			
			holdIn("passive", INFINITY);
			//passive상태로 변환 후, job이 들어올 때까지 무한정 대기.
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		//busy상태라면
		{
			m.add(makeContent("out", job));
			//out포트로 job을 출력.
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

