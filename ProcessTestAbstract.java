package com.process;

import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public abstract class ProcessTestAbstract {
	
	public int executionId = 0;
	public boolean success = true;
	

	public abstract String executeTest(Client client , Map<String, String> context);
	
	public abstract String generateUrl(Map<String, String> context);
	
	public void execute(Client client , Map<String, String> context){
		System.out.println("URL:"+this.generateUrl( context));
		String payload = this.generatePayload(context);
		if(payload!= null){
			System.out.println("Payload:"+payload);
		}
		System.out.println("Output:"+this.executeTest(client,context));
		System.out.println("");
	}
	
	public String generatePayload(Map<String, String> context){
		return null;
	}
	
	public String generatePOST(Client client, Map<String, String> context){
		WebResource webResource = client.resource(this.generateUrl(context));
		WebResource.Builder builder = webResource.getRequestBuilder();
		ClientResponse response = builder.type("application/json").header("cookie", context.get("JSESSION")).post(ClientResponse.class, this.generatePayload(context));
		String output =  this.validateStauts(response);
		return output;
	}
	
	
	public String generateGET(Client client, Map<String, String> context){
		WebResource webResource = client.resource(this.generateUrl(context));
		WebResource.Builder builder = webResource.getRequestBuilder();
		ClientResponse response = builder.type("application/json").header("cookie", context.get("JSESSION")).get(ClientResponse.class);
		String output =  this.validateStauts(response);
		return output;
	}
	
	public String validateStauts(ClientResponse response){
		if(response.getStatus() == 200 && success == false)
			throw new RuntimeException("Negative Test Failed : HTTP error code : " + response.getStatus()+" "+response.getEntity(String.class));
		if (response.getStatus() != 200  && success == true ) {
			throw new RuntimeException("Positive Test Failed : HTTP error code : " + response.getStatus()+" "+response.getEntity(String.class));
		}
		
		return response.getEntity(String.class);
	}

	
}