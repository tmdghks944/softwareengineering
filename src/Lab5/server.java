package Lab5;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class server extends ViewableAtomic
{
	protected packet job;
	protected double processing_time;

	
	public server()
	{
		this("procQ", 20);
	}

	public server(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		job = new packet(""); // job의 형태는 packet
		
		holdIn("wait", INFINITY); 
		// wait :  server가 client로부터 SYN메시지를 전달 받기 전까지의 상태
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait")) 
			// server가 client로부터 SYN메시지를 전달 받기 전까지의 상태에서
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
					// client로 부터 SYN메시지를 받으면
				{
					job = (packet) x.getValOnPort("in", i);

					holdIn("wait", processing_time);
					// wait 상태를 유지하다가
				}
			}
		}
		else if(phaseIs("SYN-received"))
			// client로 부터 ACK을 기다리는 상태에서
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
					//client로 부터 ACK을 받으면
				{
					job = (packet) x.getValOnPort("in", i);
					
					holdIn("established", INFINITY);
					// established 상태로 전이
					// established: 연결이 성립된 상태
				}
			}
		}
	}
	
	public void deltint()
	{
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("wait"))
		{
			m.add(makeContent("out", new packet("SYN-ACK")));
			// SYN-ACK메시지를 client에 전달한다.
			
			holdIn("SYN-received", processing_time);
			// SYN-ACK메시지 전달 후 SYN-received 상태로 천이 
			// client로 부터 ACK을 기다리는 상태
		}
		
		return m;
	}	
	
	public String getTooltipText()
	{
		return
        super.getTooltipText();
	}

}



