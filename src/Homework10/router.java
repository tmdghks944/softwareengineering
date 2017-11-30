package Homework10;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class router extends ViewableAtomic
{
	protected Queue q;
	protected packet packet;
	//generator로부터 받은 job
	protected double processing_time;
	//job의 실행시간.
	
	public router()
	{
		this("proc", 20);
	}

	public router(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in");
		
		
		for(int i=1;i<=5;i++) {
			addOutport("out"+i);	//receiver와 연결된 outputport 생성
		}
		
		addOutport("out");
		//sender와 연결된 output port
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		packet = new packet("",0);
		q = new Queue();
		
		holdIn("passive", INFINITY);
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
					q.add(packet);
					if(q.size()<=5) {
						holdIn("passive", processing_time);	
					}
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
		if(q.size()==5) 
		{
			holdIn("sending",processing_time);
		}
		if (phaseIs("sending"))
		//busy상태라면
		{
			if(!q.isEmpty()) {
				packet = (packet)q.removeFirst();
				//q에서 packet을 가져와서
				int portNum = packet.getArrival();
				//가져온 해당 packet의 목적지 번호를 알아온뒤
				m.add(makeContent("out" + portNum, packet));
				//해당 목적지로 packet을 전송.
				holdIn("sending",processing_time);
			}
			else 
			{
				m.add(makeContent("out", new packet("done",0)));
				//router가 5개의 패킷을 모두 receiver에게 전송 후 sender에게 done을 보냄.
				holdIn("passive",processing_time);
			}
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