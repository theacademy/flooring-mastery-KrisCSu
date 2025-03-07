package com.mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.FlooringMasteryService;

public class FlooringMasteryView {
  public UserIO io;
  public FlooringMasteryService service;

  public FlooringMasteryView(UserIO io) {
    this.io = io;
  }

  public int printMenuAndGetSelection() {
    io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    io.print("* <<Flooring Program>>");
    io.print("* 1. Display Orders");
    io.print("* 2. Add an Order");
    io.print("* 3. Edit an Order");
    io.print("* 4. Remove an Order");
    io.print("* 5. Export All Data");
    io.print("* 6. Quit");
    io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

    return io.readInt("Please select from above:", 1, 6);
  }

  // used for search existing orders
  public LocalDate getOrderDate() {
    while (true) {
      try {
        String dateInput = io.readString("Enter order date (MM/DD/YYYY):");
        return LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
      } catch (Exception e) {
        io.print("Invalid date! Please make sure your input matches the format!!!");
      }
    }
  }

  // used for adding new orders
  public LocalDate getFutureOrderDate() {
    while (true) {
      try {
        String input = io.readString("Enter order date (MM/DD/YYYY):");
        LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        if (date.isBefore(LocalDate.now())) {
          io.print("Order date must be in the future.");
          continue;
        }
        return date;
      } catch (Exception e) {
        io.print("Invalid date format. Please enter again.");
      }
    }
  }

  public void displayOrders(List<Order> orders) {
    if (orders.isEmpty()) {
      io.print("No orders found.");
    } else {
      io.print("All Orders:");
      orders.forEach(this::displayOrderSummary);
    }
    io.readString("Press Enter to continue...");
  }

  public void displayOrderSummary(Order order) {
    io.print("\n* * * * * * * * * * * * * Order Summary * * * * * * * * * * * * *");
    io.print("* Order Number: " + order.getOrderNumber());
    io.print("* Customer Name: " + order.getCustomerName());
    io.print("* State: " + order.getState());
    io.print("* Tax Rate: " + order.getTaxRate() + "%");
    io.print("* Product Type: " + order.getProductType());
    io.print("* Area: " + order.getArea() + " sq ft");
    io.print("* Material Cost: $" + order.getMaterialCost());
    io.print("* Labor Cost: $" + order.getLaborCost());
    io.print("* Tax: $" + order.getTax());
    io.print("* Total: $" + order.getTotal());
    io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
  }

  public void displayTempOrder(Order order) {
    io.print("\n* * * * * * * * * * * * * Order Summary * * * * * * * * * * * * *");
    io.print("* Customer Name: " + order.getCustomerName());
    io.print("* State: " + order.getState());
    io.print("* Tax Rate: " + order.getTaxRate() + "%");
    io.print("* Product Type: " + order.getProductType());
    io.print("* Area: " + order.getArea() + " sq ft");
    io.print("* Material Cost: $" + order.getMaterialCost());
    io.print("* Labor Cost: $" + order.getLaborCost());
    io.print("* Tax: $" + order.getTax());
    io.print("* Total: $" + order.getTotal());
    io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
  }

  public void displayAddOrderBanner() {
    io.print("\n* * * * * * Add Order * * * * * *");
  }

  public void displayEditOrderBanner() {
    io.print("\n* * * * * * Edit Order * * * * * *");
  }

  public void displayRemoveOrderBanner() {
    io.print("\n* * * * * * Remove Order * * * * * *");
  }

  public void displayExportSuccessBanner(){
    io.print("\n* * * Exported Data Successfully * * *\n");
  }

  public void displayOrderUpdatedSuccessBanner(){
    io.print("* * * Order Updated successfully * * *\n");
  }

  public void displayOrderUpdateCancelledBanner(){
    io.print("* * * Order Update cancelled. * * *");
  }

  public void displayWarningBanner(String message){
    io.print("* * *" + message + "* * *");
  }

  public void displayErrorMessage(String message) {
    io.print("\nERROR: " + message);
  }

  public void displaySuccessMessage(String message){
    io.print("\nSUCCESS: " + message);
  }

  public void displayInfo(String message){
    io.print("\nINFO: " + message);
  }

  public void displayByeBanner() {
    io.print(" ____               _ ");
    io.print("|  _ \\             | |");
    io.print("| |_) |_   _  ___  | |");
    io.print("|  _ <| | | |/ _ \\ | |");
    io.print("| |_) | |_| |  __/ |_|");
    io.print("|____/ \\__, |\\___| (_)");
    io.print("        __/ |         ");
    io.print("       |___/          ");
  }

