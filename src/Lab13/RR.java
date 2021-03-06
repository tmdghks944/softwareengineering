package Lab13;
import java.awt.*;
import simView.*;

public class RR extends ViewableDigraph
{

	final int NODE = 5;
	final int QUEUE = 5;
	int[] proc_t = {0,350,200,450,300,500,300};//processing_time
	
	public RR()
	{
		super("gpt");
    	
		ViewableAtomic g = new genr("g", 10);
		//어떤 모델을 가져다 쓸건지.
		ViewableAtomic s = new scheduler("sched", 0,NODE);
		ViewableAtomic e = new evaluator("evaluator");
    	
		add(g);
		//시뮬레이터에 모델을 등록.
		add(s);
		add(e);
		
		addCoupling(g, "out", s, "in");
		addCoupling(g,"out", e, "arrive");
		for(int i=1;i<=NODE;i++) {
			ViewableAtomic p = new processor("processor"+i, proc_t[i],QUEUE);
			add(p);
			
			addCoupling(s,"out"+i,p,"in");
			addCoupling(p,"out2",e,"loss");
			addCoupling(p,"out1",e,"solved");
		}
		
	}

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(988, 646);
        ((ViewableComponent)withName("processor4")).setPreferredLocation(new Point(518, 325));
        ((ViewableComponent)withName("processor5")).setPreferredLocation(new Point(526, 400));
        ((ViewableComponent)withName("sched")).setPreferredLocation(new Point(317, 278));
        ((ViewableComponent)withName("processor3")).setPreferredLocation(new Point(528, 246));
        ((ViewableComponent)withName("processor2")).setPreferredLocation(new Point(522, 151));
        ((ViewableComponent)withName("g")).setPreferredLocation(new Point(119, 271));
        ((ViewableComponent)withName("processor1")).setPreferredLocation(new Point(523, 63));
        ((ViewableComponent)withName("evaluator")).setPreferredLocation(new Point(755, 184));
    }
}
