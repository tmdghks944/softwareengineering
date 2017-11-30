package Lab10;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class router extends ViewableAtomic
{
	protected Queue q;
	protected packet packet;
	//generator�κ��� ���� job
	protected double processing_time;
	//job�� ����ð�.
	
	public router()
	{
		this("proc", 20);
	}

	public router(String name, double Processing_time)
	{
		super(name);	
    
		addInport("in");
		
		
		for(int i=1;i<=5;i++) {
			addOutport("out"+i);	//receiver�� ����� outputport ����
		}
		
		addOutport("out");
		//sender�� ����� output port
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		packet = new packet("",0);
		q = new Queue();
		
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		//passive���¶��
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//message x�� in ��Ʈ�� �����Ѵٸ�
				{
					packet = (packet)x.getValOnPort("in", i);
					//in ��Ʈ�� ���� �޼����� �޾ƿͼ�
					q.add(packet);
					if(q.size()==5) {
						holdIn("sending", processing_time);	
					}
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
		if (phaseIs("sending"))
		//busy���¶��
		{
			if(!q.isEmpty()) {
				packet = (packet)q.removeFirst();
				//q���� packet�� �����ͼ�
				int portNum = packet.getArrival();
				//������ �ش� packet�� ������ ��ȣ�� �˾ƿµ�
				m.add(makeContent("out" + portNum, packet));
				//�ش� �������� packet�� ����.
				holdIn("sending",processing_time);
			}
			else {
				m.add(makeContent("out", new packet("done",0)));
				//router�� 5���� ��Ŷ�� ��� receiver���� ���� �� sender���� done�� ����.
				holdIn("passive",processing_time);
			}
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + packet.getName();
	}

}



