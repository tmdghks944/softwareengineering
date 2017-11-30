package Lab3;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class user extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
  
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
		
		holdIn("active", int_arr_time);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("stop"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					 //holdIn("stop", INFINITY);
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("active"))
		{
			count = count + 1;

			holdIn("stop", INFINITY);

			// holdIn("active", int_arr_time);
		}
	}

	public message out()
	{
		message m = new message();
		
		m.add(makeContent("out", new addRequest("1 + 2 = ?", 1, 2)));
		
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
