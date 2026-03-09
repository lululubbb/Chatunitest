package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class JsonNull_557_3Test {

  @Test
    @Timeout(8000)
  public void testConstructorDeprecated() throws Exception {
    // Use reflection to invoke deprecated constructor
    var constructor = JsonNull.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    JsonNull instance = constructor.newInstance();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopyReturnsInstance() {
    JsonNull instance = JsonNull.INSTANCE;
    JsonNull copy = instance.deepCopy();
    assertNotNull(copy);
    assertSame(instance, copy);
  }

  @Test
    @Timeout(8000)
  public void testHashCodeConsistency() {
    JsonNull instance = JsonNull.INSTANCE;
    int hash1 = instance.hashCode();
    int hash2 = instance.hashCode();
    assertEquals(hash1, hash2);
  }

  @Test
    @Timeout(8000)
  public void testEquals() {
    JsonNull instance = JsonNull.INSTANCE;
    assertTrue(instance.equals(instance)); // reflexive
    assertFalse(instance.equals(null));
    assertFalse(instance.equals(new Object()));
    assertTrue(instance.equals(JsonNull.INSTANCE));
    // Using reflection to create another instance for equality test
    try {
      var constructor = JsonNull.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      JsonNull anotherInstance = constructor.newInstance();
      assertTrue(instance.equals(anotherInstance));
      assertTrue(anotherInstance.equals(instance));
    } catch (Exception e) {
      fail("Reflection instantiation failed");
    }
  }

}