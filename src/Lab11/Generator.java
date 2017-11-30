package Lab11;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class Generator extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
	
	
	public Generator()
	{
		this("genr", 30);
	}
  
	public Generator(String name, double Int_arr_time)
	{
		super(name);
   
		addOutport("out");
		//out이라는 output port를 생성
		addInport("in");
		//in이라는 input port를 생성.
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 0;
		
		holdIn("active", int_arr_time);
		
	}
  
	public void deltext(double e, message x)
	//외부에서 메세지가 들어왔을 때
	{
		Continue(e);
		if (phaseIs("active"))
		//들어온 메세지의 상태가 active라면
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				//메세지 x가 in포트로 들어왔다면
				{
				}
			}
		}
	}

	public void deltint()
	//내부에서 메세지가 나갈 때
	{
		if (phaseIs("active"))
		{
			count = count + 1;
			
			if(count==5) //5개의 JOB을 전송하였으면
			{
				holdIn("stop", INFINITY);
				//GENERATOR의 모델 중지.
			}
		}
	}

	public message out()
	//다른모델로 메세지를 보낼 때
	{
		message m = new message();
		//메세지를 생성하고
		m.add(makeContent("out", new job("job" + count)));
		//makeContent : 생성한 개체를 등록, new entity : 개체를 생성.
		//out의 이름을 가진 포트로 개체를 전송.
		return m;
	}
  
	public String getTooltipText()
	//커서 가져다 댔을 때 나오는 정보를 출력.
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

	/*
	 * entity와 message의 차이
	 * entity : 이름만 가지는 객체 -> 택배의 내용물.
	 * message : 모델끼리 entity를 주고받을 때 필요한 정보(포트 이름)등을 담은 객체 -> 택배 박스
	 * */
}
