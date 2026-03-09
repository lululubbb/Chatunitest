package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonNull_559_6Test {

  @Test
    @Timeout(8000)
  void testHashCode() {
    JsonNull jsonNull = new JsonNull();
    int expectedHashCode = JsonNull.class.hashCode();
    assertEquals(expectedHashCode, jsonNull.hashCode());
  }
}