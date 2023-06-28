package aws.serverless;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class DynamoDBAPITest {

	private static final String BASE_URL = "https://bixc0so21f.execute-api.us-east-1.amazonaws.com/dev";
	/*
	 * @Before public void setup() { try { DynamoDBAPITest dynamoDBAPITest = new
	 * DynamoDBAPITest(); dynamoDBAPITest.runTests(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
//	@Test
//	public void runregistrationTests() throws IOException {
//		System.out.println("Running tests...");
//
//		// Test storeUser and getUser
//		storeUser( "syed@examle.com","syed", "11114");
//		// Additional test cases can be added here
//		System.out.println("Tests completed.");
//	}

	@Test
	public void runLoginTests() throws IOException {
		System.out.println("Running tests...");

		// Test Login and getUser

		loginUser("syed@examle.com", "11114");
		System.out.println("Tests completed.");
	}

	public void storeUser(String email, String name, String password) throws IOException {
		String url = BASE_URL + "/registeruser";
		String payload = "{ \"email\":\"" + email + "\", \"name\":\"" + name + "\",\"password\":\"" + password + "\"}";

		HttpPost request = new HttpPost(url);
		request.setEntity(new StringEntity(payload));

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = httpClient.execute(request);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			System.out.println("User stored successfully.");

			AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
			DynamoDB dynamoDB = new DynamoDB(client);
			Table table = dynamoDB.getTable("UserTable"); //  "Table"  name

			Item item = table.getItem("email", email);
			if (item != null) {
				System.out.println("User information found in DynamoDB: " + item.toJSON());
			} else {
				System.out.println("Failed to retrieve user information from DynamoDB.");
			}

		} else {
			System.out.println("Failed to store user. Status code: " + statusCode);
		}

		httpClient.close();
	}

	public void loginUser(String email, String password) throws IOException {
		String url = BASE_URL + "/loginuser";

		String payload = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";

		HttpPost request = new HttpPost(url);
		request.setEntity(new StringEntity(payload));

		JSONObject responseBody = new JSONObject();

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = httpClient.execute(request);

		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == 200) {

			String token = Jwts.builder().setSubject(email).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

			responseBody.put("token", token);


			AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
			DynamoDB dynamoDB = new DynamoDB(client);
			Table table = dynamoDB.getTable("UserTable"); //Table 

			Item item = table.getItem("email", email);
			if (item != null) {
				System.out.println("User Login Successful DynamoDB: " + statusCode);
			} else {
				System.out.println("No User Found.");
			}

		} else {
			System.out.println("No User Found." + statusCode);
		}
		httpClient.close();
	}

	private static String SECRET_KEY = System.getenv("SECRET_KEY");

}
