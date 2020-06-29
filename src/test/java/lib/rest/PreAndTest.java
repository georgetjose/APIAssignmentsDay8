package lib.rest;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.utils.DataInputProvider;
import lib.utils.HTMLReporter;

public class PreAndTest extends HTMLReporter{
	
	public String dataFileName, dataFileType,listId,segmentId,segmentBody,dataSegmentFileName,userId,userBody,oldLastName, newLastName;
	public String[] nodeArray;
	public int i=0;
	
	@BeforeSuite
	public void beforeSuite() {
		startReport();
	}
	
	@BeforeTest
	public void beforeTest() {
		
	}
	
	
	@BeforeClass
	public void beforeClass() throws FileNotFoundException, IOException {
		startTestCase(testCaseName, testDescription);	
		
		String accessTokenBody = "{ \"username\": \"georgeb4pc@gmail.com\", \"password\": \"test@123\", \"grant_type\": \"https://nl2go.com/jwt\"}";
		
		Properties prop = new Properties();
		prop.load(new FileInputStream(new File("./src/test/resources/config.properties")));
		
		RestAssured.baseURI = "https://"+prop.getProperty("server");
		Response accessTokenResponse = RestAssured
									   .given()
									   .contentType(ContentType.JSON)
									   .header("Authorization","Basic ZDhtbmx5czdfNEtDenZoaFNZZF9MU2VlaVQ5X0sxa3pJbTRUTENfaDI2TzZROnQwdnp6a29u")
									   .body(accessTokenBody)
									   .post("/oauth/v2/token");
		
		JsonPath accessTokenResponseJson = accessTokenResponse.jsonPath();
		String accessToken = accessTokenResponseJson.getString("access_token");
		RestAssured.authentication = RestAssured.oauth2(accessToken);
	}
	
	
	@BeforeMethod
	public void beforeMethod() {
		//for reports
		nodes = nodeArray[i];
		i++;
		svcTest = startTestModule(nodes);
		svcTest.assignAuthor(authors);
		svcTest.assignCategory(category);	

	}

	@AfterMethod
	public void afterMethod() {
		
	}
	
	@AfterClass
	public void afterClass() {
		
	}
	
	@AfterTest
	public void afterTest() {
		
	}

	@AfterSuite
	public void afterSuite() {
		endResult();
	}

	@DataProvider(name="fetchData")
	public  Object[][] getData(){
		if(dataFileType.equalsIgnoreCase("Excel"))
			return DataInputProvider.getSheet(dataFileName);	
		else if(dataFileType.equalsIgnoreCase("JSON")){
			Object[][] data = new Object[1][1];
			data[0][0] = new File("./data/"+dataFileName+"."+dataFileType);
			System.out.println(data[0][0]);
			return data;
		}else if(dataFileType.equalsIgnoreCase("JSON")){
				Object[][] data = new Object[1][1];
				data[0][0] = new File("./data/"+dataFileName+"."+dataFileType);
				System.out.println(data[0][0]);
				return data;
		}
		else {
			return null;
		}
	}	
	@DataProvider(name="fetchSegmentData")
		public  Object[][] getSegmentData(){
			if(dataFileType.equalsIgnoreCase("Excel"))
				return DataInputProvider.getSheet(dataSegmentFileName);	
			else if(dataFileType.equalsIgnoreCase("JSON")){
				Object[][] data = new Object[1][1];
				data[0][0] = new File("./data/"+dataSegmentFileName+"."+dataFileType);
				System.out.println(data[0][0]);
				return data;
			}else if(dataFileType.equalsIgnoreCase("JSON")){
					Object[][] data = new Object[1][1];
					data[0][0] = new File("./data/"+dataSegmentFileName+"."+dataFileType);
					System.out.println(data[0][0]);
					return data;
			}
			else {
				return null;
			}
			
	}

	@Override
	public long takeSnap() {
		return 0;
	}	

	
	
}
