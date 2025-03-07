package com.mthree.flooringmastery.service;

public class FlooringMasteryValidationException extends Exception {
  public FlooringMasteryValidationException(String message) {
    super(message);
  }

  public FlooringMasteryValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
