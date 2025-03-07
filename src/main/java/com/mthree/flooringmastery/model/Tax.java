package com.mthree.flooringmastery.model;

import java.math.BigDecimal;

public class Tax {
  private final String stateAbbreviation;
  private String stateName;
  private BigDecimal taxRate;

  public Tax(String stateAbbreviation) {
    this.stateAbbreviation = stateAbbreviation;
  }

  public Tax(String stateAbbreviation, String stateName, BigDecimal taxRate) {
    this.stateAbbreviation = stateAbbreviation;
    this.stateName = stateName;
    this.taxRate = taxRate;
  }

  public String getStateAbbreviation() {
    return stateAbbreviation;
  }

  public String getStateName() {
    return stateName;
  }

  public void setStateName(String stateName) {
    this.stateName = stateName;
  }

  public BigDecimal getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BigDecimal taxRate) {
    this.taxRate = taxRate;
  }
}
