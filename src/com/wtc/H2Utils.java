package com.wtc;

import java.io.FileNotFoundException;
/**
 * java sql imports
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * java util imports
 */
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * fasterxml jackson imports
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * The Class H2Utils is a utility class to manage all the necessary H2 Database operations.
 */
final public class H2Utils {
	
    /** The url of H2 testing In-Memory Database */
    private static String URL = null;
    
    /** The Username of H2 testing In-Memory Database  */
    private static String Username = null;
    
    /** The Password of H2 testing In-Memory Database  */
    private static String Password = null;
	
	/** The logger, logging INFO and WARNING logging messages */
	private static Logger logger = Logger.getLogger("ServletLogger");
	
	/** The initialized boolean value of H2 Database */
	private static boolean _initialized = false;
    
    /** The Constant CREATE TABLE CLIENTS sql */
    private static final String createTableClientsSQL = "create table if not exists clients (\r\n" + "  id identity primary key,\r\n" +
            "  name varchar(20),\r\n" + "  sname varchar(20),\r\n" + "  email varchar(20),\r\n" +
            "  street varchar(20)\r\n" + "  );";
    
    /** The Constant CREATE TABLE ORDERS sql */
    private static final String createTableOrdersSQL = "create table if not exists orders (\r\n" + "  id identity primary key,\r\n" +
            "  user varchar(20),\r\n" + "  product varchar(20),\r\n" + "  date varchar(20),\r\n" +
            "  );";
    
    /** The Constant CREATE TABLE TRANSACTIONS sql */
    private static final String createTableTransactionsSQL = "create table if not exists transactions (\r\n" + "  id identity primary key,\r\n" +
            "  name varchar(20),\r\n" + "  sname varchar(20),\r\n" + "  email varchar(20),\r\n" +
            "  street varchar(20),\r\n" + "  product varchar(20),\r\n" + "  date varchar(20)\r\n" +  
            "  );";
    
    /** The Constant INSERT INTO CLIENTS sql */
    private static final String INSERT_CLIENTS_SQL = "INSERT INTO clients" + 
    		"  (name, sname, email, street) VALUES " +
    		 " (?, ?, ?, ?);";
    
    /** The Constant INSERT INTO ORDERS sql */
    private static final String INSERT_ORDERS_SQL = "INSERT INTO orders" + 
    		"  (user, product, date) VALUES " +
    		 " (?, ?, ?);";
    
    /** The Constant INSERT INTO TRANSACTIONS sql */
    private static final String INSERT_TRANSACTIONS_SQL = "INSERT INTO transactions" + 
    		"  (name, sname, email, street, product, date) VALUES " +
    		 " (?, ?, ?, ?, ?, ?);";

	/**
	 * Initialize H2 DB & Set _initialized boolean state
	 */
	public static void _initialize() {
		Properties properties;
		try {
			properties = PropertiesManager.getConfig();
			URL = properties.getProperty("DB_URL");
			Username = properties.getProperty("DB_USERNAME");
			Password = properties.getProperty("DB_PASSWORD");
			_initialized = true;
		} catch (FileNotFoundException e) {
			logger.log(Level.WARNING, e.getMessage());
		}

		logger.log(Level.INFO, "H2 Database Initialized!");
	}
	
	/**
	 * Flush H2 DB If needed(Tables, clients-orders-transactions)
	 */
	public static void _flush() {
		/**
		 * Flush the H2 DB tables, if exist
		 * */
		logger.log(Level.INFO, "H2 Database Flushing!");
		dropTableSql("clients");
		dropTableSql("orders");
		dropTableSql("transactions");
		
		/**
		 * Recreate H2 Database Tables
		 * */
		H2Utils.createTable("clients");	
		H2Utils.createTable("orders");	
		H2Utils.createTable("transactions");
	}
	
	/**
	 * Return the initialization status of H2.
	 *
	 * @return the status
	 */
	public static boolean getStatus() {
		return _initialized;
	}
	
