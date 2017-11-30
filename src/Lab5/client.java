package Lab5;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class client extends ViewableAtomic
{
	protected double int_arr_time;
	protected int count;
  
	public client() 
	{
		this("genr", 30);
	}
  
	public client(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
   
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 1;

		holdIn("active", int_arr_time);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);

		if (phaseIs("sent"))
			// sent : client�� SYN�޽����� ������ SYN-ACK�� �ޱ� �������� ����
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
					// sever�� ���� SYN-ACK�� ���� ������
				{
					holdIn("sent", int_arr_time);
					// sent���·� �����ϴٰ�
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

		if(phaseIs("active")) 
			// active : client�� SYN�޽����� ������ �������� client ����
		{
			m.add(makeContent("out", new packet("SYN")));
			// SYN �޽��� ����
			holdIn("sent", INFINITY);
			// client�� SYN�޽��� ���� �� sent ���·� ����
			// sent : client�� SYN�޽����� ������ SYN-ACK�� �ޱ� �������� ����
		}
		else if(phaseIs("sent"))
			// sent���°� ���� ���� ���¿���
		{
			m.add(makeContent("out", new packet("ACK")));
			// ACK�޽��� ����
			holdIn("connect", INFINITY);
			// ACK�޽��� ���� �� �����������
			// connect : ���� ���� ����
		}
		
		
		return m;
	}

	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

}
