import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExtractRawData {
	 public static void main(String[] args) {

	        Connection con = null;
	        Connection localcon = null;
	        Statement st = null;
	        ResultSet rs = null;
	        Statement localst = null;
	        ResultSet localrs = null;

	    
	        String url = "jdbc:postgresql://132.147.88.190:5433/postgres";
	        String user = "ecoadm";
	        String password = "ev093qer";
	  
	        

	        try {
	        	
	        	 //connect to remote server database
	            con = DriverManager.getConnection(url, user, password);
	            st = con.createStatement();
	            
	        
	            
	            String query= "SELECT \"Chiller#4_CHWR_Temp\".\"tube_temp\",\"Chiller#4_CHWS_Temp\".\"tube_temp\",\"Chiller#4_CHWR_Temp\".\"time_stamp\",\"Chiller#4_CHWS_Temp\".\"time_stamp\",(\"Chiller#4_CHWR_Temp\".\"time_stamp\" - \"Chiller#4_CHWS_Temp\".\"time_stamp\") AS \"time_diff\" FROM \"Chiller#4_CHWR_Temp\" , \"Chiller#4_CHWS_Temp\" WHERE  ((\"Chiller#4_CHWR_Temp\".\"time_stamp\" - \"Chiller#4_CHWS_Temp\".\"time_stamp\") < INTERVAL \'30 seconds\' AND ((\"Chiller#4_CHWS_Temp\".\"time_stamp\" - \"Chiller#4_CHWR_Temp\".\"time_stamp\") < INTERVAL \'30 seconds\')) LIMIT 10;";
	            rs = st.executeQuery(query);
	            
	            String queryUpdate = " ";
	            st.executeUpdate(queryUpdate);
	          
	          
	        } catch (SQLException ex) {
	            Logger lgr = Logger.getLogger(ExtractRawData.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);

	        } finally {
	            try {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (st != null) {
	                    st.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	                

	            } catch (SQLException ex) {
	                Logger lgr = Logger.getLogger(ExtractRawData.class.getName());
	                lgr.log(Level.WARNING, ex.getMessage(), ex);
	            }
	        }
	 }

}
