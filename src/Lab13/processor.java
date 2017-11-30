package Lab13;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class processor extends ViewableAtomic
{
  
	protected entity job;
	//generator로부터 받은 job
	protected double processing_time;
	//job의 실행시간.
	protected Queue q;
	
	int size; 
	// maximum queue size
	int loss; 
	//처리하지 못한 job의 갯수.
	int who; 
	//job을 처리하지 못한 processor
	double temp_time;
	//processor의 sigma 값 저장 변수
	
	
	public processor()
	{
		this("proc", 20,3);
	}

	public processor(String name, double Processing_time,int _size)
	{
		super(name);	
    
		addInport("in");
		
		addOutport("out1");
		//solved와 연결 (job처리완료)
		addOutport("out2");
		//loss port와 연결 (loss msg연결)
		
		who = Integer.parseInt(name.substring(9));
		//substring () 지정 위치 다음에서 부터 문자열의 끝까지 문자열을 가져오는 메소드.
		//processor #? 에서 ?을 가져옴.
		size = _size;
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		q = new Queue();
		loss=0;
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
		else if(phaseIs("busy")) 
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if(q.size()<size) 
					//queue size <maximum queue size
				{
					entity job = x.getValOnPort("in", i);
					q.add(job);
				}
				else
				{
					loss++;
					temp_time = sigma;
					holdIn("loss",0);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("loss"))
		{
			job = new entity("");
			
			holdIn("busy", temp_time);
		}
		//busy
		else 
		{
			if(!q.isEmpty()) {
				job = (entity)q.removeFirst();
				holdIn("busy",processing_time);
			}
			else {
				holdIn("passive",INFINITY);
			}
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		//busy상태라면
		{
			m.add(makeContent("out1", job)); //처리 완료한 JOB을 EVALUATOR에게 전송/
			//out포트로 job을 출력.
		}
		if(phaseIs("loss")) 
			//processor의 queue에게 더이상 저장을 하지못해 job을 처리할수없는경우.
		{
			m.add(makeContent("out2", new loss_msg(who + ":"+loss,who,loss)));
			//loss msg를 evaluator에게 전송.
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

