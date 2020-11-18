package com.wtc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * Servlet implementation class ManageDBServlet.
 */
/**
 * @author aimilios
 *
 */

@WebServlet("/ManageDBServlet")
public class ManageDBServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
       
    /**
     * Instantiates a new manage DB servlet.
     *
     * @see HttpServlet#HttpServlet()
     */
    public ManageDBServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Main Servlet GET method to process the uploaded static file and to create JSON object mapper
	 * to instantiate Database Tables Entry Objects(Clients, Orders, Transactions). Since the tables
	 * and entries are sucesfully registered, the Servlet performs JOIN operations between tables
	 * to create TRANSACTIONS TABLE.
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
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
			
			// Retrieve Clients as JsonArray and parse one by one
			JSONArray clients = (JSONArray) JsonObject.get("clients");
		    for (int i = 0; i < clients.size(); i++)
		    {
		        JSONObject jsonEntry = (JSONObject) clients.get(i);
		        Client client = objectMapper.readValue(jsonEntry.toJSONString(), Client.class);
		        H2Utils.insertTableSql("clients", client);
		    }
		    
		    // Retrieve Orders as JsonArray and parse one by one
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
		} catch (ClassCastException cc) {
			logger.log(Level.WARNING, cc.getMessage());
		} catch (UpdateTableEntryException utee) {
			logger.log(Level.WARNING, utee.getMessage());
		} 
		
		H2Utils.joinTables("orders", "clients");
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
}
