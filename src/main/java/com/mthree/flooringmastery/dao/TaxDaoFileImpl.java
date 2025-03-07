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

import com.mthree.flooringmastery.model.Tax;

public class TaxDaoFileImpl implements TaxDao {
  private String taxFile;
  private static final String DELIMITER = ",";
  private Map<String, Tax> taxes = new HashMap<>();

  public TaxDaoFileImpl() {
    this.taxFile = "src/main/resources/Data/Taxes.txt";
  }

  public TaxDaoFileImpl(String testFilePath) {
    this.taxFile = testFilePath;
  }

  @Override
  public List<Tax> getAllStates() throws FlooringMasteryPersistenceException {
    loadTaxes();
    return new ArrayList<>(taxes.values());
  }

  @Override
  public Tax getTax(String stateAbbreviation) throws FlooringMasteryPersistenceException {
    loadTaxes();
    return taxes.get(stateAbbreviation);
  }

  private Tax unmarshallTax(String taxAsText) {
    String[] taxToken = taxAsText.split(DELIMITER);
    String state = taxToken[0];
    Tax taxFromFile = new Tax(state);

    taxFromFile.setStateName(taxToken[1]);
    taxFromFile.setTaxRate(new BigDecimal(taxToken[2].trim()).setScale(2, RoundingMode.HALF_UP));

    return taxFromFile;
  }

  private void loadTaxes() throws FlooringMasteryPersistenceException {
    taxes.clear();

    try (Scanner sc = new Scanner(new BufferedReader(new FileReader(taxFile)))) {
      if (sc.hasNextLine()) {
        sc.nextLine(); // skip the header
      }
      String currentLine;
      Tax currentTax;
      while (sc.hasNextLine()) {
        currentLine = sc.nextLine();
        currentTax = unmarshallTax(currentLine);

        taxes.put(currentTax.getStateAbbreviation(), currentTax);
      }
    } catch (IOException e) {
      throw new FlooringMasteryPersistenceException("Could not load tax data", e);
    }
  }
}
