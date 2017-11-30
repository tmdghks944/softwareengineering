package Homework11;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class Prosessor extends ViewableAtomic
{
  
	protected job job;
	//generator로부터 받은 job
	protected double processing_time;
	//job의 실행시간.
	protected boolean isLastJob;
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
		job = new job("");
		
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
					job = (job)x.getValOnPort("in", i);
					//in 포트로 부터 메세지를 받아와서
					holdIn("busy", processing_time);
					
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			job = new job("");
			
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
			if(job.isLast==false) {
				m.add(makeContent("out_1",job)); //generator에게 처리 완료한 job을 반환
				m.add(makeContent("out_2",new entity("done"))); //queue에게 done메세지를 전송.
			}
			else {
				m.add(makeContent("out_1",job)); //generator에게 처리 완료한 job을 반환
				m.add(makeContent("out_2",new entity("done"))); //queue에게 done메세지를 전송.
				holdIn("stop",INFINITY);
			}
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

