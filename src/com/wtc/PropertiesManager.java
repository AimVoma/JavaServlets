package com.wtc;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class PropertiesManager {

	static Properties properties = new Properties();
	static String propFile = "config.properties";
	static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFile);
	static Logger logger = Logger.getLogger("ServletLogger");
	
	public static Properties getConfig() throws FileNotFoundException {	
		if (inputStream != null) {
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
		} else {
			throw new FileNotFoundException("Property file '" + propFile + "' not found in the classpath");
		}
		
		return properties;
	}
}