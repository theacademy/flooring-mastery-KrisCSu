package com.mthree.flooringmastery.dao;

import java.util.List;

import com.mthree.flooringmastery.model.Product;

public interface ProductDao {
  List<Product> getAllProducts() throws FlooringMasteryPersistenceException;

  Product getProduct(String productType) throws FlooringMasteryPersistenceException;
}
