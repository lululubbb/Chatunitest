package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LazilyParsedNumber_569_5Test {

  @Test
    @Timeout(8000)
  void testHashCode() {
    LazilyParsedNumber number = new LazilyParsedNumber("12345");
    int expectedHashCode = "12345".hashCode();
    assertEquals(expectedHashCode, number.hashCode());
  }
}