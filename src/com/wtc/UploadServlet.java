package com.wtc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50)   // 50MB
public class UploadServlet extends HttpServlet {
    /**
     * Name of the directory where uploaded files will be saved, relative to
     * the web application directory.
     */
    private static final String SAVE_DIR = "uploadFiles";
     
    /**
     * handles file upload
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR;
        
        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) fileSaveDir.mkdir();

         
        for (Part part : request.getParts()) {
        	String fileName = extractFileName(part);
            // refines the fileName in case it is an absolute path
            fileName = new File(fileName).getName();
            part.write(savePath + File.separator + fileName);
        }
        
        request.setAttribute("message", "Upload has been done successfully!");
        response.sendRedirect(request.getContextPath() + "/ManageDBServlet");
        
//        getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
        
        
    }
    private void readFile() {
		// TODO Auto-generated method stub
    	String STATIC_FILE_DIR = "/home/aimilios/eclipse-workspace-REST/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/JavaServlets/uploadFiles/";
		JSONParser parser = new JSONParser();		
		
		File file = new File(STATIC_FILE_DIR + "clients.json"); 
		// assumes the current class is called MyLogger
		
		Logger logger = Logger.getLogger("ServletLogger");
		
		try {			
			JSONObject Jobj = (JSONObject) parser.parse(new FileReader(file));
			
			logger.log(Level.INFO, Jobj.toJSONString());

			
			logger.log(Level.INFO, "Parsing Json File ... ");

			try {
					JSONArray clients = (JSONArray) Jobj.get("clients");
					
					Iterator<String> iterator = clients.iterator();
		            while (iterator.hasNext()) {
		                System.out.println(iterator.next());
		            }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			
		logger.log(Level.INFO, "Processing ");
		  
	}
	/**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
}