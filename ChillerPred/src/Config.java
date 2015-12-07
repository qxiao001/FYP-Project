public class Config {
    
    public static final String projectFolder = "C:\\Users\\samsung\\Documents\\GitHub\\fyp\\ChillerPred\\";
    
    public static String modelPath = projectFolder + "model.model";
    public static String submFilePath = projectFolder + "subm.csv";
    public static String sqlFilePath = projectFolder + "feature_query.sql";
    public static String arffTrainingPath = projectFolder + "X_Training.arff";
    public static String arffValidationPath = projectFolder + "X_Validation.arff";
    public static String esTempWorkingFolderPath = projectFolder + "es_working\\";
   
     
    public static String connString = "jdbc:postgresql://132.147.88.190:5433/postgres";
    public static String connUser = "ecoadm";
    public static String connPassword = "ev093qer"; 
    
    public static String selectQuery = "select powertotal,conflow1,conflow2,conin1,conin2,conout1,conout2,evaflow1,evaflow2,evain1,evain2,evaout1,evaout2,temp,now from \"chiller_cb2\"";

}