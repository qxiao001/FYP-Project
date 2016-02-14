
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Calendar;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
	static String yyyy_mm = "2015_11";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		getyyyy_mm();
		getMax5432();
		joinDB("updateMax");
		getStamp();
		integrityCheck();
		ResultSet rs1 = joinDB("indivSelect");
		ResultSet rs2 = joinDB("indivSelect2");
		insertResult(rs1, "chiller1pt");
		insertResult(rs2, "chiller2pt");
		 joinDB("comb");
		 joinDB("final");
		// callR();

	}

	private static void getMax5432() throws SQLException {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = getDBConn(Config.connString5432, Config.connUser,
				Config.connPassword);
		String query = Config.getUltraMax;
		query = query.replace("#yyyy_mm#", yyyy_mm);
		st = conn.createStatement();
		rs = st.executeQuery(query);

		if (rs.next())
			ultra_max = rs.getString(1);

		query = Config.getPowerMax;
		query = query.replace("#yyyy_mm#", yyyy_mm);
		st = conn.createStatement();
		rs = st.executeQuery(query);
		if (rs.next())
			power_max = rs.getString(1);
		System.out.println(ultra_max + "," + power_max);
	}

	private static void insertResult(ResultSet rs, String table)
			throws SQLException {

		Connection conn = getDBConn(Config.connString, Config.connUser,
				Config.connPassword);
		Statement st = conn.createStatement();

		String values = "";
		String sql = "INSERT INTO " + table + " VALUES ";
		String sep = ", ";

		while (rs.next()) {

			values = values + " (";
			values = values + "\'" + rs.getString(1) + "\'" + sep + "\'"
					+ rs.getTimestamp(2) + "\'" + sep + "\'"
					+ rs.getTimestamp(3) + "\'" + sep + "\'"
					+ rs.getTimestamp(4) + "\'" + sep + rs.getFloat(5) + sep
					+ rs.getFloat(6) + sep + rs.getFloat(7) + sep
					+ rs.getFloat(8) + sep + rs.getFloat(9) + sep
					+ rs.getFloat(10) + sep + rs.getDouble(11) + sep
					+ rs.getFloat(12) + "),";

		}
		values = values.substring(0, values.length() - 1);
		values = values + ";";
		sql = sql + values;
		System.out.println("string of insert is :" + sql);
		st.executeUpdate(sql);
	}

	private static void callR() {
		// TODO Auto-generated method stub
		/*
		 * Rengine re = new JRIEngine(new String[] { "--no-save" }, new
		 * RCallback(), false); re.parseAndEval("source(\"/scriptname.R\")");
		 * re.close()
		 */
		BufferedReader reader = null;
		Process shell = null;
		try {
			shell = Runtime.getRuntime().exec(
					new String[] { "C:/Program Files/R/R-3.2.2/bin/Rscript",
							"regression.R" });

			reader = new BufferedReader(new InputStreamReader(
					shell.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getyyyy_mm() {

		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
		yyyy_mm = dateFormat.format(cal.getTime());
		System.out.println(yyyy_mm);
		
	}

	public static Connection getDBConn(String conn, String user, String pwd) {
		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(conn, user, pwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void getStamp() throws Exception {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = getDBConn(Config.connString, Config.connUser,
				Config.connPassword);
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
			// System.out.println(ultra_min);
		}
	}

	private static void integrityCheck() throws Exception {
		// check if max is equal to or less than min. if equal: no new data is
		// updated, if less, jump to a new month alr.
		if (Integer.parseInt(ultra_min) > Integer.parseInt(ultra_max)
				&& Integer.parseInt(power_min) > Integer.parseInt(power_max)) {
			// update min to be 0 and 0.
			System.out.println("we are going to use new month data");
			joinDB("updateMin");
			ultra_min = "-1";
			power_min = "-1";
		} else if (Integer.parseInt(ultra_min) == Integer.parseInt(ultra_max)
				|| Integer.parseInt(power_min) == Integer.parseInt(power_max)) {
			System.out
					.println("Opps, there is no new data in database.\n Please wait and try again later.\n Sorry for inconvinience");
			callR();
			System.exit(1);

		}
	}

	public static ResultSet joinDB(String tableName) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		Connection conn = null;
		if (tableName.contains("Select")) {
			conn = getDBConn(Config.connString5432, Config.connUser,
					Config.connPassword);

		} else {
			conn = getDBConn(Config.connString, Config.connUser,
					Config.connPassword);
		}
		String query;
		String queryPath;
		switch (tableName) {
		case "updateMin":
			queryPath = Config.updateMinSQLPath;
			break;
		case "updateMax":
			queryPath = Config.updateMaxSQLPath;
			break;
		case "indivSelect":
			queryPath = Config.indivSelectSQLPath;
			break;
		case "indivSelect2":
			queryPath = Config.indivSelect2SQLPath;
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
			query = query.replace("#ultra_max#", ultra_max);
			query = query.replace("#power_max#", power_max);
			query = query.replace("#yyyy_mm#", yyyy_mm);
			break;
		case "indivSelect":
		case "indivSelect2":
			query = query.replace("#yyyy_mm#", yyyy_mm);
			query = query.replace("#ultra_min#", ultra_min);
			query = query.replace("#power_min#", power_min);
			query = query.replace("#ultra_max#", ultra_max);
			query = query.replace("#power_max#", power_max);

			System.out.println(query);
			break;
		case "comb":
			query = query.replace("#chiller1ts#", chiller1ts.toString());
			query = query.replace("#chiller2ts#", chiller1ts.toString());

			break;
		case "final":
			query = query.replace("#yyyy_mm#", yyyy_mm);
			query = query.replace("#chillercombts#", chillercombts.toString());
			break;
		default:
			break;
		}

		st = conn.createStatement();

		if (tableName.contains("Select")) {
			rs = st.executeQuery(query);
			return rs;
		} else {
			st.executeUpdate(query);
			return null;
		}

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
