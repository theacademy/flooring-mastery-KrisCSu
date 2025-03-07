package com.mthree.flooringmastery.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mthree.flooringmastery.model.Tax;

class TaxDaoFileImplTest {
  private TaxDao testDao;
  private static final String TEST_FILE = "src/test/resources/Taxes.txt";

  @BeforeEach
  void setUp() {
    testDao = new TaxDaoFileImpl(TEST_FILE);
  }

  @Test
  void testGetAllStates() throws FlooringMasteryPersistenceException {
    List<Tax> taxes = testDao.getAllStates();

    assertNotNull(taxes, "The list of tax states must not be null.");
    assertEquals(4, taxes.size(), "List of tax states should contain 4 entries.");

    Tax texas = taxes.stream().filter(t -> t.getStateAbbreviation().equals("TX")).findFirst().orElse(null);
    assertNotNull(texas, "Texas should be in the list.");
    assertEquals("Texas", texas.getStateName());
    assertEquals(new BigDecimal("4.45"), texas.getTaxRate());

    Tax california = taxes.stream().filter(t -> t.getStateAbbreviation().equals("CA")).findFirst().orElse(null);
    assertNotNull(california, "California should be in the list.");
    assertEquals("Calfornia", california.getStateName()); // Intentionally testing the typo in the data file
    assertEquals(new BigDecimal("25.00"), california.getTaxRate());
  }

  @Test
  void testGetTax_Valid() throws FlooringMasteryPersistenceException {
    Tax tax = testDao.getTax("KY");
    assertNotNull(tax, "Kentucky should be found.");
    assertEquals("Kentucky", tax.getStateName());
    assertEquals(new BigDecimal("6.00"), tax.getTaxRate());
  }

  @Test
  void testGetTax_Invalid() throws FlooringMasteryPersistenceException {
    Tax tax = testDao.getTax("ZZ");
    assertNull(tax, "ZZ should not be found in the tax records.");
  }
}
