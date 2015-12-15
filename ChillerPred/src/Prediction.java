import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.trees.NBTree;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Prediction {

	public static void main(String[] argv) throws Exception {
		//
		// prediction start
		//Instances data = DataIO.getDatasetDB("all");
		//data.setClassIndex(0);
		Instances data1 = DataIO.getDatasetDB("all");
		data1.setClassIndex(0);
		//clean data
		cleanNegativeValues(data1,1,4);
		/*Instances data2 = DataIO.getDatasetDB("chiller2");
		data2.setClassIndex(0);
		Instances dataTree = new Instances(data1);
		dataTree.setClassIndex(0);*/
		
		/*// develop costMatrix
					CostMatrix cm = new CostMatrix(2);
					cm.initialize();
					cm.setElement(0,1,10);
					cm.setElement(1,0,1);*/
		
		
		Instances data1_train=removePercentage(data1, true, 50);
		Instances data1_test=removePercentage(data1, false, 50);
		Instances dataTree = new Instances(data1_train);
		dataTree.setClassIndex(0);
		dataTree=powerToBinary(dataTree);
		System.out.println(dataTree.attributeStats(0));

		Classifier model = new LinearRegression();
		Classifier modelOn = new LinearRegression();
		Classifier modelOff = new LinearRegression();
		Classifier classifyOnOff = new NBTree();
		

		dataTree=overSampling(dataTree);
		classifyOnOff.buildClassifier(dataTree);
		evaluateModel(dataTree,classifyOnOff);
		dataSeperation(data1_train, modelOn, modelOff);
		TwoStageClassifier myClassifier=new TwoStageClassifier(modelOn, modelOff,classifyOnOff);
		
		System.out.println(data1_train.size()+ "test:" +data1_test.size() + " "  + myClassifier.classifyInstance(data1_train.instance(0)));
        evaluateModel(data1_test,myClassifier);
		// prediction end

		outputCSV("results_all.csv", data1_test,myClassifier);

		/*
		 * plot start//
		 * //System.out.println(data.attributeToDoubleArray(0).length);
		 * SwingPlot sp=new SwingPlot(data); sp.paint(); //plot end
		 */

	}

	public static Instances powerToBinary(Instances data) throws Exception {
		// 1 for on, 0 for off
		for (int i=0;i<data.size();i++){
			if(data.instance(i).value(0)<1000){
				data.instance(i).setValue(0, 0);
			}else data.instance(i).setValue(0, 1);
			
		}
		NumericToNominal numToNom = new NumericToNominal();
		numToNom.setInputFormat(data);
		numToNom.setOptions(weka.core.Utils.splitOptions("-R first"));
        return Filter.useFilter(data, numToNom);
        
	}
	public static Instances overSampling(Instances data){
		Instances dataOver=new Instances(data);
		for (int i=0;i<data.size();i++){
			if (data.instance(i).value(0)==1){
				for(int j=0; j<6; j++){
				   dataOver.add(data.instance(i));
				}
			}
		}
		return dataOver;
	}
	public static Instances removePercentage(Instances data, boolean invert, int percent) throws Exception{
		RemovePercentage rp=new RemovePercentage();
		rp.setInputFormat(data);
		String in=(invert)? " -V ":" ";
		rp.setOptions(weka.core.Utils.splitOptions("-P "+percent+in));
        return Filter.useFilter(data, rp);
	}

	public static void outputCSV(String filename, Instances data,
			Classifier model) throws Exception {

		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(filename));
		double[] x = data.attributeToDoubleArray(0);
		for (int i = 0; i < x.length; i++) {
			// Maybe:
			outputWriter
					.write(x[i] + ";" + model.classifyInstance(data.get(i)));
			// Or:
			// outputWriter.write(Double.toString(x[i]);
			outputWriter.newLine();
		}
		outputWriter.flush();
		outputWriter.close();

	}

	public static void dataSeperation(Instances data, Classifier modelOn,
			Classifier modelOff) throws Exception {
		Instances dataOn = new Instances(data);
		dataOn.clear();
		data.setClassIndex(0);
		Instances dataOff = new Instances(data);
		dataOff.clear();
		data.setClassIndex(0);
		int num = data.size();
		for (int i = 0; i < num; i++) {

			if (data.instance(i).value(0) < 1000) {
				dataOff.add(data.instance(i));
			} else
				dataOn.add(data.instance(i));
		}

		System.out.println("the size of off " + dataOff.size() + " on:"
				+ dataOn.size());

		modelOn.buildClassifier(dataOn);
		modelOff.buildClassifier(dataOff);

		evaluateModel(dataOn, modelOn);
		evaluateModel(dataOff, modelOff);
		// outputCSV("results_off_seperation.csv",dataOff,modelOff);

	}

	public static Instances cleanNegativeValues(Instances data,int flow1, int flow2) {
		for (int i = 0; i < data.size(); i++) {
			if (data.instance(i).value(flow1) < 0) {
				data.instance(i).setValue(1, 0);
			}
			if (data.instance(i).value(flow2) < 0) {
				data.instance(i).setValue(4, 0);
			}
		}
		return data;
	}

	public static void evaluateModel(Instances data, Classifier model)
			throws Exception {
		Evaluation eva = new Evaluation(data);
		eva.evaluateModel(model, data);
		System.out.println(model.toString()+eva.toSummaryString());
		
	}
	
}
