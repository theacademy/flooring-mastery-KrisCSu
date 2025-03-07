package com.mthree.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mthree.flooringmastery.model.Product;

public class ProductDaoFileImpl implements ProductDao {
  private final String productsFile;
  private static final String DELIMITER = ",";
  private Map<String, Product> products = new HashMap<>();

  public ProductDaoFileImpl() {
    this.productsFile = "src/main/resources/Data/Products.txt";
  }

  // Constructor for testing purposes
  public ProductDaoFileImpl(String testFilePath) {
    this.productsFile = testFilePath;
  }

  @Override
  public List<Product> getAllProducts() throws FlooringMasteryPersistenceException {
    loadProducts();
    return new ArrayList<>(products.values());
  }

  @Override
  public Product getProduct(String productType) throws FlooringMasteryPersistenceException {
    loadProducts();
    return products.get(productType);
  }

  private Product unmarshallProduct(String productAsText) {
    String[] productToken = productAsText.split(DELIMITER);
    String productType = productToken[0];
    Product productFromFile = new Product(productType);

    productFromFile.setCostPerSquareFoot(new BigDecimal(productToken[1]).setScale(2, RoundingMode.HALF_UP));
    productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productToken[2]).setScale(2, RoundingMode.HALF_UP));

    return productFromFile;
  }

  private void loadProducts() throws FlooringMasteryPersistenceException {
    products.clear();

    try (Scanner sc = new Scanner(new BufferedReader(new FileReader(productsFile)))) {
      if (sc.hasNextLine()) {
        sc.nextLine();
      }
      String currentLine;
      Product currentProduct;
      while (sc.hasNextLine()) {
        currentLine = sc.nextLine();
        currentProduct = unmarshallProduct(currentLine);

        products.put(currentProduct.getProductType(), currentProduct);
      }

    } catch (IOException e) {
      throw new FlooringMasteryPersistenceException("Could not product data", e);
    }
  }
}
