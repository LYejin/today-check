package com.team.todaycheck.main.exception;

@SuppressWarnings("serial")
public class InvalidateTokenException extends RuntimeException {
	public InvalidateTokenException() 
	{
		
	}
	
	public InvalidateTokenException(String message) {
		super(message);
	}
}
