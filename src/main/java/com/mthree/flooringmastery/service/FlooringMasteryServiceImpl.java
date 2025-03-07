package com.mthree.flooringmastery.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.dao.OrderDao;
import com.mthree.flooringmastery.dao.ProductDao;
import com.mthree.flooringmastery.dao.TaxDao;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;

public class FlooringMasteryServiceImpl implements FlooringMasteryService {
  private OrderDao orderDao;
  private ProductDao productDao;
  private TaxDao taxDao;

  public FlooringMasteryServiceImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
    this.orderDao = orderDao;
    this.productDao = productDao;
    this.taxDao = taxDao;
  }

  @Override
  public Order addOrder(LocalDate date, Order order)
      throws FlooringMasteryPersistenceException, FlooringMasteryValidationException {
    validateOrder(order);
    calculateOrderCosts(order);
    return orderDao.addOrder(date, order);
  }

  @Override
  public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
    return orderDao.getOrder(date, orderNumber);
  }

  @Override
  public List<Order> getOrdersByDate(LocalDate date) throws FlooringMasteryPersistenceException {
    return orderDao.getOrderByDate(date);
  }

  @Override
  public List<Tax> getAllStates() throws FlooringMasteryPersistenceException {
    return taxDao.getAllStates();
  }

  @Override
  public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
    return productDao.getAllProducts();
  }

  @Override
  public Order updateOrder(LocalDate date, Order updatedOrder) throws FlooringMasteryPersistenceException {
    orderDao.updateOrder(date, updatedOrder);
    return updatedOrder;
  }

  @Override
  public Order removeOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
    return orderDao.removeOrder(date, orderNumber);
  }

  @Override
  public void exportOrders() throws FlooringMasteryPersistenceException {
    orderDao.exportOrders();
  }

  @Override
  public void calculateOrderCosts(Order order) throws FlooringMasteryPersistenceException {
    Product product = productDao.getProduct(order.getProductType());
    Tax tax = taxDao.getTax(order.getState());

    // Material Cost = Area * CostPerSquareFoot
    BigDecimal materialCost = order.getArea().multiply(product.getCostPerSquareFoot())
        .setScale(2, RoundingMode.HALF_UP);

    // Labor Cost = Area * LaborCostPerSquareFoot
    BigDecimal laborCost = order.getArea().multiply(product.getLaborCostPerSquareFoot())
        .setScale(2, RoundingMode.HALF_UP);

    // Tax = (MaterialCost + LaborCost) * (TaxRate / 100)
    BigDecimal taxAmount = (materialCost.add(laborCost))
        .multiply(tax.getTaxRate().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))
        .setScale(2, RoundingMode.HALF_UP);

    // Total Cost = MaterialCost + LaborCost + Tax
    BigDecimal totalCost = materialCost.add(laborCost).add(taxAmount)
        .setScale(2, RoundingMode.HALF_UP);

    order.setTaxRate(tax.getTaxRate());
    order.setCostPerSquareFoot(product.getCostPerSquareFoot());
    order.setLaborCostperSquareFoot(product.getLaborCostPerSquareFoot());
    order.setMaterialCost(materialCost);
    order.setLaborCost(laborCost);
    order.setTax(taxAmount);
    order.setTotal(totalCost);
  }

  private void validateOrder(Order order)
      throws FlooringMasteryValidationException, FlooringMasteryPersistenceException {
    validateCustomerName(order.getCustomerName());
    validateState(order.getState());
    validateProductType(order.getProductType());

    if (order.getArea().compareTo(new BigDecimal("100")) < 0) {
      throw new FlooringMasteryValidationException("Order area must be at least 100 sq ft.");
    }
  }

  private void validateState(String state)
      throws FlooringMasteryValidationException, FlooringMasteryPersistenceException {
    Tax tax = taxDao.getTax(state);
    if (tax == null) {
      throw new FlooringMasteryValidationException("Invalid state: We do not sell in " + state);
    }
  }

  private void validateCustomerName(String customerName) throws FlooringMasteryValidationException {
    if (customerName == null || customerName.trim().isEmpty()) {
      throw new FlooringMasteryValidationException("Customer name cannot be empty");
    }

    if (!customerName.matches("[a-zA-Z0-9., ]+")) {
      throw new FlooringMasteryValidationException(
          "Customer name can only contain letters, numbers, periods, and commas.");
    }
  }

  private void validateProductType(String productType)
      throws FlooringMasteryValidationException, FlooringMasteryPersistenceException {
    Product product = productDao.getProduct(productType);
    if (product == null) {
      throw new FlooringMasteryValidationException("Invalid product: " + productType + " is not available.");
    }
  }

}