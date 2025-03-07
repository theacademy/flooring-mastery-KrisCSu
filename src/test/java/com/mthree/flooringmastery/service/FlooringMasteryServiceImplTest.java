package com.mthree.flooringmastery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mthree.flooringmastery.dao.OrderDao;
import com.mthree.flooringmastery.dao.OrderDaoFileImpl;
import com.mthree.flooringmastery.dao.ProductDao;
import com.mthree.flooringmastery.dao.ProductDaoFileImpl;
import com.mthree.flooringmastery.dao.TaxDao;
import com.mthree.flooringmastery.dao.TaxDaoFileImpl;
import com.mthree.flooringmastery.model.Order;

class FlooringMasteryServiceImplTest {
    private static final String TEST_ORDERS_DIR = "src/test/resources/Orders/";
    private static final String TEST_PRODUCTS_FILE = "src/test/resources/Products.txt";
    private static final String TEST_TAXES_FILE = "src/test/resources/Taxes.txt";

    private FlooringMasteryService service;
    private Path backupOrdersDir;

    @BeforeEach
    void setUp() throws Exception {
        backupOrdersDir = Files.createTempDirectory("backupOrders");
        File ordersDir = new File(TEST_ORDERS_DIR);
        for (File file : ordersDir.listFiles()) {
            Files.copy(file.toPath(), backupOrdersDir.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
        }

        OrderDao orderDao = new OrderDaoFileImpl(TEST_ORDERS_DIR);
        ProductDao productDao = new ProductDaoFileImpl(TEST_PRODUCTS_FILE);
        TaxDao taxDao = new TaxDaoFileImpl(TEST_TAXES_FILE);
        service = new FlooringMasteryServiceImpl(orderDao, productDao, taxDao);
    }

    @AfterEach
    void tearDown() throws Exception {
        File ordersDir = new File(TEST_ORDERS_DIR);
        for (File file : ordersDir.listFiles()) {
            file.delete();
        }
        for (File backupFile : backupOrdersDir.toFile().listFiles()) {
            Files.copy(backupFile.toPath(), new File(TEST_ORDERS_DIR, backupFile.getName()).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Test
    void testAddValidOrder() throws Exception {
        Order newOrder = new Order(0);
        newOrder.setCustomerName("John Doe");
        newOrder.setState("TX");
        newOrder.setProductType("Tile");
        newOrder.setArea(new BigDecimal("150"));

        LocalDate date = LocalDate.of(2025, 5, 5);
        Order addedOrder = service.addOrder(date, newOrder);

        assertNotNull(addedOrder, "Order should not be null after being added.");
        assertEquals("John Doe", addedOrder.getCustomerName(), "Customer name should match.");
        assertEquals("TX", addedOrder.getState(), "State should match.");
        assertTrue(addedOrder.getTotal().compareTo(BigDecimal.ZERO) > 0, "Total should be greater than zero.");
    }

    @Test
    void testGetOrder() throws Exception {
        LocalDate date = LocalDate.of(2025, 5, 5);

        Order newOrder = new Order(0);
        newOrder.setCustomerName("Test User");
        newOrder.setState("TX");
        newOrder.setProductType("Tile");
        newOrder.setArea(new BigDecimal("150"));

        Order addedOrder = service.addOrder(date, newOrder);
        int orderNumber = addedOrder.getOrderNumber();

        Order order = service.getOrder(date, orderNumber);

        assertNotNull(order, "Order should be found.");
        assertEquals(orderNumber, order.getOrderNumber(), "Order number should match.");
    }

    @Test
    void testUpdateOrder() throws Exception {
        LocalDate date = LocalDate.of(2025, 5, 5);
    
        // Ensure an order exists before updating
        Order newOrder = new Order(0);
        newOrder.setCustomerName("Initial Name");
        newOrder.setState("CA");
        newOrder.setProductType("Wood");
        newOrder.setArea(new BigDecimal("200"));
    
        Order addedOrder = service.addOrder(date, newOrder);
    
        addedOrder.setCustomerName("Updated Name");
        Order updatedOrder = service.updateOrder(date, addedOrder);
    
        assertNotNull(updatedOrder, "Updated order should not be null.");
        assertEquals("Updated Name", updatedOrder.getCustomerName(), "Customer name should be updated.");
    }

    @Test
    void testRemoveOrder() throws Exception {
        LocalDate date = LocalDate.of(2025, 5, 5);
    
        Order newOrder = new Order(0);
        newOrder.setCustomerName("To Be Removed");
        newOrder.setState("CA");
        newOrder.setProductType("Carpet");
        newOrder.setArea(new BigDecimal("250"));
    
        Order addedOrder = service.addOrder(date, newOrder);
        int orderNumber = addedOrder.getOrderNumber(); // Get correct order number
    
        Order removedOrder = service.removeOrder(date, orderNumber);
        assertNotNull(removedOrder, "Order should be removed.");
    
        Order shouldBeNull = service.getOrder(date, orderNumber);
        assertNull(shouldBeNull, "Order should no longer exist.");
    }

    @Test
    void testAddOrderInvalidState() {
        Order order = new Order(0);
        order.setState("ZZ");

        assertThrows(FlooringMasteryValidationException.class, () -> service.addOrder(LocalDate.now(), order));
    }

    @Test
    void testAddOrderInvalidProduct() {
        Order order = new Order(0);
        order.setProductType("Unknown");

        assertThrows(FlooringMasteryValidationException.class, () -> service.addOrder(LocalDate.now(), order));
    }

    @Test
    void testAddOrderInvalidArea() {
        Order order = new Order(0);
        order.setArea(new BigDecimal("99"));

        assertThrows(FlooringMasteryValidationException.class, () -> service.addOrder(LocalDate.now(), order));
    }
}