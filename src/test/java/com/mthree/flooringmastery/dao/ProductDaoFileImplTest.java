package com.mthree.flooringmastery.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mthree.flooringmastery.model.Product;

class ProductDaoFileImplTest {
  private ProductDao testDao;
  private static final String TEST_FILE = "src/test/resources/Products.txt";

  @BeforeEach
  void setUp() {
    testDao = new ProductDaoFileImpl(TEST_FILE);
  }

  @Test
  void testGetAllProducts() throws FlooringMasteryPersistenceException {
    List<Product> products = testDao.getAllProducts();

    assertNotNull(products, "The list of products must not be null");
    assertEquals(4, products.size(), "List of products should have 4 products.");

    Product carpet = products.stream().filter(p -> p.getProductType().equals("Carpet")).findFirst().orElse(null);
    assertNotNull(carpet, "Carpet should be in the list.");
    assertEquals(new BigDecimal("2.25").setScale(2, RoundingMode.HALF_UP), carpet.getCostPerSquareFoot(), "Carpet cost should be 2.25");
    assertEquals(new BigDecimal("2.10").setScale(2, RoundingMode.HALF_UP), carpet.getLaborCostPerSquareFoot(), "Carpet labor cost should be 2.10");

    Product tile = products.stream().filter(p -> p.getProductType().equals("Tile")).findFirst().orElse(null);
    assertNotNull(tile, "Tile should be in the list.");
    assertEquals(new BigDecimal("3.50").setScale(2, RoundingMode.HALF_UP), tile.getCostPerSquareFoot(), "Tile cost should be 3.50");
    assertEquals(new BigDecimal("4.15").setScale(2, RoundingMode.HALF_UP), tile.getLaborCostPerSquareFoot(), "Tile labor cost should be 4.15");
  }

  @Test
  void testGetProduct() throws FlooringMasteryPersistenceException {
    Product carpet = testDao.getProduct("Carpet");
    assertNotNull(carpet, "Carpet should be found.");
    assertEquals(new BigDecimal("2.25").setScale(2, RoundingMode.HALF_UP), carpet.getCostPerSquareFoot(), "Carpet cost should be 2.25");
    assertEquals(new BigDecimal("2.10").setScale(2, RoundingMode.HALF_UP), carpet.getLaborCostPerSquareFoot(), "Carpet labor cost should be 2.10");

    Product wood = testDao.getProduct("Wood");
    assertNotNull(wood, "Wood should be found.");
    assertEquals(new BigDecimal("5.15").setScale(2, RoundingMode.HALF_UP), wood.getCostPerSquareFoot(), "Wood cost should be 5.15");
    assertEquals(new BigDecimal("4.75").setScale(2, RoundingMode.HALF_UP), wood.getLaborCostPerSquareFoot(), "Wood labor cost should be 4.75");

    Product nonExistent = testDao.getProduct("Stone");
    assertNull(nonExistent, "Stone should not be found.");
  }
}
