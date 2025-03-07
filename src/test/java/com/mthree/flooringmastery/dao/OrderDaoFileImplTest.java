package com.mthree.flooringmastery.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mthree.flooringmastery.model.Order;

class OrderDaoFileImplTest {
  private OrderDao testDao;
  private static final String TEST_ORDERS_PATH = "src/test/Orders";

  @BeforeEach
  void setUp() throws FlooringMasteryPersistenceException {
    testDao = new OrderDaoFileImpl(TEST_ORDERS_PATH);
  }

  @Test
  void testLoadOrders() throws FlooringMasteryPersistenceException {
    List<Order> orders = testDao.getOrderByDate(LocalDate.of(2013, 6, 1));
    assertEquals(1, orders.size(), "Should have 1 order on 06/01/2013.");

    Order adaOrder = orders.get(0);
    assertEquals("Ada Lovelace", adaOrder.getCustomerName());
    assertEquals(new BigDecimal("2381.06"), adaOrder.getTotal());
  }

  @Test
  void testAddOrder() throws FlooringMasteryPersistenceException {
    Order newOrder = new Order(0);
    newOrder.setCustomerName("New Customer");
    newOrder.setState("TX");
    newOrder.setProductType("Tile");
    newOrder.setArea(new BigDecimal("300"));

    LocalDate date = LocalDate.of(2013, 6, 3);
    testDao.addOrder(date, newOrder);

    List<Order> orders = testDao.getOrderByDate(date);
    assertEquals(1, orders.size(), "Should contain 1 order for 06/03/2013.");
  }

  @Test
  void testEditOrder() throws FlooringMasteryPersistenceException {
    // Given: Load an order from test file
    LocalDate date = LocalDate.parse("06/01/2013", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    List<Order> orders = testDao.getOrderByDate(date);
    assertFalse(orders.isEmpty(), "Orders should be loaded for 06/01/2013.");

    Order orderToEdit = orders.get(0);
    int orderNumber = orderToEdit.getOrderNumber();

    // When: Modify order details
    orderToEdit.setCustomerName("Alan Turing");
    orderToEdit.setProductType("Wood");
    orderToEdit.setArea(new BigDecimal("300"));

    // Update the order in the DAO
    testDao.updateOrder(date, orderToEdit);

    // Then: Retrieve order and verify changes
    Order updatedOrder = testDao.getOrder(date, orderNumber);
    assertNotNull(updatedOrder, "Updated order should exist.");
    assertEquals("Alan Turing", updatedOrder.getCustomerName(), "Customer name should be updated.");
    assertEquals("Wood", updatedOrder.getProductType(), "Product type should be updated.");
    assertEquals(new BigDecimal("300"), updatedOrder.getArea(), "Area should be updated.");
  }

  @Test
  void testRemoveOrder() throws FlooringMasteryPersistenceException {
    LocalDate date = LocalDate.of(2013, 6, 2);
    Order removedOrder = testDao.removeOrder(date, 2);
    assertNotNull(removedOrder, "Order should be removed.");

    List<Order> orders = testDao.getOrderByDate(date);
    assertEquals(1, orders.size(), "Should contain only 1 order after removal.");
  }
}