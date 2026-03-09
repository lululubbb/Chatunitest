package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonNull_557_6Test {

  @Test
    @Timeout(8000)
  void testDeprecatedConstructor() throws Exception {
    // Use reflection to invoke the deprecated constructor
    var constructor = JsonNull.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    JsonNull instance = constructor.newInstance();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  void testINSTANCEIsSingleton() {
    JsonNull instance1 = JsonNull.INSTANCE;
    JsonNull instance2 = JsonNull.INSTANCE;
    assertSame(instance1, instance2);
  }

  @Test
    @Timeout(8000)
  void testDeepCopyReturnsSameInstance() {
    JsonNull instance = JsonNull.INSTANCE;
    JsonNull copy = instance.deepCopy();
    assertSame(instance, copy);
  }

  @Test
    @Timeout(8000)
  void testHashCodeConsistency() {
    JsonNull instance = JsonNull.INSTANCE;
    int hash1 = instance.hashCode();
    int hash2 = instance.hashCode();
    assertEquals(hash1, hash2);
  }

  @Test
    @Timeout(8000)
  void testEquals() {
    JsonNull instance = JsonNull.INSTANCE;
    assertTrue(instance.equals(instance));
    assertFalse(instance.equals(null));
    assertFalse(instance.equals(new Object()));

    // Equals with another JsonNull instance (should be true)
    JsonNull otherInstance = JsonNull.INSTANCE;
    assertTrue(instance.equals(otherInstance));
  }
}