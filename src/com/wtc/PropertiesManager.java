package com.wtc;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * The Class PropertiesManager which is responsible for statically defined
 * properties and key configs.
 */
public class PropertiesManager {

	/** The properties. */
	static Properties properties = new Properties();
	
	/** The prop file. */
	static String propFile = "config.properties";
	
	/** The input stream. */
	static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFile);
	
	/** The logger. */
	static Logger logger = Logger.getLogger("ServletLogger");
	
	/**
	 * Gets the properties persistence manager and send it 
	 * to different parts of the application, where needed
	 * @return java.util.Properties
	 * @throws FileNotFoundException the file not found exception
	 */
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