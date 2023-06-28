package com.aws.lamda.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.aws.lamda.api.model.User;



public class UserLambdaHandler implements RequestStreamHandler {

	private static String DYNAMO_TABLE = "UserTable";

	@SuppressWarnings("unchecked")

	public void handlePutRequest(InputStream input, OutputStream output, Context context) throws IOException {
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

				String email = user.getEmail();
				if (EmailValidator.getInstance().isValid(email)) {
					dynamoDB.getTable(DYNAMO_TABLE)
							.putItem(new PutItemSpec().withItem(new Item().withString("name", user.getName())
									.withString("email", user.getEmail()).withString("password", user.getPassword())));
					responseBody.put("message", "New Item created/updated");
					responseObject.put("statusCode", 200);
					// Set headers
					Map<String, String> headers = new HashMap<>();
					headers.put("Access-Control-Allow-Headers", "Content-Type");
					headers.put("Access-Control-Allow-Origin", "*");
					headers.put("Access-Control-Allow-Methods", "OPTIONS,GET");
					responseObject.put("headers", headers);

					// Set body
					responseObject.put("body", responseBody.toString());
				} else {
					responseBody.put("message", "Invalid email format");
					responseObject.put("statusCode", 400);

				}

			}
		} catch (Exception e) {
			String errorMessage = "An error occurred: " + e.getMessage();
			responseObject.put("error", errorMessage);
		}

		writer.write(responseObject.toString());
		reader.close();
		writer.close();

	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// TODO Auto-generated method stub

	}

}
