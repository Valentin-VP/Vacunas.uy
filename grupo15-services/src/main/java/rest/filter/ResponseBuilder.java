package rest.filter;


import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import rest.filter.JsonSerializable;

public class ResponseBuilder {
	
	public static Response createResponse( Response.Status status  ) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put( "message", status.toString() );
		}
		catch( JSONException e ) {
			return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( Response.Status.INTERNAL_SERVER_ERROR ).build();
		}
		
		return Response.status( status ).entity( jsonObject.toString() ).build();
	}
	
	public static Response createResponse( Response.Status status, String message ) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put( "message", message );
		}
		catch( JSONException e ) {
			return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( Response.Status.INTERNAL_SERVER_ERROR ).build();
		}
		
		return Response.status( status ).entity( jsonObject.toString() ).build();
	}
	
	public static Response createResponse( Response.Status status, JsonSerializable json ) throws JSONException {
		return Response.status( status ).entity( json.toJson().toString() ).build();
	}
	
	public static Response createResponse( Response.Status status, JSONObject json ) throws JSONException {
		return Response.status( status ).entity( json.toString() ).build();
	}
	
	public static Response createResponse( Response.Status status, List<JsonSerializable> json ) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		
		for( int i = 0; i < json.size(); i++ ) {
			jsonArray.put( json.get(i).toJson() );
		}
		
		return Response.status( status ).entity( jsonArray.toString() ).build();
	}
	
	public static Response createResponse( Response.Status status, Map<String,Object> map ) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			for( Map.Entry<String,Object> entry : map.entrySet() ) {
				jsonObject.put( entry.getKey(), entry.getValue() );
			}
		}
		catch( JSONException e ) {
			return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( Response.Status.INTERNAL_SERVER_ERROR ).build();
		}
		
		return Response.status( status ).entity( jsonObject.toString() ).build();
	}
	
	public static Response createResponseUrl( Response.Status status, String url ) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put( "url", url );
		}
		catch( JSONException e ) {
			return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( Response.Status.INTERNAL_SERVER_ERROR ).build();
		}
		
		return Response.status( status ).entity( jsonObject.toString() ).build();
	}
	
}
