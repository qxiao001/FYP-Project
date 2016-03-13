package com.chillerpredictor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PowerServelet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public PowerServelet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		//String PowerCode = request.getParameter("PowerCode");

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
	

		Gson gson = new Gson();
		out.write(gson.toJson(getInfo()));
		
		

		out.close();

	}

	// Get Power Information
	private JsonArray getInfo() {

		//ArrayList <PowerElmt> powerTrend = new ArrayList <PowerElmt>();
		JsonArray powerTrend = new JsonArray();
		
		DataSource ds = null;
		Connection con = null;
		Statement stmt = null;
		System.out.println("outsiede try");

		try {

			System.out.println("insiede try");
			Context ic = (Context) new InitialContext().lookup("java:comp/env");
            con = ((DataSource) ic.lookup("jdbc/postgresql")).getConnection(); 
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from \"test\" limit 100 ");
		

			while (rs.next()) {
				/*PowerElmt powerElmt = new PowerElmt();
				powerElmt.setTimestamp(rs.getTimestamp("hourstamp"));
				powerElmt.setActual(rs.getDouble("power"));
				powerElmt.setPredicted(rs.getDouble("predall$fit"));
                powerTrend.add(powerElmt);*/
                
                
                JsonObject powerElmt= new JsonObject();
                powerElmt.addProperty("timestamp", rs.getTimestamp("hourstamp").toString());
				powerElmt.addProperty("actual", (Number)rs.getDouble("power"));
				powerElmt.addProperty("predicted", (Number)rs.getDouble("predall$fit"));
                powerTrend.add(powerElmt);
                //System.out.println(powerElmt.toString());
                
			}

			rs.close();
			stmt.close();
			stmt = null;

			con.close();
			con = null;

		} catch (Exception e) {
			System.out.println(e);
		}

		finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlex) {
					// ignore -- as we can't do anything about it here
				}

				stmt = null;
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqlex) {
					// ignore -- as we can't do anything about it here
				}

				con = null;
			}
		}

		return powerTrend;

	}

}