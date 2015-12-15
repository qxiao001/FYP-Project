public class Config {
    
    public static final String projectFolder = "C:\\Users\\samsung\\Documents\\GitHub\\FYP-Project\\JoinTable\\";
    
    public static String updateMaxSQLPath = projectFolder + "updateMax.sql";
    public static String indivChilllerSQLPath = projectFolder + "indivChillerJoin.sql";
    public static String combChilllerSQLPath = projectFolder + "combChillerJoin.sql";
    public static String finalChilllerSQLPath = projectFolder + "finalChillerJoin.sql";
    
     
    public static String connString = "jdbc:postgresql://132.147.88.190:5433/postgres";
    public static String connUser = "ecoadm";
    public static String connPassword = "ev093qer"; 
    
    public static String getStampSQL = "select * from \"stamps\"";
  

}