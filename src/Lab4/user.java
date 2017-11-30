package Lab4;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class user extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
  
	protected Queue queue; // Queue 선언
	// Create Queue 
	
	public user() 
	{
		this("genr", 30);
	}
  
	public user(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		addInport("in");
   
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 1;
		queue = new Queue(); // Queue 생성
		
		// Queue에 정수 추가
		for(int i=1;i<=10;i++)
			queue.add(i);
		
		holdIn("active", int_arr_time);
		// active : 연산에 사용 될 양의 정수를 전송하는 상태
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					holdIn("finished", INFINITY);
					// accumulator로부터 결과 값(누적합)을 받으면 finished 상태로 전이
					// finished : User Atomic Model의 활동 종료
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("active"))
		{
			count = count + 1;
			
			holdIn("active", int_arr_time); 
			// active 상태 유지
			// active : 연산에 사용 될 양의 정수를 전송하는 상태
		}
	}

	public message out()
	{
		message m = new message();

		if(queue.size() > 1) // queue size가 1보다 크면
		{
			int num = (int)queue.removeFirst(); // queue에서 정수 하나를 가져온다
			m.add(makeContent("out", new job(Integer.toString(num), num))); 
			// 가져온 하나의 정수를 out포트를 통해 accumulator로 보낸다. 
		}
		else if(queue.size() == 1) // queue size가 1이면
		{
			int num = (int)queue.removeFirst(); //  queue에서 정수 하나를 가져온다
			m.add(makeContent("out", new job(Integer.toString(num) + ", last", num, true)));
			// job(String name, int _num, boolean _isLast)
			// Integer.toString : string 값 return
			//  flag를 사용하여 마지막 정수임을 알리고, out포트를 통해 마지막 정수를 accumulator로 보낸다. 
			holdIn("passive", INFINITY);
			// passive 상태로 전이한다
			// passive : 전송된 정수들에 대한 연산 결과를 기다리는 상태 
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
