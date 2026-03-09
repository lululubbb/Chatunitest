package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class JsonNull_557_4Test {

  @Test
    @Timeout(8000)
  public void testConstructorDeprecated() throws Exception {
    // Use reflection to invoke the deprecated public constructor
    JsonNull instance = JsonNull.class.getDeclaredConstructor().newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof JsonNull);
  }

  @Test
    @Timeout(8000)
  public void testInstanceSingleton() {
    assertNotNull(JsonNull.INSTANCE);
    assertTrue(JsonNull.INSTANCE instanceof JsonNull);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopyReturnsSameInstance() {
    JsonNull copy = JsonNull.INSTANCE.deepCopy();
    assertSame(JsonNull.INSTANCE, copy);
  }

  @Test
    @Timeout(8000)
  public void testHashCodeConsistency() {
    int hash1 = JsonNull.INSTANCE.hashCode();
    int hash2 = JsonNull.INSTANCE.hashCode();
    assertEquals(hash1, hash2);
  }

  @Test
    @Timeout(8000)
  public void testEquals() {
    JsonNull instance = JsonNull.INSTANCE;
    assertTrue(instance.equals(instance)); // Reflexive
    assertFalse(instance.equals(null));
    assertFalse(instance.equals(new Object()));
    // Equals with another JsonNull instance created by deprecated constructor
    JsonNull another = new JsonNull();
    assertTrue(instance.equals(another));
    assertTrue(another.equals(instance));
  }
}