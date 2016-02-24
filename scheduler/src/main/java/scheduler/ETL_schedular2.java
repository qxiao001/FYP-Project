package scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

@Stateless(name = "AutomaticSchedulerBean")
public class ETL_schedular2 {
	@Schedule(dayOfWeek = "*", hour = "*", minute = "59", second = "0", year = "*", persistent = false)
	public void backgroundProcessing() {
		try {
			System.out.println("running join program: ");
			(new JoinChillerData2()).executeJoin();
			/*Process proc = Runtime.getRuntime().exec("java -jar \"C:/Users/samsung/Google Drive/FYP/Implementation/Jar/joinTable.jar\"");
		    proc.waitFor();
		    // Then retreive the process output
		    InputStream in = proc.getInputStream();
		    InputStream err = proc.getErrorStream();

		    byte b[]=new byte[in.available()];
		    in.read(b,0,b.length);
		    System.out.println(new String(b));

		    byte c[]=new byte[err.available()];
		    err.read(c,0,c.length);
		    System.out.println(new String(c));*/

			
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
}
