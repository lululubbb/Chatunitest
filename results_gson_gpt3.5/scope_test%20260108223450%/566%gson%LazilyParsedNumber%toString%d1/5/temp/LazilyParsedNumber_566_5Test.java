package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LazilyParsedNumber_566_5Test {

  @Test
    @Timeout(8000)
  void toString_shouldReturnValueString() {
    String expected = "123.456";
    LazilyParsedNumber number = new LazilyParsedNumber(expected);

    String actual = number.toString();

    assertEquals(expected, actual);
  }
}