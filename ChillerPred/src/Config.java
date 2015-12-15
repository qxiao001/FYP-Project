public class Config {
    
    public static final String projectFolder = "C:\\Users\\samsung\\Documents\\GitHub\\fyp\\ChillerPred\\";
    
    public static String modelPath = projectFolder + "model.model";
    public static String submFilePath = projectFolder + "subm.csv";
    public static String sqlFilePath = projectFolder + "feature_query.sql";
    public static String arffTrainingPath = projectFolder + "X_Training.arff";
    public static String arffValidationPath = projectFolder + "X_Validation.arff";
    public static String esTempWorkingFolderPath = projectFolder + "es_working\\";
    
    public static String indivChilllerSQLPath = projectFolder + "indivChillerJoin.sql";
    public static String combChilllerSQLPath = projectFolder + "combChillerJoin.sql";
    public static String finalChilllerSQLPath = projectFolder + "finalChillerJoin";
     
    public static String connString = "jdbc:postgresql://132.147.88.190:5433/postgres";
    public static String connUser = "ecoadm";
    public static String connPassword = "ev093qer"; 
    
    public static String selectAll = "select consumptiontotal,conflow1,conflow2,conin1,conin2,conout1,conout2,evaflow1,evaflow2,evain1,evain2,evaout1,evaout2,temp,now from \"chiller_cbp2\"";
    public static String selectChiller1 = "select power1,conflow1,conin1,conout1,evaflow1,evain1,evaout1,temp,now from \"chiller_cb2\"";
    public static String selectChiller2 = "select power2,conflow2,conin2,conout2,evaflow2,evain2,evaout2,temp,now from \"chiller_cb2\"";
    public static String selectConsumption = "select avg(powertotal) as power,avg(conflow1) as conflow1 ,avg(conflow2) as conflow2 ,avg(conin1) as conin1, avg(conin2) as conin2, avg(conout1) as conout1 , avg(conout2) as conout2,avg(evaflow1) as evaflow1, avg(evaflow2) as evaflow2, avg(evain1) as evain1, avg(evain2) as evain2, avg(evaout1) as evaout1, avg(evaout2) as evaout2, avg(temp) as temp, avg(now) as rain from \"chiller_cbp2\" group by hourstamp";


}