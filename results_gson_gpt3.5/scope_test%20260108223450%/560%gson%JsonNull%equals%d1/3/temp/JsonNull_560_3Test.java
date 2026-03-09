package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonNull_560_3Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.equals(jsonNull));
  }

  @Test
    @Timeout(8000)
  void testEquals_otherJsonNullInstance() {
    JsonNull jsonNull1 = JsonNull.INSTANCE;
    JsonNull jsonNull2 = new JsonNull();
    assertTrue(jsonNull1.equals(jsonNull2));
    assertTrue(jsonNull2.equals(jsonNull1));
  }

  @Test
    @Timeout(8000)
  void testEquals_null() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertFalse(jsonNull.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    Object other = new Object();
    assertFalse(jsonNull.equals(other));
  }
}