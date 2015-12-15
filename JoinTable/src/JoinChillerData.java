import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Calendar;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JoinChillerData {

	static String ultra_min;
	static String power_min;
	static Timestamp chiller1ts;
	static Timestamp chiller2ts;
	static Timestamp chillercombts;
	static String ultra_max;
	static String power_max;
	static String yyyy_mm="2015_11";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
        getyyyy_mm();
		joinDB("updateMax");
		getStamp();
		integrityCheck();
		joinDB("indiv");
		joinDB("comb");
		joinDB("final");

	}
	
	public static void getyyyy_mm(){
	
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
		yyyy_mm=dateFormat.format(cal.getTime());
	    System.out.println(yyyy_mm);
	}

	public static Connection getDBConn() {
		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(Config.connString,
					Config.connUser, Config.connPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void getStamp() throws Exception {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = getDBConn();
		String query = Config.getStampSQL;
		st = conn.createStatement();
		rs = st.executeQuery(query);

		if (rs.next()) {

			ultra_min = rs.getString(1);
			power_min = rs.getString(2);
			chiller1ts = rs.getTimestamp(3);
			chiller2ts = rs.getTimestamp(4);
			chillercombts = rs.getTimestamp(5);
			ultra_max = rs.getString(6);
			power_max = rs.getString(7);
		}
	}

	private static void integrityCheck() throws Exception {
		// check if max is equal to or less than min. if equal: no new data is updated, if less, jump to a new month alr.
		if (Integer.parseInt(ultra_min)>Integer.parseInt(ultra_max) && Integer.parseInt(power_min)>Integer.parseInt(power_max)){
			//update min to be 0 and 0. 
			System.out.println("we are going to use new month data");
			joinDB("updateMin");
			ultra_min="-1";power_min="-1";
		}
		else if (Integer.parseInt(ultra_min)==Integer.parseInt(ultra_max)|| Integer.parseInt(power_min)==Integer.parseInt(power_max)){
			System.out.println("Opps, there is no new data in database.\n Please wait and try again later.\n Sorry for inconvinience");
			System.exit(1);
		}
	}
	public static void joinDB(String tableName) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = getDBConn();
		String query;
		String queryPath;
		switch (tableName) {
		case "updateMin":
			queryPath=Config.updateMinSQLPath;
			break;
		case "updateMax":
			queryPath = Config.updateMaxSQLPath;
			break;
		case "indiv":
			queryPath = Config.indivChilllerSQLPath;
			break;
		case "comb":
			queryPath = Config.combChilllerSQLPath;
			break;
		case "final":
			queryPath = Config.finalChilllerSQLPath;
			break;
		default:
			queryPath = "";
			break;
		}

		query = readFile(queryPath);
		

		switch (tableName) {
		
		case "updateMax":
			query = query.replace("#yyyy_mm#", yyyy_mm);
            break;
		case "indiv":
			query =query.replace("#ultra_min#", ultra_min); 
			query =query.replace("#power_min#", power_min);
			query =query.replace("#ultra_max#", ultra_max);
			query =query.replace("#power_max#", power_max);
			query = query.replace("#yyyy_mm#", yyyy_mm);
			System.out.println(query);
			break;
		case "comb":
			query =query.replace("#chiller1ts#", chiller1ts.toString());
			query =query.replace("#chiller2ts#", chiller1ts.toString());
			
			break;
		case "final":
			query =query.replace("#chillercombts#", chillercombts.toString());
			break;
		default:
			break;
		}

		st = conn.createStatement();
		st.executeUpdate(query);

	}

	private static String readFile(String path) throws Exception {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

}
