package com.mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {

  Scanner sc = new Scanner(System.in);

  @Override
  public void print(String message) {
    System.out.println(message);
  }

  @Override
  public double readDouble(String prompt) {
    print(prompt);
    return Double.parseDouble(sc.nextLine());
  }

  @Override
  public double readDouble(String prompt, double min, double max) {
    double num;
    do {
      print(prompt);
      num = Double.parseDouble(sc.nextLine());
    } while (num < min || num > max);

    return num;
  }

  @Override
  public float readFloat(String prompt) {
    print(prompt);
    return Float.parseFloat(sc.nextLine());
  }

  @Override
  public float readFloat(String prompt, float min, float max) {
    float num;
    do {
      print(prompt);
      num = Float.parseFloat(sc.nextLine());
    } while (num < min || num > max);

    return num;
  }

  @Override
  public int readInt(String prompt) {
    print(prompt);
    return Integer.parseInt(sc.nextLine());
  }

  @Override
  public int readInt(String prompt, int min, int max) {
    int num;
    while (true){
      try {
        print(prompt);
            num = Integer.parseInt(sc.nextLine().trim());

            if (num >= min && num <= max) {
                return num;
            } else {
                print("Invalid number! Please enter a number between " + min + " and " + max + ".");
            }
      } catch (Exception e) {
        print("Invalid input! Try again!");
      }
    }
  }

  @Override
  public long readLong(String prompt) {

    print(prompt);
    return Long.parseLong(sc.nextLine());
  }

  @Override
  public long readLong(String prompt, long min, long max) {
    long num;
    do {
      print(prompt);
      num = Long.parseLong(sc.nextLine());
    } while (num < min || num > max);

    return num;
  }

  @Override
  public String readString(String prompt) {
    print(prompt);
    return sc.nextLine();
  }

  @Override
    public BigDecimal readBigDecimal(String prompt) {
        print(prompt);
        while (true) {
            try {
                return new BigDecimal(sc.nextLine()).setScale(2, RoundingMode.HALF_UP);
            } catch (NumberFormatException e) {
                print("Invalid input. Please enter a valid decimal number.");
            }
        }
    }


    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min) {
        BigDecimal num;
        do {
            num = readBigDecimal(prompt + " (Minimum: " + min + "):");
            if (num.compareTo(min) < 0){
              print("Invalid number! Try again!");
            }
        } while (num.compareTo(min) < 0);
        return num;
    }
}