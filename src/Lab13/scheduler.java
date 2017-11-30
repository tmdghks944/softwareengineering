package Lab13;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class scheduler extends ViewableAtomic
{
  
	protected entity job;
	//generator로부터 받은 job
	protected double scheduling_time;
	//job의 실행시간.
	
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
		way=1; //way값이 증가하면서 (1->2->3->4->5->1....)순으로 job을 전송함.
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
					holdIn("busy", scheduling_time);
					//busy상태로 변환 후, processing_time만큼 처리한다.
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
		//passive상태로 변환 후, job이 들어올 때까지 무한정 대기.
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		//busy상태라면
		{
			m.add(makeContent("out"+way, job)); //RR스케줄링 원칙에 따른 JOB 전송..
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

