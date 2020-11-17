package com.wtc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet implementation class ManageDBServlet
 */
@WebServlet("/ManageDBServlet")
public class ManageDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageDBServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Properties properties = PropertiesManager.getConfig();
		final String STATIC_FILE_DIR = properties.getProperty("STATIC_FILE_DIR");
		final String FILE_NAME = properties.getProperty("FILE_NAME");

		H2Utils._initialize(); H2Utils._flush();
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
		        Client client = objectMapper.readValue(jsonEntry.toJSONString(), Client.class);
		        H2Utils.insertTableSql("clients", client);
		    }
		    	    		    
			JSONArray orders = (JSONArray) JsonObject.get("orders");
		    for (int i = 0; i < orders.size(); i++)
		    {
		        JSONObject jsonEntry = (JSONObject) orders.get(i);
		        Order order = objectMapper.readValue(jsonEntry.toJSONString(), Order.class);
		        H2Utils.insertTableSql("orders", order);
		    }
		} catch (FileNotFoundException fnf) {
			logger.log(Level.WARNING, fnf.getMessage());
		} catch (IOException io) {
			logger.log(Level.WARNING, io.getMessage());
		} catch (ParseException pe) {
			logger.log(Level.WARNING, pe.getMessage());
		}

		List<Map> dbList = new ArrayList<Map>();

		H2Utils.prinTableSql("clients");
		H2Utils.prinTableSql("orders");
		H2Utils.joinTables("orders", "clients");
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
