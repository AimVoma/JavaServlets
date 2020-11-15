package com.wtc;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class PropertiesManager {

	static Properties properties = new Properties();
	static String propFile = "config.properties";
	static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFile);

	public static Properties getConfig() throws FileNotFoundException {	
		if (inputStream != null) {
		
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} else {
			throw new FileNotFoundException("Property file '" + propFile + "' not found in the classpath");
		}
		
		return properties;
	}
}