    /**
     * Gets the connection.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(URL, Username, Password);
        } catch (SQLException e) {
        	printSQLException(e);
        } catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, e.getMessage());
		}
        return connection;
    }
    
    /**
     * Creates TABLE in H2 Database.
     *
     * @param tableName the table name
     */
    public static void createTable(String tableName) 
    {
    	String createTableSQL = null;
    	    	
    	try(Connection connection = getConnection();
    		Statement statement = connection.createStatement();){
        	switch (tableName) {
    		case "clients":
    			createTableSQL = createTableClientsSQL;
    			break;
    		case "orders":
    			createTableSQL = createTableOrdersSQL;
    			break;
    		case "transactions":
    			createTableSQL = createTableTransactionsSQL;
    			break;
    		default:
    			logger.log(Level.WARNING, String.format("createTable Tablename: %s does not exist, resulting in SQL exception", tableName));
    			break;
    		}
    		statement.execute(createTableSQL);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    
    /**
     * Drops TABLE sql in H2 Database.
     *
     * @param tableName the table name
     */
    public static void dropTableSql(String tableName)
    {
    	final String DROP_TABLE_NAME = String.format("drop table if exists %s cascade", tableName);    	
    	
    	System.err.println("ERROR: " + tableName);
    	try(Connection connection = getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE_NAME);){
    		logger.log(Level.INFO, "Dropping Table " + tableName + " ...");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    
    /**
     * JOINs TABLES ORDERS & CLIENTS, while mapping the resulted JOIN Query set 
     * to Transaction object through JSONObject mapper. And inserting new Transaction 
     * entry in H2 database.
     * @param tableOrders the table orders
     * @param tableClients the table clients
     */
    public static void joinTables(String tableOrders, String tableClients) 
    {
    	String COLUMN_NAME = null;
    	String COLUMN_CONTENT = null;
    	
    	final String JoinQuery = String.format(
    							"select name, sname, street, email, product, date\n"
				    			+ "FROM %s AS or\n"
				    			+ "JOIN %s AS cl\n"
				    			+ "ON or.user = cl.name", tableOrders, tableClients
				    			);

    	try(Connection connection = getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(JoinQuery);
        	ResultSet rs = preparedStatement.executeQuery();)
    	{
    		ObjectMapper objectMapper = new ObjectMapper();
    		JSONObject JsonObj = null;
    		//Processing the ResultSet object for COLUMN names and entries
            while (rs.next()) {
            	ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();
                
                JsonObj = new JSONObject();
                for (int i=1; i<=columnCount; i++) 
                {
                	COLUMN_NAME = rs.getMetaData().getColumnName(i);
                	COLUMN_CONTENT = (String) rs.getObject(i);
                    System.out.println(COLUMN_NAME + " : " +  COLUMN_CONTENT);
                    JsonObj.put(COLUMN_NAME.toLowerCase(), COLUMN_CONTENT);
                }
                Transaction transaction = objectMapper.readValue(JsonObj.toJSONString(), Transaction.class);                
		        H2Utils.insertTableSql("transactions", transaction);
            }
        } catch (SQLException e) {
        	printSQLException(e);
        } catch (JsonMappingException jme) {
        	logger.log(Level.WARNING, jme.getMessage());
		} catch (JsonProcessingException jpe) {
			logger.log(Level.WARNING, jpe.getMessage());
		}
    }
    
    /**
     * Inserts on demand a new entry into one of the TABLEs(CLIENTS, ORDERS, TRANSACTIONS)
     * in H2 database.
     * @param tableName the table name
     * @param obj the obj
     * @throws ClassCastException the class cast exception
     * @throws UpdateTableEntryException the update table entry exception
     */
    public static void insertTableSql(String tableName, Object obj) throws ClassCastException, UpdateTableEntryException
    {
    	String _type = null, insertTableSQLString = null;
    	Client client = null;
    	Order order = null;
    	Transaction transaction = null;
    	ResultSet rs = null;
    	int candidateId = 0;
    	
    	switch (tableName) {
		case "clients":
			insertTableSQLString = INSERT_CLIENTS_SQL;
			break;
		case "orders":
			insertTableSQLString = INSERT_ORDERS_SQL;
			break;
		case "transactions":
			insertTableSQLString = INSERT_TRANSACTIONS_SQL;
			break;
		default:
			logger.log(Level.WARNING, String.format("createTable Tablename: %s does not exist, resulting further in SQL exception", tableName));
			break;
		}
    	
    	try (Connection connection = getConnection();
    		 PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQLString, Statement.RETURN_GENERATED_KEYS))
    	{
    		System.out.println("Inserting Table " + tableName + " ...");

    		if (obj instanceof Client) {
    			client = (Client) obj;
    			_type = "client";
    			
				preparedStatement.setString(1, client.getFirstName());
				preparedStatement.setString(2, client.getLastName());
				preparedStatement.setString(3, client.getEmail());
				preparedStatement.setString(4, client.getStreet());
    		}
    		else if(obj instanceof Order) {
    			order = (Order) obj;
    			_type = "order";
    			
				preparedStatement.setString(1, 	order.getUser());
				preparedStatement.setString(2, 	order.getProduct());
				preparedStatement.setDate(	3, 	order.getDate());
    		}
    		else if(obj instanceof Transaction) {
    			transaction = (Transaction) obj;
    			_type = "transaction";
    			
				preparedStatement.setString(1, transaction.getName());
				preparedStatement.setString(2, transaction.getSname());
				preparedStatement.setString(3, transaction.getEmail());
				preparedStatement.setString(4, transaction.getStreet());
				preparedStatement.setString(5, transaction.getProduct());
				preparedStatement.setDate(  6, transaction.getDate());
    			
    		}
    		else {
				throw new ClassCastException("Only Client or Order Class Cast Accepted!");
			}
    	
            int rowAffected = preparedStatement.executeUpdate();
            
            /**
             * If executeUpdate returns 0, that means DML query returned nothing,
             * instead we always want to Affect the insertion rows, thats why
             * custom Exception is thrown. 
             */
            if(rowAffected == 1)
            {
                // get candidate id
            	rs = preparedStatement.getGeneratedKeys();
                if(rs.next())
                	candidateId = rs.getInt(1);
                
                if(_type.equals("client"))
	                logger.log(Level.INFO, "Creted Client!\n" + "ID[" + candidateId + "] " +
	                				client.getFirstName() + " " +
	                				client.getLastName() + " " +
	                				client.getEmail() + " " +
	                				client.getStreet()
	                		);
                else if(_type.equals("order"))
                	logger.log(Level.INFO, "Creted Order!\n" + "ID[" + candidateId + "] " +
		            				order.getUser() + " " +
		            				order.getProduct() + " " +
		            				order.getDate().toString() + " "
            		);
                else
                	logger.log(Level.INFO, "Creted Transaction!\n" + "ID[" + candidateId + "] " +
                			transaction.getName() + " " +
                			transaction.getSname() + " " +
                			transaction.getEmail() + " " +
                			transaction.getStreet() + " " +
                			transaction.getProduct() + " " +
            				transaction.getDate() + " "
            		); 
            }else {
            	throw new UpdateTableEntryException(String.format("The Table %s was not updated, DML query returned 0!", tableName));
            }
        } catch (SQLException e) {
            printSQLException(e);
        } catch (ClassCastException e) {
        	logger.log(Level.WARNING, e.getMessage());
        } 
    }
    
    /**
     * Prints one of the transient TABLEs(CLIENTS, ORDERS) content for DEBUG reasons.
     * @param tableName the table name
     */
    public static void prinTableSql(String tableName) 
    {
    	final String CLIENTS_QUERY = "select name, sname, email, street from clients";    	
        final String ORDERS_QUERY = "select user, product, date from orders";    	
        
        
        String READ_TABLE_QUERY = (tableName.equals("clients")) ? CLIENTS_QUERY : ORDERS_QUERY;
        
    	try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(READ_TABLE_QUERY);
    		ResultSet rs = preparedStatement.executeQuery();)
    		{
	    		System.out.println("Reading Table " + tableName + " ...");
	    		
	    		int id_unique=1, entry_id=0;
	    		//Processing the ResultSet object for COLUMN names and entries
	            while (rs.next()) {
	            	entry_id = id_unique++;
	            	
	            	ResultSetMetaData metadata = rs.getMetaData();
	                int columnCount = metadata.getColumnCount();
	                
	                System.out.println(String.format("ID=%d ", entry_id));
	                for (int i=1; i<=columnCount; i++) 
	                {
	                    System.out.println(" " + rs.getMetaData().getColumnName(i) + "=" + rs.getObject(i));
	                }
	                System.out.println();
	            }
	        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    
    /**
     * Prints SQLException Handling module
     * { SQLState, Err Code, Message, Cause }
     * @param ex the SQLException
     */
    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                    e.printStackTrace(System.err);
                    System.err.println("SQLState: " +
                        ((SQLException)e).getSQLState());

                    System.err.println("Error Code: " +
                        ((SQLException)e).getErrorCode());

                    System.err.println("Message: " + e.getMessage());

                    Throwable t = ex.getCause();
                    while(t != null) {
                        System.out.println("Cause: " + t);
                        t = t.getCause();
                    }
            }
        }
    }
}
