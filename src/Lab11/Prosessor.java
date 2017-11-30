package Lab11;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class Prosessor extends ViewableAtomic
{
  
	protected entity job;
	//generator로부터 받은 job
	protected double processing_time;
	//job의 실행시간.
	
	public Prosessor()
	{
		this("proc", 20);
	}

	public Prosessor(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in");
		
		addOutport("out_1"); //generator에게 처리한 job을 변환할 포트
		addOutport("out_2"); //queue에게 done메세지를 보낼 포트.
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job = new entity("");
		
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
		{
			job = new entity("");
			
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		//다음 event는 busy상태일때.
		if (phaseIs("busy"))
		//busy상태라면
		{
			m.add(makeContent("out_1",job)); //generator에게 처리 완료한 job을 반환
			m.add(makeContent("out_2",new entity("done"))); //queue에게 done메세지를 전송.
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

