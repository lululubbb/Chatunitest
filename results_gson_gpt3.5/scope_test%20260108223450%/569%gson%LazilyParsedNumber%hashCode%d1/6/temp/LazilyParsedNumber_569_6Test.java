package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LazilyParsedNumber_569_6Test {

  @Test
    @Timeout(8000)
  void testHashCode() {
    String val = "12345.6789";
    LazilyParsedNumber lpn = new LazilyParsedNumber(val);
    assertEquals(val.hashCode(), lpn.hashCode());
  }
}