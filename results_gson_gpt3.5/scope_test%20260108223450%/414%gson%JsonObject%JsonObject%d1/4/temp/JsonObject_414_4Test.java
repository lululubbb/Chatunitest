package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_414_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testConstructor_initialState() throws Exception {
    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    Object members = membersField.get(jsonObject);
    assertNotNull(members);
    assertTrue(members instanceof LinkedTreeMap);
    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) members;
    assertTrue(map.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testAddAndGetAndHasAndRemove() {
    JsonPrimitive value = new JsonPrimitive("value");
    jsonObject.add("key", value);
    assertTrue(jsonObject.has("key"));
    assertEquals(value, jsonObject.get("key"));

    JsonElement removed = jsonObject.remove("key");
    assertEquals(value, removed);
    assertFalse(jsonObject.has("key"));
    assertNull(jsonObject.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testAddPropertyWithVariousTypes() {
    jsonObject.addProperty("string", "str");
    jsonObject.addProperty("number", 123);
    jsonObject.addProperty("bool", true);
    jsonObject.addProperty("char", 'c');

    assertEquals(new JsonPrimitive("str"), jsonObject.get("string"));
    assertEquals(new JsonPrimitive(123), jsonObject.get("number"));
    assertEquals(new JsonPrimitive(true), jsonObject.get("bool"));
    assertEquals(new JsonPrimitive('c'), jsonObject.get("char"));
  }

  @Test
    @Timeout(8000)
  public void testEntrySetKeySetSizeIsEmpty() {
    assertTrue(jsonObject.isEmpty());
    assertEquals(0, jsonObject.size());
    assertTrue(jsonObject.entrySet().isEmpty());
    assertTrue(jsonObject.keySet().isEmpty());

    jsonObject.addProperty("key", "value");

    assertFalse(jsonObject.isEmpty());
    assertEquals(1, jsonObject.size());
    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    assertEquals(1, entries.size());
    Set<String> keys = jsonObject.keySet();
    assertEquals(1, keys.size());
    assertTrue(keys.contains("key"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitiveJsonArrayJsonObject() {
    JsonPrimitive primitive = new JsonPrimitive("prim");
    JsonArray array = new JsonArray();
    JsonObject innerObject = new JsonObject();

    jsonObject.add("primitive", primitive);
    jsonObject.add("array", array);
    jsonObject.add("object", innerObject);

    assertEquals(primitive, jsonObject.getAsJsonPrimitive("primitive"));
    assertEquals(array, jsonObject.getAsJsonArray("array"));
    assertEquals(innerObject, jsonObject.getAsJsonObject("object"));

    // Test null returns when not present or wrong type
    assertNull(jsonObject.getAsJsonPrimitive("nonexistent"));

    // Safely test getAsJsonArray and getAsJsonObject by checking type before casting
    JsonElement elemPrimitive = jsonObject.get("primitive");
    if (elemPrimitive instanceof JsonArray) {
      assertEquals(elemPrimitive, jsonObject.getAsJsonArray("primitive"));
    } else {
      // Avoid ClassCastException by not calling getAsJsonArray if not a JsonArray
      assertNull(jsonObject.getAsJsonArray("primitive"));
    }

    if (elemPrimitive instanceof JsonObject) {
      assertEquals(elemPrimitive, jsonObject.getAsJsonObject("primitive"));
    } else {
      // Avoid ClassCastException by not calling getAsJsonObject if not a JsonObject
      assertNull(jsonObject.getAsJsonObject("primitive"));
    }
  }

  @Test
    @Timeout(8000)
  public void testAsMapReflectsMembers() {
    jsonObject.addProperty("key", "value");
    Map<String, JsonElement> map = jsonObject.asMap();
    assertNotNull(map);
    assertEquals(1, map.size());
    assertTrue(map.containsKey("key"));
    assertEquals(new JsonPrimitive("value"), map.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonObject other = new JsonObject();
    assertEquals(jsonObject, other);
    assertEquals(jsonObject.hashCode(), other.hashCode());

    jsonObject.addProperty("key", "value");
    assertNotEquals(jsonObject, other);
    other.addProperty("key", "value");
    assertEquals(jsonObject, other);
    assertEquals(jsonObject.hashCode(), other.hashCode());

    other.addProperty("key2", "value2");
    assertNotEquals(jsonObject, other);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopyCreatesIndependentCopy() {
    jsonObject.addProperty("key", "value");
    JsonObject copy = jsonObject.deepCopy();
    assertNotSame(jsonObject, copy);
    assertEquals(jsonObject, copy);

    copy.addProperty("key2", "value2");
    assertNotEquals(jsonObject, copy);
    assertFalse(jsonObject.has("key2"));
  }

  @Test
    @Timeout(8000)
  public void testInvokeDeepCopyByReflection() throws Exception {
    Method deepCopyMethod = JsonObject.class.getDeclaredMethod("deepCopy");
    deepCopyMethod.setAccessible(true);
    Object copy = deepCopyMethod.invoke(jsonObject);
    assertNotNull(copy);
    assertTrue(copy instanceof JsonObject);
  }
}