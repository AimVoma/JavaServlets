package com.wtc;

import com.wtc.PropertiesManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.internal.compiler.ast.Clinit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;


class Cat extends Object{}

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {		
		
		
		Properties properties = PropertiesManager.getConfig();
		H2Utils._initialize();
		
		final String STATIC_FILE_DIR = properties.getProperty("STATIC_FILE_DIR");
		final String FILE_NAME = properties.getProperty("FILE_NAME");
		
//		H2Utils.createTable("clients");
		Client client = new Client();
		Client client1 = new Client();

		client.setFirstName("AIM");
		client.setLastName("Vomits");
		client.setEmail("am@voma");
		client.setStreet("VH CITY");
		
		client1.setFirstName("AIM1");
		client1.setLastName("Vomits2");
		client1.setEmail("am@voma12");
		client1.setStreet("VH CITY12");
		
		Order order1 = new Order();
		Order order2 = new Order();
		
		order1.setUser("AIM");	
		order1.setProduct("MAKITA");
		order1.setDate(new Date(0));
		
		order2.setUser("AIM");	
		order2.setProduct("MAKITA");
		order2.setDate(new Date(1));
		
		H2Utils.dropTableSql("clients");
		H2Utils.dropTableSql("orders");
				
		H2Utils.createTable("clients");	
		H2Utils.insertTableSql("clients", client);
		H2Utils.insertTableSql("clients", client1);
		
		H2Utils.createTable("orders");	
		H2Utils.insertTableSql("orders", order1);
		H2Utils.insertTableSql("orders", order2);
		
		H2Utils.prinTableSql("orders");
		
		
//		H2Utils.joinTables();
		H2Utils.joinTables("orders", "clients");
		System.exit(0);

		
		File file = new File(STATIC_FILE_DIR + FILE_NAME); 
		// assumes the current class is called MyLogger
		
		Logger logger = Logger.getLogger("ServletLogger");		
		ObjectMapper objectMapper = new ObjectMapper();
	
		try {
			JSONObject JsonObject = (JSONObject) new JSONParser().parse(new FileReader(file));

			logger.log(Level.INFO, "Parsing Json File ... ");
			JSONArray clients = (JSONArray) JsonObject.get("clients");

		    for (int i = 0; i < clients.size(); i++)
		    {
		        JSONObject jsonEntry = (JSONObject) clients.get(i);
		        Client client11 = objectMapper.readValue(jsonEntry.toJSONString(), Client.class);
		        
		        System.out.println(client11.getFirstName());
		        System.out.println(client11.getLastName());
		        System.out.println(client11.getEmail());

		    }
		    	    		    
			JSONArray orders = (JSONArray) JsonObject.get("orders");
		    for (int i = 0; i < orders.size(); i++)
		    {
		        JSONObject jsonEntry = (JSONObject) orders.get(i);
		        Order order = objectMapper.readValue(jsonEntry.toJSONString(), Order.class);
		        
		        System.out.println(order.getUser());
		        System.out.println(order.getProduct());
		        System.out.println(order.getDate());
		    }
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}