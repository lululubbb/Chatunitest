package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LazilyParsedNumber_569_3Test {

  @Test
    @Timeout(8000)
  void testHashCode() {
    String val = "123.45";
    LazilyParsedNumber lpn = new LazilyParsedNumber(val);
    assertEquals(val.hashCode(), lpn.hashCode());
  }
}