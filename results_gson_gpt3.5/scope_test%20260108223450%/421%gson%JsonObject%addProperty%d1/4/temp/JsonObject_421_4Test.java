package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_421_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullCharacter_addsJsonPrimitive() {
    Character value = 'a';
    String property = "key";

    // Call addProperty
    jsonObject.addProperty(property, value);

    // Verify add called with property and JsonPrimitive wrapping 'a'
    verify(jsonObject).add(eq(property), argThat(arg -> arg instanceof JsonPrimitive && ((JsonPrimitive) arg).getAsCharacter() == value));
    // Also verify size increased and has returns true
    assertTrue(jsonObject.has(property));
    assertEquals(1, jsonObject.size());
    assertEquals(value.charValue(), jsonObject.getAsJsonPrimitive(property).getAsCharacter());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullCharacter_addsJsonNull() {
    Character value = null;
    String property = "nullKey";

    jsonObject.addProperty(property, value);

    // Verify add called with property and JsonNull.INSTANCE
    verify(jsonObject).add(eq(property), eq(JsonNull.INSTANCE));
    assertTrue(jsonObject.has(property));
    assertEquals(1, jsonObject.size());
    assertEquals(JsonNull.INSTANCE, jsonObject.get(property));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_reflectionInvoke() throws Exception {
    // Use reflection to invoke private add method to verify coverage indirectly
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);

    String property = "reflected";
    JsonPrimitive primitive = new JsonPrimitive('z');

    addMethod.invoke(jsonObject, property, primitive);

    assertTrue(jsonObject.has(property));
    assertEquals(primitive, jsonObject.get(property));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_multipleAdds() {
    jsonObject.addProperty("char1", 'x');
    jsonObject.addProperty("char2", (Character) null);
    jsonObject.addProperty("char3", 'y');

    assertEquals(3, jsonObject.size());
    assertEquals('x', jsonObject.getAsJsonPrimitive("char1").getAsCharacter());
    assertEquals(JsonNull.INSTANCE, jsonObject.get("char2"));
    assertEquals('y', jsonObject.getAsJsonPrimitive("char3").getAsCharacter());
  }
}