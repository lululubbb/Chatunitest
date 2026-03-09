package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class JsonNull_559_1Test {

  @Test
    @Timeout(8000)
  void testHashCode() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    int expectedHashCode = JsonNull.class.hashCode();
    int actualHashCode = jsonNull.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }
}