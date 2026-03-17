package com.rebellion.notifyhub.exception;

public class CustomInvalidJsonStructure extends RuntimeException{
	
	public CustomInvalidJsonStructure(String message){
		super("Invalid JSON Structure: " + message);
	}
}
