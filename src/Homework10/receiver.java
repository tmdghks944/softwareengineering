package Homework10;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class receiver extends ViewableAtomic
{
  
	protected packet packet;
	//generator로부터 받은 job
	protected double processing_time;
	//job의 실행시간.
	
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
					packet = (packet)x.getValOnPort("in", i);
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
		{	
			holdIn("passive", INFINITY);
			//새로운 packet을 받아 처리하기 위해 초기상태로 전이.
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		//busy상태라면
		{
			m.add(makeContent("out", packet));
			//out포트로 job을 출력.
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

