package Practice;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class accumulator extends ViewableAtomic
{
	
	protected Queue q; // user로 부터 받은 정수를 저장할 queue
	protected job job;
	protected double processing_time;
	
	protected int result; // 누적합 변수
	
	public accumulator()
	{
		this("procQ", 20);
	}

	public accumulator(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		q = new Queue();
		job = new job("", 0, false);
		// job(String name, int _num, boolean _isLast)
		result = 0;
		// 누적합의 초기값은 0
		
		holdIn("passive", INFINITY);
		// passive : 연산에 사용 될 양의 정수를 전송 받는 상태
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					job = (job) x.getValOnPort("in", i);
					// in포트로 들어온 정수를
					
					q.add(job);
					// queue에 저장
				
					holdIn("passive", processing_time);
					// passive : 연산에 사용 될 양의 정수를 전송 받는 상태
				}
			}
		}
	}
	
	public void deltint()
	{
		if (phaseIs("passive"))
		{
			//job = (job) q.getLast();
			//System.out.println(job.isLast);
			
			if (job.isLast) // 마지막 정수이면 (즉, job.isLast = true)
			{
				for(int i=0; i<10; i++) // 10개의 정수
				{
					job = (job) q.removeFirst(); // queue에 있는 정수를 하나 빼서
					System.out.println("job.num=?"+job.num);
					result = result + job.num; // 누적합을 구한다
					
					holdIn("processing", processing_time);
					// processing : 정수를 받아 처리하는 상태
				}
			}
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("processing")) // processing : 정수를 받아 처리하는 상태
		{
			m.add(makeContent("out", new job(Integer.toString(result), result)));
			// 누적합을 out 포트를 통해 user에게 반환한다.
			holdIn("finished", INFINITY);
			// finished : Accumulator Atomic Model의 활동 종료
		}
		
		return m;
	}	
	
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + "queue length: " + q.size()
        + "\n" + "queue itself: " + q.toString();
	}

}



