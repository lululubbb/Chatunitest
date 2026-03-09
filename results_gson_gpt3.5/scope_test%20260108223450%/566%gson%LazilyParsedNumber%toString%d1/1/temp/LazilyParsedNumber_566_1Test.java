package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_566_1Test {

  @Test
    @Timeout(8000)
  public void testToString() {
    String val = "123.45";
    LazilyParsedNumber lpn = new LazilyParsedNumber(val);
    assertEquals(val, lpn.toString());
  }
}