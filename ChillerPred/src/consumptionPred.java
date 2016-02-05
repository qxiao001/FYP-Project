import java.sql.Timestamp;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;


public class consumptionPred {

	public static void main(String[] args) throws Exception {
		// prepare data
		Instances data = DataIO.getDatasetDB("consumption");
		data.setClassIndex(0);
		data.remove(0);
		data.remove(data.size()-1);
		
		Instances data_train=Prediction.removePercentage(data, true, 70);
		Instances data_test=Prediction.removePercentage(data, false, 70);
		
		// build model and output results
		Classifier model = new PosPred();  // PosPred is the model of linear regeression with absolute value
	    model.buildClassifier(data_train); 
	    

		
		Prediction.evaluateModel(data_test, model); 
		Prediction.outputCSV("results_consumption.csv", data_test, model);

		
		

	}

}
