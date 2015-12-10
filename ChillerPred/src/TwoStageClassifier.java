import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;


public class TwoStageClassifier implements Classifier {
	private static final long serialVersionUID = -8996052328536347834L;
	Classifier modelOn;
	Classifier modelOff;
	Classifier classifyOnOff; 

    public TwoStageClassifier(Classifier modelOn, Classifier modelOff, Classifier classifyOnOff){
    	this.modelOn=modelOn;
    	this.modelOff=modelOff;
    	this.classifyOnOff=classifyOnOff;
    }
	
	@Override
	public double classifyInstance(Instance instance) throws Exception {
		double power=0;
		
		if (classifyOnOff.classifyInstance(instance)==0)
			power = modelOff.classifyInstance(instance);
		else power = modelOn.classifyInstance(instance);
		return power;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
    
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("twoStageClassifier\n");
		buffer.append("-----------------\n");
		return buffer.toString();
	}

	@Override
	public void buildClassifier(Instances arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] distributionForInstance(Instance arg0) throws Exception {
		// TODO Auto-generated method stub
		double[] a= {1,2}; 
		return a;
	}

	@Override
	public Capabilities getCapabilities() {
		// TODO Auto-generated method stub
		return null;
	}
}
