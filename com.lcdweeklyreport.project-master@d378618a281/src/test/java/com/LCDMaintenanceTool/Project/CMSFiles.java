package com.LCDMaintenanceTool.Project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public class CMSFiles 
{
	
//#####################################################REMOVING ALREADY DOWNLOADED FILES#####################################################
	
		public static boolean removefiles(String spath) {
			try {
				File file = new File(spath);
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted! from "+spath);
					return true;
				} else {
					System.out.println("No Existing Files Present in "+spath);
					return true;
				}
			} catch (Exception e) {
				System.out.println("Path not found" +spath);
				return false;
			}

		}

//#####################################################DOWNLOADIGN FILES AND COPYING TO DESTINATION FOLDER//#####################################################
		
		@Test
		public void downloadCurrentAndRetiredLCD_Articles() throws InterruptedException, IOException {
			

			String DestinationFolder=null;
			
			LocalDateTime datetime1 = LocalDateTime.now();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");

			String formatDateTime = datetime1.format(formatter);
			
			String sDirectory = System.getProperty("user.dir");

			String formatDateTime1 = datetime1.format(formatter1);

			String newTime = formatDateTime1.replace(":", "-");

			String sDateandTimeFolder = formatDateTime + " CurrentTime " + newTime;

			System.out.println(sDateandTimeFolder);
			
	        String sDate=filesUpdatedDate();
	        
	        String sActualDate=sDate.replace("-", "/");
				
			System.setProperty("webdriver.chrome.driver","P:\\Trinath\\chromedriverlatest\\chromedriver.exe");
			
			ChromeOptions options = new ChromeOptions();
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("useAutomationExtension", "false");
			options.setExperimentalOption("prefs", prefs);
			
			WebDriver driver = new ChromeDriver(options);
			
			driver.get("https://www.cms.gov/medicare-coverage-database/downloads/downloadable-databases.aspx");
			
			driver.manage().window().maximize();
				  
			//Scrolling till Element into view 
			JavascriptExecutor js = (JavascriptExecutor) driver;
	        
			//WebElement ScrollDown0 = driver.findElement(By.xpath("//span[contains(text(),'LICENSE FOR USE OF CURRENT DENTAL TERMINOLOGY (CDT™)')]"));
			
			WebElement ScrollDown = driver.findElement(By.xpath("(//input[@type='submit' and contains(@value,'Accept')])[1]"));
	
			//js.executeScript("arguments[0].scrollIntoView(true);", ScrollDown0);
			
			js.executeScript("arguments[0].scrollIntoView(true);", ScrollDown);
	
			int i = driver.findElements(By.xpath("(//input[@type='submit' and contains(@value,'Accept')])[1]")).size();
	
			while (i == 0) {
				
				Thread.sleep(2000);
			}
	
//			driver.findElement(By.xpath("(//input[@type='submit' and contains(@value,'Accept')])[1]")).click();
			driver.findElement(By.xpath("//button[@id='lnkBtnAccept']")).click();
			
	
			Thread.sleep(10000);
	
			String sCMSFileUpdatedDate=driver.findElement(By.xpath("//th[text()='Local Coverage Information']/following-sibling::td[1]")).getText();
			
			String sPropertyDate = gfn_ReadData_fromPropertyfile("Date","DateHistory.properties");
			
			System.out.println("Date in UI :"+ sCMSFileUpdatedDate);
			
			System.out.println("Date of the week :"+sPropertyDate);				                                     
					
			if(!sCMSFileUpdatedDate.trim().equalsIgnoreCase(sPropertyDate.trim())){

				    //Removing existing downloaded files
					String sUserName = System.getProperty("user.name");
					
					String[] srcArray = new String[2];
					
					srcArray[0] = "C:\\Users\\" + sUserName + "\\Downloads\\all_lcd.zip";
					
					System.out.println(srcArray[0]);
					
					srcArray[1] = "C:\\Users\\" + sUserName + "\\Downloads\\all_article.zip";

					/*srcArray[0] = "H:\\Downloads\\all_lcd.zip";
					
					srcArray[1] = "H:\\Downloads\\all_article.zip";*/
					
					removefiles(srcArray[0]);
		
					removefiles(srcArray[1]);			
	
					Thread.sleep(3000);
					
					driver.findElement(By.xpath("//input[@value='CurrentAndRetiredLCDs']")).click();
			
					driver.findElement(By.xpath("//input[@title='Submit Criteria']")).click();
					
					Thread.sleep(30000);
			
					driver.findElement(By.xpath("//input[@value='CurrentAndRetiredArticles']")).click();
			
					Thread.sleep(1000);
			
					driver.findElement(By.xpath("//input[@title='Submit Criteria']")).click();
			
					Thread.sleep(60000);
					
				    driver.quit();
					
					String[] desArray = new String[2];
					
					desArray[0] = "C:\\LCD Updates\\" + sDateandTimeFolder + "\\all_lcd.zip";
					
					desArray[1] = "C:\\LCD Updates\\" + sDateandTimeFolder + "\\all_article.zip";
					
					DestinationFolder = "C:\\LCD Updates\\" + sDateandTimeFolder + "";
					
					System.out.println(DestinationFolder);
					
					boolean blnFolder = CreateFolder("C:\\LCD Updates");
					
					if (blnFolder){
						File srcFolder = new File(sDirectory+"\\Resources\\Logo");
				    	File destFolder = new File("C:\\LCD Updates\\");
				    	
						FileUtils.copyDirectory(srcFolder, destFolder);
						
					}
					
					CreateFolder(DestinationFolder);
			
					for (int j = 0; j < 2; j++) {
			
						copyFileUsingStream(srcArray[j], desArray[j]);
			
					}
					
					DestinationFolder = "C:\\LCD Updates\\";	
					File latestFileInDrive=getLatestFilefromDir(DestinationFolder);	
					String sDestinationFolder = latestFileInDrive.toString();			
					
			    	String sDriverPath = sDirectory+"\\Resources";
					String sExcelPath = sDirectory+"\\Resources\\InputFileMacroV38.xlsm";
					Process killExcel = Runtime.getRuntime().exec("taskkill /F /IM EXCEL.EXE");
					killExcel.waitFor();
					
					String sqlQuery = "select mid_rule_key ||'.' || rule_Version as MidRuleDotVersion,Reference from rules.sub_rules where disabled_10 = 0 and deactivated_10 = 0  and LIBRARY_STATUS_KEY = '1' ";
					
					HashMap<String,String> sRules = db_GetAllColumnValues(sqlQuery,"MidRuleDotVersion");
					
					GenerateBOReportNew(sRules,sDestinationFolder);
					
					System.out.println("BO Report Generated Successfully");
					
			    	Process p = Runtime.getRuntime().exec(new String[] { "wscript.exe", sDriverPath+"\\Trigger.vbs",sDestinationFolder,sCMSFileUpdatedDate,sExcelPath});
					p.waitFor();
					
					gfn_Writedata_toPropertyfile("Date",sCMSFileUpdatedDate,"DateHistory.properties");
					
					System.out.println("Impacted LCD and articles are generated successfully");

						
				}else {

					System.out.println("News file is not updated in CMS");
				    
				    driver.close();
				}

		}
		
		//#####################################################COPYING FILES FROM SOURCE TO DESTINATION FOLDER===============================================================
		
		private static boolean copyFileUsingStream(String srcArray, String desArray) throws IOException {
			
			InputStream is = null;
			OutputStream os = null;
			try {
				is = new FileInputStream(srcArray);
				os = new FileOutputStream(desArray);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				
				return true;
				
			}catch(Exception e){
				return false;
			} finally {
				is.close();
				os.close();
			}
		}

	//##############################################CREATING FOLDER IN DESTINATION//#####################################################
		
		public static boolean CreateFolder(String sFolderPath) {

			File file = new File(sFolderPath);

			// If the folder does not exist then create it

			if (!file.exists()) {

				if (file.mkdir()) {

					System.out.println("Directory is created!");
					return true;

				} else {

					System.out.println("Failed to create directory!");
					return false;

				}
			}
			
			return true;
			
		}

	//########################################################CALENDER DAY OF WEEK##################################################################
		
		public static String filesUpdatedDate(){
			
			Date now = new Date();
			
		    Calendar calendar = Calendar.getInstance();
		    
		    calendar.setTime(now);
		    
		    int i=calendar.get(Calendar.DAY_OF_WEEK)-1;
		    
		    LocalDateTime datetime1 = LocalDateTime.now();
			
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-M-yyyy");
			
			String formatDateTime = datetime1.format(format);
			
			String[] ymd=formatDateTime.split("-");
			
			int day = Integer.parseInt(ymd[0]);
			
			String updated= String.valueOf(day-i);
			
			String mm=ymd[1];
			
			String yy=ymd[2];
			
			String updatedDate=mm+"-"+updated+"-"+yy;
			
			System.out.println(updatedDate);
			
			return updatedDate;
		}
		
	//###########################################################################################################################	
		
		public static void removefilesInPath(String spath) {
		  try {
		         BufferedWriter out = new BufferedWriter (new FileWriter(spath));
		         out.write("aString1\n");
		         out.close();
		         boolean success = (new File(spath)).delete();
		         
		         if (success) {
		            System.out.println("The file has been successfully deleted"); 
		         }
		         
		      }catch (IOException e) {
		         System.out.println("exception occoured"+ e);
		         System.out.println("File does not exist or you are trying to read a file that has been deleted");
		      }
	    
		}
		
	//###################################################WRITE DATE TO PROPERTIES FILE###################################################
		
		public static void gfn_Writedata_toPropertyfile(String sKey,String sValue,String sFile) throws IOException{
			
			File configFile = new File(sFile);
			FileInputStream in = new FileInputStream(configFile);
			Properties props = new Properties();
			props.load(in);
			in.close();
			FileOutputStream out = new FileOutputStream(configFile);
			props.setProperty(sKey, sValue);	
			props.store(out, null);
			out.close();
		}
		
	//#####################################################READ DATA FROM PROPERTIES FILE######################################################################
		
		public static String gfn_ReadData_fromPropertyfile(String sKey,String sFile) throws IOException{
		
			File file = new File(sFile);		
			FileInputStream fileInput;
			fileInput = new FileInputStream(file);	
			Properties prop = new Properties();
			prop.load(fileInput);
			String value = prop.getProperty(sKey);
			System.out.println(prop.getProperty(sKey));
			return value;
		}
		
		private File getLatestFilefromDir(String dirPath){
		    File dir = new File(dirPath);
		    File[] files = dir.listFiles();
		    if (files == null || files.length == 0) {
		        return null;
		    }

		    File lastModifiedFile = files[0];
		    for (int i = 1; i < files.length; i++) {
		       if (lastModifiedFile.lastModified() < files[i].lastModified()) {
		           lastModifiedFile = files[i];
		       }
		    }
		    return lastModifiedFile;
		}

		
		public static boolean GenerateBOReport(HashMap<String,String> sRules, String sFolderPath) {

			try {
				
				String path = sFolderPath;
				XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet = workbook.createSheet("Report1");
				
		        XSSFRow row = sheet.createRow(0);
	        	row.createCell(0).setCellValue("MidRuleDotVersion");
	        	row.createCell(1).setCellValue("Reference");
	        	row.createCell(2).setCellValue("MedicalPolicy");
	        	row.createCell(3).setCellValue("TopicTitle");
	        	
		        Iterator entries = sRules.entrySet().iterator();
		        int i=1;
		        while(entries.hasNext()){
		        	Map.Entry pair = (Entry) entries.next();

		        	String sReference = pair.getValue().toString();
		        	String sMidRule = pair.getKey().toString();	
		        	
		        	row = sheet.createRow(i);
		        	row.createCell(0).setCellValue(sMidRule);
		        	row.createCell(1).setCellValue(sReference);
		        	String[] sRule = sMidRule.split("\\.");
		        	String sqlQuery = "select MED_POL_TITLE_4_SUB_RULE_KEY from PAYER_RULES.VW_SUB_RULES where mid_rule_key='"+sRule[0]+"'";
		        	String sqlTopicQuery = "select TOPIC_TITLE_4_SUB_RULE_KEY from PAYER_RULES.VW_SUB_RULES where mid_rule_key='"+sRule[0]+"'";
		        	String sMedicalPolicy = DBUtils.executeSQLQuery(sqlQuery);
		        	String sTopic = DBUtils.executeSQLQuery(sqlTopicQuery);
		        	row.createCell(2).setCellValue(sMedicalPolicy);
		        	row.createCell(3).setCellValue(sTopic);
		        	i = i+1;
		        	
		        }
	
		        String soutputExcel = path+"\\BOReport.xlsx";
				FileOutputStream fileOut = new FileOutputStream(soutputExcel);
				workbook.write(fileOut);
				fileOut.close();

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		

public static boolean GenerateBOReportNew(HashMap<String,String> sRules, String sFolderPath) {
	
	Connection con=null;
	String sMedicalPolicy = null;
	String sTopic = null;
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

		String path = sFolderPath;
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report1");
		
        XSSFRow row = sheet.createRow(0);
    	row.createCell(0).setCellValue("MidRuleDotVersion");
    	row.createCell(1).setCellValue("Reference");
    	row.createCell(2).setCellValue("MedicalPolicy");
    	row.createCell(3).setCellValue("TopicTitle");
    	
        Iterator entries = sRules.entrySet().iterator();
        int i=1;
        while(entries.hasNext()){
        	Map.Entry pair = (Entry) entries.next();

        	String sReference = pair.getValue().toString();
        	String sMidRule = pair.getKey().toString();	
        	
        	row = sheet.createRow(i);
        	row.createCell(0).setCellValue(sMidRule);
        	row.createCell(1).setCellValue(sReference);
        	String[] sRule = sMidRule.split("\\.");
        	String sqlQuery = "select MED_POL_TITLE_4_SUB_RULE_KEY,TOPIC_TITLE_4_SUB_RULE_KEY from PAYER_RULES.VW_SUB_RULES where mid_rule_key='"+sRule[0]+"'";

        	try{
        	ResultSet rs =st.executeQuery(sqlQuery);
        	
	        	 while(rs.next())
		         {
        		  sMedicalPolicy = rs.getString(1).toString();
        		  sTopic = rs.getString(2).toString();
		          break;
		         } 

        	}catch (NullPointerException err) {
		            System.out.println("No Records obtained for this specific query");
		            err.getMessage(); 
	         }
        	row.createCell(2).setCellValue(sMedicalPolicy);
        	row.createCell(3).setCellValue(sTopic);
        	i = i+1;
        	
        }

        String soutputExcel = path+"\\BOReport.xlsx";
		FileOutputStream fileOut = new FileOutputStream(soutputExcel);
		workbook.write(fileOut);
		fileOut.close();
		con.close();

	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
	
	finally{
	      try {
	         if(con != null)
	           con.close();
	           
	        }catch(SQLException e)  {           
	       	 e.getMessage();         
	       } 
		}
	
	return true;
	
}


public static HashMap<String,String> db_GetAllColumnValues(String sqlQuery, String sColumn) {
	
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
}