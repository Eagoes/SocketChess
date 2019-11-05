package mychess.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {
	public static String IP;
	
	public static String PORT;

	public static String DBIP;

	public static String DBPORT;

	public static String DBPASSWORD;

	public static String FUNCTIONPORT;
	
	public static void read() {
		// TODO Auto-generated constructor stub
		Properties properties=new Properties();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File("src/main/resources/conf/chess.properties"));
			properties.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		IP=properties.getProperty("chess.ip");
		PORT=properties.getProperty("chess.port");
		DBIP=properties.getProperty("db.ip");
		DBPORT=properties.getProperty("db.port");
		DBPASSWORD=properties.getProperty("db.password");
		FUNCTIONPORT=properties.getProperty("server.port");
	}
}
