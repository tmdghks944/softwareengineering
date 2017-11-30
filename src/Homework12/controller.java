package Homework12;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class controller extends ViewableAtomic
{
  
	protected job_msg userJob, sensorJob;
	
	protected double processing_time;
	//job의 실행시간.
	
	int count;
	double current_temp;//현재온도
	double user_temp;	//희망온도
	boolean IsWork;	//동작여부
	
	
	public controller()
	{
		this("proc", 20);
	}

	public controller(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in1"); //user와 연결될 포트
		addInport("in2"); //temp_sensor와 연결될 포트.
		
		
		addOutport("out"); //output포트생성
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		userJob = new job_msg("off",false);
		sensorJob = new job_msg("off",false);
		
		user_temp=0;
		current_temp=0;
		IsWork = false;
		
		holdIn("off", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("off"))
		//passive상태라면
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in1", i))
				//message x가 in 포트에 존재한다면
				{
					userJob = (job_msg)x.getValOnPort("in1", i);
					//in1 : user와 연결된 포트.
					IsWork = userJob.IsWork;		//동작여부를 받아옴.
					holdIn("wait", processing_time);//wait상태 전이.
				}
			}
		}
		else
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in1", i))
				//message x가 in 포트에 존재한다면
				{
					userJob = (job_msg)x.getValOnPort("in1", i);
					//in1 : user와 연결된 포트.
					IsWork = userJob.IsWork;		//동작여부를 받아옴.
					user_temp = userJob.temp;		//희망온도를 설정.
				}
				if (messageOnPort(x, "in2", i))
					//message x가 in 포트에 존재한다면
				{
					sensorJob = (job_msg)x.getValOnPort("in2", i);
					current_temp = sensorJob.temp;		//현재온도값 설정.
				}
			}
	}
  
	public void deltint()
	{
		if (phaseIs("wait"))
		//busy상태라면
		{
			holdIn("ready", processing_time);
		}
		else
		{
			if(current_temp !=0.0 && user_temp != 0.0) {
				if(current_temp<user_temp) 
				{
					holdIn("heating",processing_time);
				}
				else if(current_temp>user_temp) 
				{
					holdIn("cooling",processing_time);
				}
			}
		}
	}

	public void deltcon(double e, message x) 
	//deltext와 deltint의 동시 호출 문제를 해결해주는 함수.
	{	
		deltext(0,x);
		//ext를 먼저 호출하게 한다.
		deltint();
	}
	
	
	public message out()
	{
		message m = new message();
		if (phaseIs("wait"))
		//busy상태라면
		{
			m.add(makeContent("out", userJob)); //user로부터 받은 동작메세지를 temp_sensor에게 전달.
		}
		else {
			if(IsWork==false)//작동중단.
			{
				m.add(makeContent("out", userJob)); //user로부터 받은 동작메세지를 temp_sensor에게 전달.
				holdIn("off",INFINITY);
			}
		}
		return m;
	}
}

