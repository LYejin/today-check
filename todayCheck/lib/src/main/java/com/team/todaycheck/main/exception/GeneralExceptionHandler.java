package com.team.todaycheck.main.exception;

import java.util.NoSuchElementException;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.team.todaycheck.main.DTO.MessageDTO;

@RestControllerAdvice
public class GeneralExceptionHandler {
	/*
	 * 계정을 찾을 수 없을 때 , 
	 */
	@ExceptionHandler({
		AccountException.class , AccountNotFoundException.class , NotAuthorizationException.class , NoSuchElementException.class
		, UnknownPostException.class
	})
	public MessageDTO handleBadRequestException(Exception e) {
	    return MessageDTO.builder()
	    		.code("-1")
	    		.message(e.getMessage())
	    		.build();
	}
	
	// RefreshToken 만료되었을때
	@ExceptionHandler({ 
		InvalidateTokenException.class
	})
	public MessageDTO handleInvalidateTokenException(Exception e) {
	    return MessageDTO.builder()
	    		.code("-2")
	    		.message(e.getMessage())
	    		.build();
	}
	
	// refreshToken 쿠키가 없을 때
	@ExceptionHandler({
		MissingRequestCookieException.class
	})
	public MessageDTO handleNotAuthorizationRequestException(Exception e) {
	    return MessageDTO.builder()
	    		.code("-3")
	    		.message(e.getMessage())
	    		.build();
	}
	
	// refreshToken이 변조되었을 때
	@ExceptionHandler({
		FalsifyTokenException.class
	})
	public MessageDTO handleFlasifyTokenException(Exception e) {
	    return MessageDTO.builder()
	    		.code("-4")
	    		.message(e.getMessage())
	    		.build();
	}
}
