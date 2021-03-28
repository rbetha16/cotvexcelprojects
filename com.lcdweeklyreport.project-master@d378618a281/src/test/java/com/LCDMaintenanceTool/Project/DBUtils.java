package com.LCDMaintenanceTool.Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public  class DBUtils {	
		 
	public static String executeSQLQuery(String sqlQuery)
	{
		String queryResultValue= "";
		Connection con=null;
	
	try {
        		
        Class.forName(ProjectVariables.DB_DRIVER_NAME);
        con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,ProjectVariables.DB_USERNAME,ProjectVariables.DB_PASSWORD);
        	
    	  if(con!=null) {
              System.out.println("Connected to the Database...");
          	}else {
              System.out.println("Database connection failed ");
          }
        	
    	  Statement st = con.createStatement();
    	  st.setQueryTimeout(30);
    	  ResultSet rs =st.executeQuery(sqlQuery);        	

         while(rs.next())
         {
            queryResultValue = rs.getString(1).toString();   
            	break;
         } 
            
          System.out.println("DB Result: "+queryResultValue);
            
          con.close();
            
        }catch (SQLException e) {
            e.printStackTrace();}
        
        catch (NullPointerException err) {
            System.out.println("No Records obtained for this specific query");
            err.getMessage();              }        
		
	     catch (ClassNotFoundException e1) {     		  		
				e1.getMessage();	}
       
	finally{
	      try {
	         if(con != null)
	           con.close();
	           
	        }catch(SQLException e)  {           
	       	 e.getMessage();         
	       } 
		}
	
	 return queryResultValue;
        
	}   
	
	// ####################################################################################################

	public static ArrayList<String> db_GetAllFirstColumnValues(String sqlQuery){
	
		Connection con=null;
		String result;
		ArrayList<String> resultList = new ArrayList<String>();
	
	try {
        		
    	  Class.forName(ProjectVariables.DB_DRIVER_NAME);
    	  con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,ProjectVariables.DB_USERNAME,ProjectVariables.DB_PASSWORD);
    	
    	  if(con!=null) {
              System.out.println("Connected to the Database...");
          	 }else {
              System.out.println("Database connection failed ");
          }  

    	  Statement st = con.createStatement();	        		
    	  ResultSet rs =st.executeQuery(sqlQuery);  
    	     	 
    	  while (rs.next()) {
    	          result = rs.getString(1).toString();
    	          resultList.add(result);
    	   }
            
           con.close();
            
        }catch (SQLException e) {
            e.printStackTrace();}
        
        catch (NullPointerException err) {
            System.out.println("No Records obtained for this specific query");
            err.getMessage();              }        
		
	     catch (ClassNotFoundException e1) {     		  		
				e1.getMessage();	}
       
	finally{
        try {
           if(con != null)
             con.close();
             
          }catch(SQLException e)  {           
         	 e.getMessage();         
         } 
	}
	
	  return resultList;
          
	}   
	
	//####################################################################################################

	public static HashMap<String,String> db_GetAllColumnValues_Trail(String sqlQuery, String sColumn) {
	
	Connection con=null;
	HashMap<String,String> resultList = new HashMap<String,String>();
	String sMidRule = null;
	String sReference = null;

	try {

		Class.forName(ProjectVariables.DB_DRIVER_NAME);
		con = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,
				ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);

		if (con != null) {
			System.out.println("Connected to the Database...");
			} else {
			System.out.println("Database connection failed ");
		}

		Statement st = con.createStatement();
		st.setQueryTimeout(10);
		ResultSet rs = st.executeQuery(sqlQuery);
		ResultSetMetaData rsmd = rs.getMetaData();

		int iColCount = rsmd.getColumnCount();
		System.out.println("Column"+iColCount);
		
		while (rs.next()) {
			try{
				 sMidRule = rs.getString("MidRuleDotVersion").toString();
				 sReference = rs.getString("reference").toString();
				 resultList.put(sMidRule, sReference);
				
			}catch(NullPointerException err){
		
				resultList.put(sMidRule, sReference);
			}
		}

		con.close();

	} catch (SQLException e) {
		e.getMessage();
	}

	catch (NullPointerException err) {
		System.out.println("No Records obtained for this specific query");
		err.getMessage();
	}

	catch (ClassNotFoundException e1) {
		e1.getMessage();
	}

	finally{
         try {
            if(con != null)
              con.close();
              
           }catch(SQLException e)  {           
          	 e.getMessage();         
          } 
	}
	
	return resultList;

}
		
	//####################################################################################################

	public static String db_GetFirstValueforColumn(String sQuery,String dbColumn) throws Exception{
	
		Connection conn=null;      
		String sVal="";

  	try {    	   
 
        Class.forName("oracle.jdbc.OracleDriver");
      
        conn = DriverManager.getConnection(ProjectVariables.DB_CONNECTION_URL,
				ProjectVariables.DB_USERNAME, ProjectVariables.DB_PASSWORD);
       
        Statement stmt=conn.createStatement();     
        stmt.setQueryTimeout(40);
        ResultSet rs=stmt.executeQuery(sQuery);

	    while (rs.next()) {
			sVal=rs.getString(dbColumn);
		}
	  
		System.out.println("Stored Data in DB:" +" " +sVal);
		 
		if (conn != null) {
		    conn.close();}
			
  		}catch(Exception e) {       
  			System.out.println("Exception "+e.getMessage());
  		}
  	
    finally{
          try {
             if(conn != null)
               conn.close();
               
            }catch(SQLException e)  {           
           	 e.printStackTrace();         
           } 
   		}

	return sVal;
                 
  }

   //####################################################################################################
	
}
