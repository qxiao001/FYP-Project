import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;

public class PosPred extends LinearRegression{

	/**
	 * 
	 */
	private static final long serialVersionUID = 176080314765325769L;
	
	public double classifyInstance(Instance instance) throws Exception{
		
		return Math.abs(super.classifyInstance(instance));
		
	}

}
