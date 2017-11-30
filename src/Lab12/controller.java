package Lab12;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class controller extends ViewableAtomic
{
  
	protected job_msg userJob, sensorJob;
	
	protected double processing_time;
	//job�� ����ð�.
	
	int count;
	double current_temp;//����µ�
	double user_temp;	//����µ�
	boolean IsWork;	//���ۿ���
	
	
	public controller()
	{
		this("proc", 20);
	}

	public controller(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in1"); //user�� ����� ��Ʈ
		addInport("in2"); //temp_sensor�� ����� ��Ʈ.
		
		
		addOutport("out"); //output��Ʈ����
		
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
		//passive���¶��
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in1", i))
				//message x�� in ��Ʈ�� �����Ѵٸ�
				{
					userJob = (job_msg)x.getValOnPort("in1", i);
					//in1 : user�� ����� ��Ʈ.
					IsWork = userJob.IsWork;		//���ۿ��θ� �޾ƿ�.
					holdIn("wait", processing_time);//wait���� ����.
				}
			}
		}
		else
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in1", i))
				//message x�� in ��Ʈ�� �����Ѵٸ�
				{
					userJob = (job_msg)x.getValOnPort("in1", i);
					//in1 : user�� ����� ��Ʈ.
					IsWork = userJob.IsWork;		//���ۿ��θ� �޾ƿ�.
					user_temp = userJob.temp;		//����µ��� ����.
				}
				if (messageOnPort(x, "in2", i))
					//message x�� in ��Ʈ�� �����Ѵٸ�
				{
					sensorJob = (job_msg)x.getValOnPort("in2", i);
					current_temp = sensorJob.temp;		//����µ��� ����.
				}
			}
	}
  
	public void deltint()
	{
		if (phaseIs("wait"))
		//busy���¶��
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
	//deltext�� deltint�� ���� ȣ�� ������ �ذ����ִ� �Լ�.
	{	
		deltext(0,x);
		//ext�� ���� ȣ���ϰ� �Ѵ�.
		deltint();
	}
	
	
	public message out()
	{
		message m = new message();
		if (phaseIs("wait"))
		//busy���¶��
		{
			m.add(makeContent("out", userJob)); //user�κ��� ���� ���۸޼����� temp_sensor���� ����.
		}
		else {
			if(IsWork==false)//�۵��ߴ�.
			{
				m.add(makeContent("out", userJob)); //user�κ��� ���� ���۸޼����� temp_sensor���� ����.
				holdIn("off",INFINITY);
			}
		}
		return m;
	}
}

