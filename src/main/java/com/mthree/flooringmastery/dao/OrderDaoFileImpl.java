package com.mthree.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mthree.flooringmastery.model.Order;

public class OrderDaoFileImpl implements OrderDao {
  private final String ordersPath;
  private static final String DELIMITER = ",";
  private Map<LocalDate, Map<Integer, Order>> allOrders = new HashMap<>();
  private int maxOrderNum = 0;

  public OrderDaoFileImpl() throws FlooringMasteryPersistenceException {
    this("src/main/resources/Data/Orders/");
  }

  public OrderDaoFileImpl(String testOrdersPath) throws FlooringMasteryPersistenceException {
    this.ordersPath = testOrdersPath;
    loadAllOrders();
  }

  private void loadAllOrders() throws FlooringMasteryPersistenceException {
    File ordersDir = new File(ordersPath);
    if (!ordersDir.exists() || !ordersDir.isDirectory()) {
      throw new FlooringMasteryPersistenceException("Orders directory not found.");
    }

    File[] orderFiles = ordersDir.listFiles();
    if (orderFiles == null)
      return;

    for (File file : orderFiles) {
      try (Scanner sc = new Scanner(new BufferedReader(new FileReader(file)))) {
        if (sc.hasNextLine())
          sc.nextLine();

        String fileName = file.getName();
        LocalDate date = LocalDate.parse(fileName.replace("Orders_", "").replace(".txt", ""),
            DateTimeFormatter.ofPattern("MMddyyyy"));

        Map<Integer, Order> ordersForDate = new HashMap<>();

        while (sc.hasNextLine()) {
          Order order = unmarshallOrder(sc.nextLine());
          ordersForDate.put(order.getOrderNumber(), order);

          maxOrderNum = Math.max(maxOrderNum, order.getOrderNumber());
        }
        allOrders.put(date, ordersForDate);
      } catch (IOException e) {
        throw new FlooringMasteryPersistenceException("Could not load orders from " + file.getName(), e);
      }
    }
  }

  @Override
  public Order addOrder(LocalDate date, Order order) throws FlooringMasteryPersistenceException {
    maxOrderNum++;
    order.setOrderNumber(maxOrderNum);
    if (!allOrders.containsKey(date)) {
      allOrders.put(date, new HashMap<>());
    }

    allOrders.get(date).put(maxOrderNum, order);
    writeOrders(date);
    return order;
  }

  @Override
  public Order getOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
    return allOrders.getOrDefault(date, new HashMap<>()).get(orderNumber);
  }

  @Override
  public List<Order> getOrderByDate(LocalDate date) throws FlooringMasteryPersistenceException {
    return new ArrayList<>(allOrders.getOrDefault(date, new HashMap<>()).values());
  }

  @Override
  public Order updateOrder(LocalDate date, Order updatedOrder) throws FlooringMasteryPersistenceException {
    if (allOrders.containsKey(date) && allOrders.get(date).containsKey(updatedOrder.getOrderNumber())) {
      allOrders.get(date).put(updatedOrder.getOrderNumber(), updatedOrder);
      writeOrders(date);
      return updatedOrder;
    }
    return null;
  }

  @Override
  public Order removeOrder(LocalDate date, int orderNumber) throws FlooringMasteryPersistenceException {
    if (allOrders.containsKey(date)) {
      Order removedOrder = allOrders.get(date).remove(orderNumber);
      writeOrders(date);
      return removedOrder;
    }
    return null;
  }

  @Override
  public void exportOrders() throws FlooringMasteryPersistenceException {
    String fileName = "src/main/resources/Data/Backup/DataExport.txt";
    try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
      out.println(
          "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,Date");

      for (Map.Entry<LocalDate, Map<Integer, Order>> entry : allOrders.entrySet()) {
        LocalDate date = entry.getKey();
        Map<Integer, Order> orders = entry.getValue();

        orders.values().stream()
            .sorted(Comparator.comparingInt(Order::getOrderNumber))
            .forEach(o -> out
                .println(marshallOrder(o) + DELIMITER + date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
      }
    } catch (IOException e) {
      throw new FlooringMasteryPersistenceException("Could not export orders", e);
    }
  }

  private Order unmarshallOrder(String orderAstext) {
    String[] orderToken = orderAstext.split(DELIMITER);
    int orderId = Integer.parseInt(orderToken[0]);
    Order order = new Order(orderId);

    order.setCustomerName(orderToken[1].replace("\\c", ","));
    order.setState(orderToken[2]);
    order.setTaxRate(new BigDecimal(orderToken[3]).setScale(2, RoundingMode.HALF_UP));
    order.setProductType(orderToken[4]);
    order.setArea(new BigDecimal(orderToken[5]).setScale(2, RoundingMode.HALF_UP));
    order.setCostPerSquareFoot(new BigDecimal(orderToken[6]).setScale(2, RoundingMode.HALF_UP));
    order.setLaborCostperSquareFoot(new BigDecimal(orderToken[7]).setScale(2, RoundingMode.HALF_UP));
    order.setMaterialCost(new BigDecimal(orderToken[8]).setScale(2, RoundingMode.HALF_UP));
    order.setLaborCost(new BigDecimal(orderToken[9]).setScale(2, RoundingMode.HALF_UP));
    order.setTax(new BigDecimal(orderToken[10]).setScale(2, RoundingMode.HALF_UP));
    order.setTotal(new BigDecimal(orderToken[11]).setScale(2, RoundingMode.HALF_UP));

    return order;
  }

  // helper method to get file name based on the orders folder path and date
  private String getFileName(LocalDate date) {
    return ordersPath + "Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
  }

  private String marshallOrder(Order order) {
    String escapedName = order.getCustomerName().replace(",", "\\c");
    return order.getOrderNumber() + DELIMITER +
        escapedName + DELIMITER +
        order.getState() + DELIMITER +
        order.getTaxRate() + DELIMITER +
        order.getProductType() + DELIMITER +
        order.getArea() + DELIMITER +
        order.getCostPerSquareFoot() + DELIMITER +
        order.getLaborCostperSquareFoot() + DELIMITER +
        order.getMaterialCost() + DELIMITER +
        order.getLaborCost() + DELIMITER +
        order.getTax() + DELIMITER +
        order.getTotal();
  }

  private void writeOrders(LocalDate date) throws FlooringMasteryPersistenceException {
    String fileName = getFileName(date);

    try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
      out.println(
          "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

      for (Order order : allOrders.getOrDefault(date, new HashMap<>()).values()) {
        out.println(marshallOrder(order));
      }
    } catch (IOException e) {
      throw new FlooringMasteryPersistenceException("Could not save order data", e);
    }
  }
}
