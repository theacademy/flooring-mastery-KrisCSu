package com.mthree.flooringmastery.controller;

import java.time.LocalDate;
import java.util.List;

import com.mthree.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.FlooringMasteryService;
import com.mthree.flooringmastery.ui.FlooringMasteryView;

public class FlooringMasteryController {
  private final FlooringMasteryView view;
  private final FlooringMasteryService service;

  public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryService service) {
    this.view = view;
    this.service = service;
  }

  public void run() {
    boolean keepGoing = true;

    while (keepGoing) {
      int menuSelection = view.printMenuAndGetSelection();

      switch (menuSelection) {
        case 1 -> displayOrders();
        case 2 -> addOrder();
        case 3 -> editOrder();
        case 4 -> removeOrder();
        case 5 -> exportOrders();
        case 6 -> {
          keepGoing = false;
          view.displayByeBanner();
        }

        default -> view.displayWarningBanner("Invalid selection, please try again.");
      }
    }
  }

  private void displayOrders() {
    LocalDate date = view.getOrderDate();
    try {
      List<Order> orders = service.getOrdersByDate(date);
      view.displayOrders(orders);
    } catch (FlooringMasteryPersistenceException e) {
      view.displayErrorMessage("Could not load orders");
    }
  }

  private void addOrder() {
    try {
      List<Tax> states = service.getAllStates();
      List<Product> products = service.getAllProducts();
      LocalDate date = view.getFutureOrderDate();
      Order order = view.getNewOrderDetails(states, products);
      
      service.calculateOrderCosts(order);
      view.displayTempOrder(order);
      int confirm = view.confirmAddition();
      if (confirm == 1) {
        service.addOrder(date, order);
        view.displaySuccessMessage("Order added successfully.");
      } else {
        view.displayInfo("New Order Cancelled.");
      }
    } catch (Exception e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  private void editOrder() {
    view.displayEditOrderBanner();
    LocalDate date = view.getOrderDate();
    int orderNumber = view.io.readInt("Enter the order number:");

    try {
        // Check if order exists
        Order existingOrder = service.getOrder(date, orderNumber);
        if (existingOrder == null) {
            view.displayErrorMessage("Order not found.");
            return;
        }

        // Get state & product lists
        List<Tax> states = service.getAllStates();
        List<Product> products = service.getAllProducts();

        // Get updated order details from user input
        Order updatedOrder = view.getUpdatedOrderDetails(existingOrder, states, products);

        // Recalculate costs based on updated info
        service.calculateOrderCosts(updatedOrder);
        view.displayOrderSummary(updatedOrder);

        int confirm = view.confirmUpdate();
        if (confirm == 1) {
          // Save the updated order
          service.updateOrder(date, updatedOrder);
          view.displayOrderUpdatedSuccessBanner();
        } else {
          view.displayOrderUpdateCancelledBanner();
        }

    } catch (FlooringMasteryPersistenceException e) {
        view.displayErrorMessage("Could not update order.");
    }
}

  private void removeOrder() {
    view.displayRemoveOrderBanner();
    LocalDate date = view.getOrderDate();
    int orderNumber = view.io.readInt("Enter the order number:");

    try {
      Order order = service.getOrder(date, orderNumber);
      if (order == null) {
        view.displayErrorMessage("Order not found.");
        return;
      }
      view.displayOrderSummary(order);
      int confirmation = view.confirmRemoval();
      if (confirmation == 1) {
        service.removeOrder(date, orderNumber);
        view.displaySuccessMessage("Order successfully removed.\n");
      } else {
        view.displayInfo("Order removal cancelled.\n");
      }
    } catch (Exception e) {
      view.displayErrorMessage("Error: " + e.getMessage());
    }
  }

  private void exportOrders(){
    try {
      service.exportOrders();
      view.displayExportSuccessBanner();
    } catch (FlooringMasteryPersistenceException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }
}
