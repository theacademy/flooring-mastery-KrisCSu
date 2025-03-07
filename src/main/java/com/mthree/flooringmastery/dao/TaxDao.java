package com.mthree.flooringmastery.dao;

import java.util.List;

import com.mthree.flooringmastery.model.Tax;

public interface TaxDao {
  List<Tax> getAllStates() throws FlooringMasteryPersistenceException;

  Tax getTax(String stateAbbreviation) throws FlooringMasteryPersistenceException;
}
