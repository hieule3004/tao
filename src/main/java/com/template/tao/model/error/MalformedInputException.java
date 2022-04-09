package com.template.tao.model.error;

public class MalformedInputException extends RuntimeException {

  public MalformedInputException(String message) {
    super(message);
  }

  public MalformedInputException(Throwable throwable) {
    super(throwable);
  }
}
