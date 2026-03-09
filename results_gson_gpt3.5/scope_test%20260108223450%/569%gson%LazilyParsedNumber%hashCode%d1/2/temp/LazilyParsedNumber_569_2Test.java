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

class LazilyParsedNumber_569_2Test {

  @Test
    @Timeout(8000)
  void testHashCode_sameValue_sameHashCode() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("12345");
    LazilyParsedNumber num2 = new LazilyParsedNumber("12345");
    assertEquals(num1.hashCode(), num2.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_differentValue_differentHashCode() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("12345");
    LazilyParsedNumber num2 = new LazilyParsedNumber("54321");
    // It's possible for hash collisions, but for different strings hashCode usually differs
    assertNotEquals(num1.hashCode(), num2.hashCode());
  }

  @Test
    @Timeout(8000)
  void testHashCode_consistent() {
    LazilyParsedNumber num = new LazilyParsedNumber("987654321");
    int firstHash = num.hashCode();
    int secondHash = num.hashCode();
    assertEquals(firstHash, secondHash);
  }
}