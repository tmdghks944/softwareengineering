package Lab13;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class genr extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
	
	public genr()
	{
		this("genr", 30);
	}
  
	public genr(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		//out�̶�� output port�� ����
		addInport("in");
		//in�̶�� input port�� ����.
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 0;
		
		holdIn("active", int_arr_time);
	}
  
	public void deltext(double e, message x)
	//�ܺο��� �޼����� ������ ��
	{
		Continue(e);
		if (phaseIs("active"))
		//���� �޼����� ���°� active���
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//�޼��� x�� in��Ʈ�� ���Դٸ�
				{
				}
			}
		}
	}

	public void deltint()
	//���ο��� �޼����� ���� ��
	{
		if (phaseIs("active"))
		{
			count = count + 1;
			holdIn("active", int_arr_time);
		}
	}

	public message out()
	//�ٸ��𵨷� �޼����� ���� ��
	{
		message m = new message();
		//�޼����� �����ϰ�
		m.add(makeContent("out", new entity("job" + count)));
		//makeContent : ������ ��ü�� ���, new entity : ��ü�� ����.
		//out�� �̸��� ���� ��Ʈ�� ��ü�� ����.
		return m;
	}
  
	public String getTooltipText()
	//Ŀ�� ������ ���� �� ������ ������ ���.
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

	/*
	 * entity�� message�� ����
	 * entity : �̸��� ������ ��ü -> �ù��� ���빰.
	 * message : �𵨳��� entity�� �ְ���� �� �ʿ��� ����(��Ʈ �̸�)���� ���� ��ü -> �ù� �ڽ�
	 * */
}
