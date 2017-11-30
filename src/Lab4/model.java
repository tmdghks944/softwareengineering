package Lab4;
import java.awt.*;
import simView.*;

public class model extends ViewableDigraph
{
	public model()
	{
		super("gpt");
    	
		ViewableAtomic g = new user("user", 10);
		//어떤 모델을 가져다 쓸건지.
		ViewableAtomic p = new accumulator("accumulator", 10);
    	
		add(g);
		//시뮬레이터에 모델을 등록.
		add(p);
  
		//gpt모델을 커플링 시킨 코드.
		addCoupling(g, "out", p, "in");//user의 out포트와 calc의 in을 커플링
		addCoupling(p, "out", g, "in");//calc의 out포트와 user의 in을 커플링
	}

    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
   
//    preferredSize = new Dimension(988, 646);
//    ((ViewableComponent)withName("t")).setPreferredLocation(new Point(639, 117));
//    ((ViewableComponent)withName("p")).setPreferredLocation(new Point(465, 476));
//    ((ViewableComponent)withName("g")).setPreferredLocation(new Point(59, 148));
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(938, 597);
        ((ViewableComponent)withName("user")).setPreferredLocation(new Point(364, 133));
        ((ViewableComponent)withName("accumulator")).setPreferredLocation(new Point(366, 416));
    }
}
