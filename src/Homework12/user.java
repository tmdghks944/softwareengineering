package Homework12;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class user extends ViewableAtomic
{
	double user_temp; //user의 희망온도.
	protected double int_arr_time;
	protected int count;
	
	public user()
	{
		this("genr");
	}
  
	public user(String name)
	{
		super(name);
   
		addOutport("out");
		//out이라는 output port를 생성
	}
  
	public void initialize()
	{
		holdIn("READY", 10);
		user_temp = 24;	//user의 희망온도.
	}
  
	public void deltext(double e, message x)
	//외부에서 메세지가 들어왔을 때
	{
		Continue(e);
	}

	public void deltint()
	//내부에서 메세지가 나갈 때
	{
		if (phaseIs("READY"))
		{
			holdIn("SWITCH ON", 10);
		}
	}

	public message out()
	//다른모델로 메세지를 보낼 때
	{
		message m = new message();
		//메세지를 생성하고
		if(phaseIs("READY")) {
			m.add(makeContent("out", new job_msg("on",true)));
			//에어컨 작동 메세지를 CONTROLLER에게 전송.
		}
		else if(phaseIs("SWITCH ON")) {
			m.add(makeContent("out", new job_msg("user_temp : " + user_temp,user_temp)));
			//user의 희망온도를 controller에게 전송.
			holdIn("SET TEMP",60);
			//SET TEMP상태로 전이.
		}
		else if(phaseIs("SET TEMP")) {
			m.add(makeContent("out", new job_msg("off",false)));
			//에어컨 중단메세지를 CONTROLLER에게 전송.
			holdIn("SWITCH OFF",INFINITY);
			//SWITCH OFF 상태로 전이
		}
		return m;
	}
  
	public String getTooltipText()
	//커서 가져다 댔을 때 나오는 정보를 출력.
	{
		return
        super.getTooltipText()
        + "\n" + " count: " + count;
	}

}
