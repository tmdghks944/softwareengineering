package Lab12;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class user extends ViewableAtomic
{
	double user_temp; //user�� ����µ�.
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
		//out�̶�� output port�� ����
	}
  
	public void initialize()
	{
		holdIn("READY", 10);
		user_temp = 24;	//user�� ����µ�.
	}
  
	public void deltext(double e, message x)
	//�ܺο��� �޼����� ������ ��
	{
		Continue(e);
	}

	public void deltint()
	//���ο��� �޼����� ���� ��
	{
		if (phaseIs("READY"))
		{
			holdIn("SWITCH ON", 10);
		}
	}

	public message out()
	//�ٸ��𵨷� �޼����� ���� ��
	{
		message m = new message();
		//�޼����� �����ϰ�
		if(phaseIs("READY")) {
			m.add(makeContent("out", new job_msg("on",true)));
			//������ �۵� �޼����� CONTROLLER���� ����.
		}
		else if(phaseIs("SWITCH ON")) {
			m.add(makeContent("out", new job_msg("user_temp : " + user_temp,user_temp)));
			//user�� ����µ��� controller���� ����.
			holdIn("SET TEMP",60);
			//SET TEMP���·� ����.
		}
		else if(phaseIs("SET TEMP")) {
			m.add(makeContent("out", new job_msg("off",false)));
			//������ �ߴܸ޼����� CONTROLLER���� ����.
			holdIn("SWITCH OFF",INFINITY);
			//SWITCH OFF ���·� ����
		}
		return m;
	}
  
	public String getTooltipText()
	//Ŀ�� ������ ���� �� ������ ������ ���.
	{
		return
        super.getTooltipText()
        + "\n" + " count: " + count;
	}

}
