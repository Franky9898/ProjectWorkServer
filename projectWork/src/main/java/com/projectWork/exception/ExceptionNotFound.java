package com.projectWork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ExceptionNotFound extends RuntimeException
{
	public ExceptionNotFound(String message) 
	{
        super(message);
    }
}

