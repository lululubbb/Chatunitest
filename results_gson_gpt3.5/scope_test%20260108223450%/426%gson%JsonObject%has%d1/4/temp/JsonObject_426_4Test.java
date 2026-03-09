package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonObject_426_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHas_withExistingKey_shouldReturnTrue() {
    JsonElement mockElement = mock(JsonElement.class);
    jsonObject.add("key1", mockElement);

    assertTrue(jsonObject.has("key1"));
  }

  @Test
    @Timeout(8000)
  public void testHas_withNonExistingKey_shouldReturnFalse() {
    assertFalse(jsonObject.has("nonExistingKey"));
  }

  @Test
    @Timeout(8000)
  public void testHas_withNullKey_shouldReturnFalse() {
    assertFalse(jsonObject.has(null));
  }

  @Test
    @Timeout(8000)
  public void testHas_usingReflection_withExistingKey_shouldReturnTrue() throws Exception {
    // Use reflection to invoke private field members and test has method indirectly
    Method hasMethod = JsonObject.class.getDeclaredMethod("has", String.class);
    hasMethod.setAccessible(true);

    JsonElement mockElement = mock(JsonElement.class);
    jsonObject.add("keyReflected", mockElement);

    boolean result = (boolean) hasMethod.invoke(jsonObject, "keyReflected");
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testHas_usingReflection_withNonExistingKey_shouldReturnFalse() throws Exception {
    Method hasMethod = JsonObject.class.getDeclaredMethod("has", String.class);
    hasMethod.setAccessible(true);

    boolean result = (boolean) hasMethod.invoke(jsonObject, "absentKey");
    assertFalse(result);
  }
}