
public class Config {
    
    public static final String projectFolder = "C:\\Users\\samsung\\Documents\\GitHub\\FYP-Project\\JoinTable\\";
    
    public static String updateMaxSQLPath = projectFolder + "updateMax.sql";
    public static String updateMinSQLPath = projectFolder + "updateMin.sql";
    public static String indivChilllerSQLPath = projectFolder + "indivChillerJoin.sql";
    public static String combChilllerSQLPath = projectFolder + "combChillerJoin.sql";
    public static String finalChilllerSQLPath = projectFolder + "finalChillerJoin.sql";
    public static String indivSelectSQLPath = projectFolder + "indivSelect.sql" ;
    public static String indivSelect2SQLPath = projectFolder + "indivSelect2.sql" ;
     
    public static String connString = "jdbc:postgresql://132.147.88.190:5433/postgres";
    public static String connString5432 = "jdbc:postgresql://132.147.88.190:5432/ems3";
    public static String connUser = "ecoadm";
    public static String connPassword = "ev093qer"; 
    public static String connUser5432 = "ecoreader";
    public static String connPassword5432 = "EMSreader"; 
    
    public static String getStampSQL = "select * from \"stamps\"";

	public static String getUltraMax ="select ultrasonic_flow_modbus_log_seq from \"Ultrasonic_flow_modbus_log_#yyyy_mm#\" order by ultrasonic_flow_modbus_log_seq desc limit 1";
    public static String getPowerMax = "select meter_log_seq from \"Meter_log_#yyyy_mm#\" order by meter_log_seq desc limit 1";
	

	
  

}