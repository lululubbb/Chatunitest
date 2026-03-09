package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonNull_557_2Test {

  @Test
    @Timeout(8000)
  public void testConstructor_and_INSTANCE() throws Exception {
    // Deprecated constructor
    JsonNull jsonNull = new JsonNull();
    assertNotNull(jsonNull);

    // INSTANCE field
    assertNotNull(JsonNull.INSTANCE);
    assertSame(JsonNull.INSTANCE, JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    JsonNull copy = JsonNull.INSTANCE.deepCopy();
    assertNotNull(copy);
    assertSame(JsonNull.INSTANCE, copy);
  }

  @Test
    @Timeout(8000)
  public void testHashCode() {
    int hash = JsonNull.INSTANCE.hashCode();
    assertEquals(JsonNull.INSTANCE.hashCode(), hash);
  }

  @Test
    @Timeout(8000)
  public void testEquals() {
    assertTrue(JsonNull.INSTANCE.equals(JsonNull.INSTANCE));
    assertFalse(JsonNull.INSTANCE.equals(null));
    assertFalse(JsonNull.INSTANCE.equals(new Object()));
    assertFalse(JsonNull.INSTANCE.equals(new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return null;
      }
    }));
  }
}