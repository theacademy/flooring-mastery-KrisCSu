package com.mthree.flooringmastery.model;

import java.math.BigDecimal;

public class Order {
  private int orderNumber;
  private String customerName;
  private String state;
  private BigDecimal taxRate;
  private String productType;
  private BigDecimal area;
  private BigDecimal costPerSquareFoot;
  private BigDecimal laborCostperSquareFoot;
  private BigDecimal materialCost;
  private BigDecimal laborCost;
  private BigDecimal tax;
  private BigDecimal total;

  public Order(int orderNumber) {
    this.orderNumber = orderNumber;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(int orderNumber) {
    this.orderNumber = orderNumber;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public BigDecimal getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BigDecimal taxRate) {
    this.taxRate = taxRate;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public BigDecimal getArea() {
    return area;
  }

  public void setArea(BigDecimal area) {
    this.area = area;
  }

  public BigDecimal getCostPerSquareFoot() {
    return costPerSquareFoot;
  }

  public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
    this.costPerSquareFoot = costPerSquareFoot;
  }

  public BigDecimal getLaborCostperSquareFoot() {
    return laborCostperSquareFoot;
  }

  public void setLaborCostperSquareFoot(BigDecimal laborCostperSquareFoot) {
    this.laborCostperSquareFoot = laborCostperSquareFoot;
  }

  public BigDecimal getMaterialCost() {
    return materialCost;
  }

  public void setMaterialCost(BigDecimal materialCost) {
    this.materialCost = materialCost;
  }

  public BigDecimal getLaborCost() {
    return laborCost;
  }

  public void setLaborCost(BigDecimal laborCost) {
    this.laborCost = laborCost;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  @Override
  public String toString() {
    return "Order [orderNumber=" + orderNumber + ", customerName=" + customerName + ", state=" + state + ", taxRate="
        + taxRate + ", productType=" + productType + ", area=" + area + ", costPerSquareFoot=" + costPerSquareFoot
        + ", laborCostperSquareFoot=" + laborCostperSquareFoot + ", materialCost=" + materialCost + ", laborCost="
        + laborCost + ", tax=" + tax + ", total=" + total + "]";
  }
}
