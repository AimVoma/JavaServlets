package com.wtc;

import java.io.FileNotFoundException;
/**
 * java sql imports
 */
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
/**
 * java util imports
 */
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * fasterxml jackson imports
 */
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


final public class H2Utils {
	
    private static String URL = null;
    private static String Username = null;
    private static String Password = null;
	
    private static final String createTableClientsSQL = "create table if not exists clients (\r\n" + "  id identity primary key,\r\n" +
            "  name varchar(20),\r\n" + "  sname varchar(20),\r\n" + "  email varchar(20),\r\n" +
            "  street varchar(20)\r\n" + "  );";
    
    private static final String createTableOrdersSQL = "create table if not exists orders (\r\n" + "  id identity primary key,\r\n" +
            "  user varchar(20),\r\n" + "  product varchar(20),\r\n" + "  date varchar(20),\r\n" +
            "  );";
    
    private static final String INSERT_CLIENTS_SQL = "INSERT INTO clients" + 
    		"  (name, sname, email, street) VALUES " +
    		 " (?, ?, ?, ?);";
    
    private static final String INSERT_ORDERS_SQL = "INSERT INTO orders" + 
    		"  (user, product, date) VALUES " +
    		 " (?, ?, ?);";
    
	private static Logger logger = Logger.getLogger("ServletLogger");
	
	public static void _initialize() {
		try {
			Properties properties = PropertiesManager.getConfig();
			URL = properties.getProperty("DB_URL");
			Username = properties.getProperty("DB_USERNAME");
			Password = properties.getProperty("DB_PASSWORD");
			logger.log(Level.INFO, "H2 Database Initialized!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static Connection getConnection() {
        Connection connection = null;
        try {
        	Class.forName("org.h2.Driver");
			logger.log(Level.INFO, "Connecting to JDBC driver");
            connection = DriverManager.getConnection(URL, Username, Password);
        } catch (SQLException e) {
        	printSQLException(e);
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return connection;
    }
    
    public static void createTable(String tableName) 
    {
    	String createTableSQL = (tableName.equals("clients")) ? createTableClientsSQL:createTableOrdersSQL;
    	
    	try(Connection connection = getConnection();
    		Statement statement = connection.createStatement();){
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    
    public static void dropTableSql(String tableName) 
    {
    	final String DROP_TABLE_NAME = String.format("drop table if exists %s cascade", tableName);    	
    	
    	try(Connection connection = getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE_NAME);){
    		logger.log(Level.INFO, "Dropping Table " + tableName + " ...");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
    
    public static void joinTables(String tableOrders, String tableClients) 
    {
    	final String JOIN_QUERY = String.format(
    							"select product, date, name, sname, street\n"
				    			+ "FROM %s AS or\n"
				    			+ "JOIN %s AS cl\n"
				    			+ "ON or.user = cl.name", tableOrders, tableClients
				    			);

    	
    	try(Connection connection = getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(JOIN_QUERY);
        	ResultSet rs = preparedStatement.executeQuery();)
    	{
    		//Processing the ResultSet object for COLUMN names and entries
            while (rs.next()) {
            	ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();
                
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
    public static void insertTableSql(String tableName, Object obj) throws ClassCastException
    {
    	String _type = null;
    	Client client = null;
    	Order order = null;
    	ResultSet rs = null;
    	int candidateId = 0;
    	
    	String insertTableSQL = (tableName.equals("clients")) ? INSERT_CLIENTS_SQL:INSERT_ORDERS_SQL;
    	
    	try (Connection connection = getConnection();
    		 PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQL, Statement.RETURN_GENERATED_KEYS))
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
    		}else {
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
                else
                	logger.log(Level.INFO, "Creted Order!\n" + "ID[" + candidateId + "] " +
            				order.getUser() + " " +
            				order.getProduct() + " " +
            				order.getDate().toString() + " "
            		);
            }else {
            	throw new UpdateTableEntryException(String.format("The Table %s was not updated, DML query returned 0!", tableName));
            }
        } catch (SQLException e) {
            printSQLException(e);
        } catch (ClassCastException e) {
        	logger.log(Level.WARNING, e.getMessage());
        } catch (UpdateTableEntryException e) {
        	logger.log(Level.WARNING, e.getMessage());
        }
    }
    
    public static List<Map> returnTableSql(String tableName) 
    {
    	final String CLIENTS_QUERY = "select name, sname, email, street from clients";    	
		List<Map> resultList = new ArrayList<Map>();
		
    	try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CLIENTS_QUERY);
    		ResultSet rs = preparedStatement.executeQuery();)
    		{
	    		System.out.println("Reading Table " + tableName + " ...");
	    		
	    		int id_unique=1, entry_id=0;
	    		
	    		//Processing the ResultSet object for COLUMN names and entries
	            while (rs.next()) {
	            	entry_id = id_unique++;
	            	
	            	Map<String, Comparable> map = new HashMap<String, Comparable>();
	            	map.put("id", entry_id);
	            	map.put("name", rs.getString("name"));
	            	map.put("sname", rs.getString("sname"));
	            	map.put("email", rs.getString("email"));
	            	map.put("street", rs.getString("street"));
	            	
	            	resultList.add(map);
	            }
	        } catch (SQLException e) {
            printSQLException(e);
        }
		return resultList;
    }
    
    
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

class Order{
	@JsonProperty("user") private String user;
	@JsonProperty("product") private String product;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT")
	private Date date;
	
	public String getUser() {
		return user;
	}
	public String getProduct() {
		return product;
	}
	public Date getDate() {
		return date;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public void setDate(Date date) {
		this.date = date;
	}	
}

class Client {
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	 @JsonProperty("firstName") private String firstName;
	 @JsonProperty("lastName") private String lastName;
	 @JsonProperty("email") private String email;
	 @JsonProperty("street") private String street;
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	 public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getStreet() {
		return street;
	}
	
	
}