  public Order getNewOrderDetails(List<Tax> stateList, List<Product> productList) {
    String customerName;
    do {
      customerName = io.readString("Enter customer name:");
      if (customerName.isEmpty()) {
        io.print("Customer name cannot be empty!");
      } else if (!customerName.matches("[a-zA-Z0-9., ]+")) {
        io.print("Customer name can only contain letters, numbers, periods, and commas.");
      }
    } while (customerName.isEmpty());

    // list available states for selection
    io.print("\nSelect a State:");
    for (int i = 0; i < stateList.size(); i++) {
      io.print((i + 1) + ". " + stateList.get(i).getStateAbbreviation() + " - " + stateList.get(i).getStateName());
    }
    int stateChoice;
    while (true) {
      try {
        stateChoice = io.readInt("Enter the number:", 1, stateList.size());
        break;
      } catch (NumberFormatException e) {
        io.print("Please enter a valid number from above!");
      }
    }
    String state = stateList.get(stateChoice - 1).getStateAbbreviation();

    // list all available products for selection
    io.print("\nSelect a Product Type:");
    for (int i = 0; i < productList.size(); i++) {
      io.print((i + 1) + ". " + productList.get(i).getProductType());
    }

    int productChoice;
    while (true) {
      try {
        productChoice = io.readInt("Enter the number:", 1, productList.size());
        break;
      } catch (Exception e) {
        io.print("Please enter a valid number from above!");
      }
    }
    String productType = productList.get(productChoice - 1).getProductType();

    BigDecimal area = io.readBigDecimal("Enter area (minimum 100 sq ft):", new BigDecimal("100"));

    Order newOrder = new Order(0);
    newOrder.setCustomerName(customerName);
    newOrder.setState(state);
    newOrder.setProductType(productType);
    newOrder.setArea(area);

    return newOrder;
  }

  public Order getUpdatedOrderDetails(Order existingOrder, List<Tax> stateList, List<Product> productList) {
    io.print("\n* * * * * * Editing Order #" + existingOrder.getOrderNumber() + " * * * * * *");

    // Customer Name (Press Enter to Keep Existing)
    String customerName = io
        .readString("Enter new customer name (or press Enter to keep: " + existingOrder.getCustomerName() + "):");
    if (customerName.trim().isEmpty()) {
      customerName = existingOrder.getCustomerName();
    }

    io.print("\nSelect a State:");
    for (int i = 0; i < stateList.size(); i++) {
      io.print((i + 1) + ". " + stateList.get(i).getStateAbbreviation() + " - " + stateList.get(i).getStateName());
    }
    String state = existingOrder.getState();
    String stateInput = io.readString("Enter state number (or press Enter to keep: " + existingOrder.getState() + "):");
    if (!stateInput.trim().isEmpty()) {
      try {
        int stateChoice = Integer.parseInt(stateInput);
        if (stateChoice >= 1 && stateChoice <= stateList.size()) {
          state = stateList.get(stateChoice - 1).getStateAbbreviation();
        } else {
          io.print("Invalid selection. Keeping existing state.");
        }
      } catch (NumberFormatException e) {
        io.print("Invalid input. Keeping existing state.");
      }
    }

    io.print("\nSelect a Product Type:");
    for (int i = 0; i < productList.size(); i++) {
      io.print((i + 1) + ". " + productList.get(i).getProductType());
    }
    String productType = existingOrder.getProductType();
    String productInput = io
        .readString("Enter product number (or press Enter to keep: " + existingOrder.getProductType() + "):");
    if (!productInput.trim().isEmpty()) {
      try {
        int productChoice = Integer.parseInt(productInput);
        if (productChoice >= 1 && productChoice <= productList.size()) {
          productType = productList.get(productChoice - 1).getProductType();
        } else {
          io.print("Invalid selection. Keeping existing product type.");
        }
      } catch (NumberFormatException e) {
        io.print("Invalid input. Keeping existing product type.");
      }
    }

    String areaInput = io
        .readString("Enter area (minimum 100 sq ft) (or press Enter to keep: " + existingOrder.getArea() + "):");
    BigDecimal area = existingOrder.getArea();
    if (!areaInput.trim().isEmpty()) {
      try {
        BigDecimal newArea = new BigDecimal(areaInput);
        if (newArea.compareTo(new BigDecimal("100")) >= 0) {
          area = newArea;
        } else {
          io.print("Invalid area. Must be at least 100 sq ft. Keeping existing area.");
        }
      } catch (NumberFormatException e) {
        io.print("Invalid input. Keeping existing area.");
      }
    }

    Order updatedOrder = new Order(existingOrder.getOrderNumber());
    updatedOrder.setCustomerName(customerName);
    updatedOrder.setState(state);
    updatedOrder.setProductType(productType);
    updatedOrder.setArea(area);

    return updatedOrder;
  }

  public int confirmAddition() {
    io.print("\nDo you want to add this order?");
    io.print("1. Yes");
    io.print("2. No");
    return io.readInt("Enter your choice", 1, 2);
  }

  public int confirmUpdate(){
    io.print("\nDo you want to update this order?");
    io.print("1. Yes");
    io.print("2. No");
    return io.readInt("Enter your choice", 1, 2);
  }

  public int confirmRemoval() {
    io.print("\nAre you sure you want to remove this order?");
    io.print("1. Yes");
    io.print("2. No");
    return io.readInt("Enter your choice:", 1, 2);
  }
}
