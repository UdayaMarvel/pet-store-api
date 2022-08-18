package org.testApi;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetStore {
	public static JsonPath js;
	public String url = "https://petstore.swagger.io/v2";
	public static RestAssured r;
	public int cid;
	public String tname;
	public int id;
	@Test(priority=1)
	public void newPost(){
		r.baseURI = url;
		String response = given().log().all().header("Content-Type","application/json").body("{\r\n"
				+ "    \"id\": 9396,\r\n"
				+ "    \"category\": {\r\n"
				+ "        \"id\": 121,\r\n"
				+ "        \"name\": \"wit\"\r\n"
				+ "    },\r\n"
				+ "    \"name\": \"boohie\",\r\n"
				+ "    \"photoUrls\": [\r\n"
				+ "        \"string\"\r\n"
				+ "    ],\r\n"
				+ "    \"tags\": [\r\n"
				+ "        {\r\n"
				+ "            \"id\": 1,\r\n"
				+ "            \"name\": \"woffle\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"status\": \"available\"\r\n"
				+ "}").
		when().post("/pet").
		then().log().all().assertThat().statusCode(200).body("id", equalTo(9396)).extract().asString();
		
		js = new JsonPath(response);
		cid = js.get("category.id");
		System.out.println(cid);
		
		tname = js.get("tags[0].name");
		System.out.println(tname);
		
		id = js.get("id");
		System.out.println(id);
		
	}
	
	@Test(priority=2)
	public void verifyPost() {
		r.baseURI=url;
		String gresponse = given().log().all().pathParam("id", id).header("Content-Type","application/json").
		when().get("/pet/{id}").
		then().log().all().assertThat().statusCode(200).extract().asString();
		
		js= new JsonPath(gresponse);
		String tagname = js.get("tags[0].name");
		System.out.println(tagname);

		int nid = js.getInt("id");
		System.out.println(nid);
		
		assertEquals(nid,id);
  
	}

	@DataProvider(name="status")
	public Object[][] getStatus(){
		return new Object[][] {{"sold"},{"available"},{"pending"}};
	}
	@Test(priority = 3,dataProvider = "status")
	public void verifyByStatus(String status  ) {
		r.baseURI=url;
		String sresponse = given().log().all().queryParam("status",status).header("Content-Type","application/json").
		when().get("/pet/findByStatus").
		then().log().all().assertThat().statusCode(200).extract().asString();
		
		
	}
}
