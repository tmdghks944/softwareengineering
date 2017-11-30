package Lab5;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class server extends ViewableAtomic
{
	protected packet job;
	protected double processing_time;

	
	public server()
	{
		this("procQ", 20);
	}

	public server(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		job = new packet(""); // job�� ���´� packet
		
		holdIn("wait", INFINITY); 
		// wait :  server�� client�κ��� SYN�޽����� ���� �ޱ� �������� ����
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait")) 
			// server�� client�κ��� SYN�޽����� ���� �ޱ� �������� ���¿���
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
					// client�� ���� SYN�޽����� ������
				{
					job = (packet) x.getValOnPort("in", i);

					holdIn("wait", processing_time);
					// wait ���¸� �����ϴٰ�
				}
			}
		}
		else if(phaseIs("SYN-received"))
			// client�� ���� ACK�� ��ٸ��� ���¿���
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
					//client�� ���� ACK�� ������
				{
					job = (packet) x.getValOnPort("in", i);
					
					holdIn("established", INFINITY);
					// established ���·� ����
					// established: ������ ������ ����
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
		
		if (phaseIs("wait"))
		{
			m.add(makeContent("out", new packet("SYN-ACK")));
			// SYN-ACK�޽����� client�� �����Ѵ�.
			
			holdIn("SYN-received", processing_time);
			// SYN-ACK�޽��� ���� �� SYN-received ���·� õ�� 
			// client�� ���� ACK�� ��ٸ��� ����
		}
		
		return m;
	}	
	
	public String getTooltipText()
	{
		return
        super.getTooltipText();
	}

}



