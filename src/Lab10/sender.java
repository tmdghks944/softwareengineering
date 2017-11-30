package Lab10;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class sender extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
	protected int packet_num; //������ packet��
	
	public sender()
	{
		this("genr", 30);
	}
  
	public sender(String name, double Int_arr_time)
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
		count = 1;		
		//������ packet�� ���� �ʱ�ȭ (packet 1, packet 2,...packet n)
		packet_num =0; 
		//������ packet�� ���� �ʱ�ȭ packet_num<=5
		holdIn("active", int_arr_time);
	}
  
	public void deltext(double e, message x)
	//�ܺο��� �޼����� ������ ��
	{
		Continue(e);
		
		if (phaseIs("wait"))
		//���� �޼����� ���°� active���
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//�޼��� x�� in��Ʈ�� ���Դٸ�
				{
					holdIn("active", int_arr_time);
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
			
		}
		else if(phaseIs("wait"))
		//router�� ���� done�޼����� ������
		{
			count=1;
			packet_num=0;
		}
	}

	public message out()
	//�ٸ��𵨷� �޼����� ���� ��
	{
		message m = new message();
		//�޼����� �����ϰ�
		if(phaseIs("active")) {
			if(packet_num<5) {
				m.add(makeContent("out", new packet("packet" + count,(int)((Math.random())*5+1))));
				packet_num = packet_num+1;
				//��Ŷ�� �������� �����ϰ�.
				//packet(name, _arrival)
			}
			if(packet_num==5) {
				holdIn("wait",INFINITY);
			}
		}
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
