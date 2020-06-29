package tests.rest;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import lib.rest.RESTAssuredBase;

public class NewsLetter2Go_Scenario2 extends RESTAssuredBase
{
	@BeforeTest
	public void setValues1() {
		testCaseName = "Scenario 2";
		testDescription = "Create a new list";
		nodeArray = new String[]{ "Get all the users", "Get your user detail", "Update your user details", "Verify updated user details" };
		authors = "George";
		category = "API";
		dataFileName = "CreateList";
		dataSegmentFileName = "UpdateSegment";
		dataFileType = "JSON";
	}
	
	@Test(priority=1)
	public void GetAllUsers() 
	{	
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n1) Get Users\n");
		
		Response getAllUsersResponse = get("users");
		verifyResponseCode(getAllUsersResponse, 200);
		userId = getContentWithKey(getAllUsersResponse,"value[0].id");		
	}
	
	@Test(priority=2)
	public void GetAUsers() 
	{	
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n2) Get My User Details\n");
		
		Response getAUsersResponse = get("users/"+userId);
		verifyResponseCode(getAUsersResponse, 200);
		System.out.println("-->First Name: "+getContentWithKey(getAUsersResponse,"value[0].first_name"));	
		System.out.println("-->Last Name: "+getContentWithKey(getAUsersResponse,"value[0].last_name"));
		System.out.println("-->Email: "+getContentWithKey(getAUsersResponse,"value[0].email"));
		System.out.println("-->Company Name: "+getContentWithKey(getAUsersResponse,"value[0].name"));	
		oldLastName = getContentWithKey(getAUsersResponse,"value[0].last_name");
	}
	
	@Test(priority=3)
	public void updateSegment()  
	{	
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n3) Update User\n");
		
		Random rand = new Random(); 
		  
        // Generate random integers in range 0 to 99 
        int randomnumber = rand.nextInt(100); 
		userBody = "{\r\n" + 
				"	\"last_name\": \"GTJ Test User "+randomnumber+"\"\r\n" + 
				"}";
		
		Response patchUserResponse = patchWithBodyAsStringAndUrl(userBody, "users/"+userId);
		verifyResponseCode(patchUserResponse, 200);
	}
	
	@Test(priority=4)
	public void verifyUserAfterUpdate() 
	{	
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n4) Verify My User Details after Update\n");
		
		Response getUpdatedUsersResponse = get("users/"+userId);
		verifyResponseCode(getUpdatedUsersResponse, 200);
		System.out.println("-->First Name: "+getContentWithKey(getUpdatedUsersResponse,"value[0].first_name"));	
		System.out.println("-->Last Name: "+getContentWithKey(getUpdatedUsersResponse,"value[0].last_name"));
		System.out.println("-->Email: "+getContentWithKey(getUpdatedUsersResponse,"value[0].email"));
		System.out.println("-->Company Name: "+getContentWithKey(getUpdatedUsersResponse,"value[0].name"));
		newLastName = getContentWithKey(getUpdatedUsersResponse,"value[0].last_name");
		Assert.assertNotEquals(oldLastName, newLastName);
		System.out.println("Update verified\nOld last name:"+ oldLastName+" was changed to "+newLastName);
	}
}
