package com.safelogisitics.gestionentreprisesusers.web.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ApiError {

  private HttpStatus status;
  private List<String> messages;
  private String errors;

  //

  public ApiError() {
    super();
  }

  public ApiError(final HttpStatus status, final List<String> messages, final String errors) {
    super();
    this.status = status;
    this.messages = messages;
    this.errors = errors;
  }

  public ApiError(final HttpStatus status, final String messages, final String error) {
    super();
    this.status = status;
    this.messages = Arrays.asList(messages);
    errors = error;
  }

  //

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(final HttpStatus status) {
    this.status = status;
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessages(final List<String> messages) {
    this.messages = messages;
  }

  public String getErrors() {
    return errors;
  }

  public void setErrors(final List<String> errors) {
    this.errors = String.join(", ", errors) ;
  }

  public void setError(final String error) {
    errors = error;
  }

}