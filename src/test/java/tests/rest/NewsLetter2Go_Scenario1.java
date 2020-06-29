package tests.rest;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import lib.rest.RESTAssuredBase;

public class NewsLetter2Go_Scenario1 extends RESTAssuredBase{

	@BeforeTest
	public void setValues() {
		testCaseName = "Scenario 1";
		testDescription = "List Operations";
		nodeArray = new String[]{ "List Creation", "Segment Creation", "Segment Update", "Get List Details", "Delete List" };
		authors = "George";
		category = "API";
		dataFileName = "CreateList";
		dataSegmentFileName = "UpdateSegment";
		dataFileType = "JSON";
	}
	
	@Test(dataProvider = "fetchData",priority=1)
	public void createList(File file) 
	{	
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n1) Create List\n");
		
		Response postListResponse = postWithBodyAsFileAndUrl(file,"lists");
		verifyResponseCode(postListResponse, 201);
		listId = getContentWithKey(postListResponse,"value[0].id");
		System.out.println("--->"+listId);
	}
	
	@Test(dependsOnMethods= {"createList"},priority=2)
	public void createSegment() 
	{		
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n2) Create Segment\n");
		
		segmentBody = "{\r\n" + 
				"	\"list_id\": \""+listId+"\",\r\n" + 
				"	\"name\": \"New regular Segment GTJ\",\r\n" + 
				"	\"is_dynamic\": false\r\n" + 
				"}";
		
		Response postSegmentResponse = postWithBodyAsStringAndUrl(segmentBody,"groups");
		verifyResponseCode(postSegmentResponse, 201);
		segmentId = getContentWithKey(postSegmentResponse,"value[0].id");
		System.out.println("--->"+segmentId);
	
	}
	
	@Test(dependsOnMethods= {"createSegment"}, dataProvider = "fetchSegmentData",priority=3)
	public void updateSegment(File file)  
	{	
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n3) Update Segment\n");
		
		Response patchSegmentResponse = patchWithBodyAsFileAndUrl(file,"groups/"+segmentId);
		verifyResponseCode(patchSegmentResponse, 200);
	
	}
	
	@Test(dependsOnMethods= {"createList"},priority=4)
	public void getList() 
	{				
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n4) Get List\n");
		
		Response getListResponse = get("lists");
		verifyResponseCode(getListResponse, 200);
		String listIdFetched = getContentWithKey(getListResponse,"value[0].id");
		Assert.assertEquals(listIdFetched, listId);
	
	}
	
	@Test(dependsOnMethods= {"createList"},priority=5)
	public void deleteList() 
	{				
		System.out.println("\n\n*******************************************************************************");
		System.out.println("\n5) Delete List\n");
		
		Response deleteListResponse = delete("lists/"+listId);
		verifyResponseCode(deleteListResponse, 204);
	}
}
