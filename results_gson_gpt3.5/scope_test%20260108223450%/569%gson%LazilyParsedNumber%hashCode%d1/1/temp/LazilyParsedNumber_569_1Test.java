package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class LazilyParsedNumber_569_1Test {

  @Test
    @Timeout(8000)
  void testHashCode_sameValue() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("123.45");
    LazilyParsedNumber num2 = new LazilyParsedNumber("123.45");
    // hashCode depends on value string's hashCode, so equal strings produce equal hashCodes
    assertEquals(num1.hashCode(), num2.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_differentValue() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("123.45");
    LazilyParsedNumber num2 = new LazilyParsedNumber("543.21");
    // Different strings => very likely different hashCodes
    assertNotEquals(num1.hashCode(), num2.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_consistent() {
    LazilyParsedNumber num = new LazilyParsedNumber("0");
    int hash1 = num.hashCode();
    int hash2 = num.hashCode();
    assertEquals(hash1, hash2);
  }
}