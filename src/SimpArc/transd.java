package SimpArc;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class transd extends  ViewableAtomic
{
	
	protected Function arrived, solved;
	//arrived : 생성된 job의 개수
	//solved : 처리된 job의 개수
	protected double clock, total_ta, observation_time;
	//성능 측정용 변수.
	public transd(String name, double Observation_time)
	{
		super(name);
    
		addOutport("out");
		addInport("ariv");
		addInport("solved");
    
		arrived = new Function();
		solved = new Function();
    
		observation_time = Observation_time;
	}
  
	public transd()
	{
		this("transd", 200);
	}

	public void initialize()
	{	
		clock = 0;
		total_ta = 0;
    
		arrived = new Function();
		solved = new Function();
		
		holdIn("on", observation_time);
		//on상태, observation_time동안 성능을 측정.
	}

	public void deltext(double e, message x)
	{
		clock = clock + e;
		//현재 시간을 측정.
		Continue(e);
		entity val;
 
		if(phaseIs("on"))
		//on상태라면
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "ariv", i))
				//ariv 포트로 수신한 메세지가 있을 때
				{
					val = x.getValOnPort("ariv", i);
					//ariv 포트에서 메세지를 받아서
					arrived.put(val.getName(), new doubleEnt(clock));
					//작업응답시간 측정을 위해 생산한 job의 수신 시간 기록.
				}
				if (messageOnPort(x, "solved", i))
				//solve포트로 수신한 메세지가 있을 때
				{
					val = x.getValOnPort("solved", i);
					//solve포트에서 메세지를 받아
					if (arrived.containsKey(val.getName()))
					//발생된 job에 대한 정보를 추적.
					{
						entity ent = (entity) arrived.assoc(val.getName());
					
						doubleEnt num = (doubleEnt) ent;
						double arrival_time = num.getv();
          
						double turn_around_time = clock - arrival_time;
          
						total_ta = total_ta + turn_around_time;
          
						solved.put(val, new doubleEnt(clock));
					}
				}
			}
			show_state();
		}
	}

	public void deltint()
	{
		if (phaseIs("on"))
		//on상태일 때
		{
			clock = clock + sigma;
			System.out.println("--------------------------------------------------------");
	   		show_state();
	   		System.out.println("--------------------------------------------------------");
	   		
	   		holdIn("off", 0);
	   		//off상태로 변환하고 sigma를 0으로.
		}
	}
  
	public message out()
	{
		message m = new message();
		
		if (phaseIs("on"))
		{
			m.add(makeContent("out", new entity("TA: " + compute_TA())));
		}
		
		return m;
	}

	public double compute_TA()
	{
		double avg_ta_time = 0;
		if (!solved.isEmpty())
		{
			avg_ta_time = ( (double) total_ta) / solved.size();
		}
		return avg_ta_time;
	}

  
	public String compute_Thru()
	{
		String thruput = "";
		if (clock > 0)
		{
			thruput = solved.size() + " / " + clock;
		}
		return thruput;
	}

	public void show_state()
	{
		System.out.println("state of  " + name + ": ");
		System.out.println("phase, sigma : " + phase + " " + sigma + " ");
		
		if (arrived != null && solved != null)
		{
			System.out.println("Total jobs arrived : "+ arrived.size());
			System.out.println("Total jobs solved : " + solved.size());
			System.out.println("AVG TA = " + compute_TA());
			System.out.println("THRUPUT = " + compute_Thru());
		}
	}	
  
	public String getTooltipText()
	{
		String s = "";
		if (arrived != null && solved != null)
		{
			s = "\n" + "jobs arrived :" + arrived.size()
			+ "\n" + "jobs solved :" + solved.size()
			+ "\n" + "AVG TA = " + compute_TA()
			+ "\n" + "THRUPUT = " + compute_Thru();
		}
		return super.getTooltipText() + s;
	}

}
