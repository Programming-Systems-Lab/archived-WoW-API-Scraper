package edu.columbia.cs.psl.scrape.wow;

import java.sql.Connection;
import java.sql.DriverManager;


public class DBUtils {
	public static Connection conn = null;
	public static void initDB()
	{
        try
        {
//            String url = "jdbc:sqlite:"+filename;
//            Class.forName ("org.sqlite.JDBC").newInstance ();
//            conn = DriverManager.getConnection (url);
        	String userName = "wow";
            String password = "wow";
            String url = "jdbc:mysql://127.0.0.1/wow";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server");
        }

	}

}
