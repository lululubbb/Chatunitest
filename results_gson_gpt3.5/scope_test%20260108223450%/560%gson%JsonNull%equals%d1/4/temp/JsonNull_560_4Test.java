package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonNull_560_4Test {

  @Test
    @Timeout(8000)
  void testEquals_withSameInstance() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.equals(jsonNull));
  }

  @Test
    @Timeout(8000)
  void testEquals_withAnotherJsonNullInstance() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    JsonNull another = new JsonNull();
    assertTrue(jsonNull.equals(another));
  }

  @Test
    @Timeout(8000)
  void testEquals_withNull() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertFalse(jsonNull.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_withDifferentObject() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    Object different = new Object();
    assertFalse(jsonNull.equals(different));
  }

  @Test
    @Timeout(8000)
  void testEquals_withSubclassInstance() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    Object subclassInstance = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    assertFalse(jsonNull.equals(subclassInstance));
  }
}