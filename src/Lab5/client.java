package Lab5;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class client extends ViewableAtomic
{
	protected double int_arr_time;
	protected int count;
  
	public client() 
	{
		this("genr", 30);
	}
  
	public client(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
   
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 1;

		holdIn("active", int_arr_time);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);

		if (phaseIs("sent"))
			// sent : client가 SYN메시지를 보내고 SYN-ACK을 받기 전까지의 상태
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
					// sever로 부터 SYN-ACK을 전달 받으면
				{
					holdIn("sent", int_arr_time);
					// sent상태로 유지하다가
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

		if(phaseIs("active")) 
			// active : client가 SYN메시지를 보내기 전까지의 client 상태
		{
			m.add(makeContent("out", new packet("SYN")));
			// SYN 메시지 전송
			holdIn("sent", INFINITY);
			// client는 SYN메시지 전송 후 sent 상태로 전이
			// sent : client가 SYN메시지를 보내고 SYN-ACK을 받기 전까지의 상태
		}
		else if(phaseIs("sent"))
			// sent상태가 유지 중인 상태에서
		{
			m.add(makeContent("out", new packet("ACK")));
			// ACK메시지 전송
			holdIn("connect", INFINITY);
			// ACK메시지 전송 후 연결상태유지
			// connect : 연결 성립 상태
		}
		
		
		return m;
	}

	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

}
