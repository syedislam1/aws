package com.aws.lamda.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.lamda.api.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class LoginLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static String DYNAMO_TABLE = "UserTable";

	@SuppressWarnings("unchecked")
	public void handlePostRequest(InputStream input, OutputStream output, Context context) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(output);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		JSONParser parser = new JSONParser();
		JSONObject responseObject = new JSONObject();
		JSONObject responseBody = new JSONObject();

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDB dynamoDB = new DynamoDB(client);

		try {
			JSONObject reqObject = (JSONObject) parser.parse(reader);

			if (reqObject.get("body") != null) {
				User user = new User((String) reqObject.get("body"));

				Table table = dynamoDB.getTable(DYNAMO_TABLE);
				Item item = table.getItem("email", user.getEmail());

				if (item != null && item.getString("password").equals(user.getPassword())) {
					responseBody.put("message", "Login successful");
					responseObject.put("statusCode", 200);
				} else {
					responseBody.put("message", "Invalid email or password");
					responseObject.put("statusCode", 401);
				}

				// Set headers
				Map<String, String> headers = new HashMap<>();
				headers.put("Access-Control-Allow-Headers", "Content-Type");
				headers.put("Access-Control-Allow-Origin", "*");
				headers.put("Access-Control-Allow-Methods", "OPTIONS,POST");
				responseObject.put("headers", headers);

				// Set body
				responseObject.put("body", responseBody.toString());
			}
		} catch (Exception e) {
			responseObject.put("statusCode", 400);
			responseObject.put("error", e);
		}

		writer.write(responseObject.toString());
		reader.close();
		writer.close();
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		// TODO Auto-generated method stub
		return null;
	}
}
