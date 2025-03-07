package com.mthree.flooringmastery.service;

import java.time.LocalDate;
import java.util.List;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;

public interface FlooringMasteryService {
  Order addOrder(LocalDate date, Order order)
      throws FlooringMasteryPersistenceException, FlooringMasteryValidationException;

  Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;

  List<Order> getOrdersByDate(LocalDate date) throws FlooringMasteryPersistenceException;

  Order updateOrder(LocalDate date, Order updatedOrder) throws FlooringMasteryPersistenceException;

  Order removeOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;

  List<Tax> getAllStates() throws FlooringMasteryPersistenceException;

  List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

  void calculateOrderCosts(Order order) throws FlooringMasteryPersistenceException;

  void exportOrders() throws FlooringMasteryPersistenceException;
}
