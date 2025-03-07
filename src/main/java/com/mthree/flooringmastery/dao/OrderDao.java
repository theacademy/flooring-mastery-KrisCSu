package com.mthree.flooringmastery.dao;

import java.time.LocalDate;
import java.util.List;

import com.mthree.flooringmastery.model.Order;

public interface OrderDao {
  Order addOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException;
  Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;
  List<Order> getOrderByDate(LocalDate date) throws FlooringMasteryPersistenceException;
  Order updateOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException;
  Order removeOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException;
  void exportOrders() throws FlooringMasteryPersistenceException;
}
