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

public class JsonObject_414_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testConstructor_initializesEmptyMembers() throws Exception {
    // Using reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    Object members = membersField.get(jsonObject);
    assertNotNull(members);
    assertTrue(members instanceof LinkedTreeMap);
    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) members;
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testDeepCopyCreatesIndependentCopy() {
    JsonPrimitive prim = new JsonPrimitive("value");
    jsonObject.add("key", prim);

    JsonObject copy = jsonObject.deepCopy();
    assertNotSame(jsonObject, copy);
    assertEquals(jsonObject.size(), copy.size());
    assertTrue(copy.has("key"));
    assertEquals(prim, copy.get("key"));

    // Modify original, copy should not be affected if deep copy is correct
    jsonObject.addProperty("key", "newValue");
    assertNotEquals(jsonObject.get("key"), copy.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testAddAndRemoveProperties() {
    JsonPrimitive prim = new JsonPrimitive("val");
    jsonObject.add("prop", prim);
    assertTrue(jsonObject.has("prop"));
    assertEquals(prim, jsonObject.get("prop"));

    JsonElement removed = jsonObject.remove("prop");
    assertEquals(prim, removed);
    assertFalse(jsonObject.has("prop"));
    assertNull(jsonObject.get("prop"));
  }

  @Test
    @Timeout(8000)
  public void testAddPropertyWithVariousTypes() {
    jsonObject.addProperty("string", "str");
    assertTrue(jsonObject.get("string").isJsonPrimitive());
    assertEquals("str", jsonObject.get("string").getAsString());

    jsonObject.addProperty("number", 123);
    assertEquals(123, jsonObject.get("number").getAsInt());

    jsonObject.addProperty("bool", true);
    assertTrue(jsonObject.get("bool").getAsBoolean());

    jsonObject.addProperty("char", 'c');
    assertEquals("c", jsonObject.get("char").getAsString());
  }

  @Test
    @Timeout(8000)
  public void testEntrySetAndKeySet() {
    jsonObject.addProperty("one", "1");
    jsonObject.addProperty("two", "2");

    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    assertEquals(2, entries.size());
    assertTrue(entries.stream().anyMatch(e -> e.getKey().equals("one")));
    assertTrue(entries.stream().anyMatch(e -> e.getKey().equals("two")));

    Set<String> keys = jsonObject.keySet();
    assertEquals(2, keys.size());
    assertTrue(keys.contains("one"));
    assertTrue(keys.contains("two"));
  }

  @Test
    @Timeout(8000)
  public void testSizeAndIsEmpty() {
    assertTrue(jsonObject.isEmpty());
    assertEquals(0, jsonObject.size());

    jsonObject.addProperty("a", "b");
    assertFalse(jsonObject.isEmpty());
    assertEquals(1, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testHasAndGetMethods() {
    jsonObject.addProperty("exists", "yes");
    assertTrue(jsonObject.has("exists"));
    assertFalse(jsonObject.has("missing"));

    JsonElement element = jsonObject.get("exists");
    assertNotNull(element);
    assertEquals("yes", element.getAsString());

    assertNull(jsonObject.get("missing"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitiveArrayObject() {
    jsonObject.addProperty("prim", "string");

    JsonPrimitive prim = jsonObject.getAsJsonPrimitive("prim");
    assertNotNull(prim);
    assertEquals("string", prim.getAsString());

    JsonArray array = new JsonArray();
    jsonObject.add("array", array);
    assertSame(array, jsonObject.getAsJsonArray("array"));

    JsonObject obj = new JsonObject();
    jsonObject.add("obj", obj);
    assertSame(obj, jsonObject.getAsJsonObject("obj"));

    // For missing keys, these methods return null
    assertNull(jsonObject.getAsJsonPrimitive("missing"));
    assertNull(jsonObject.getAsJsonArray("missing"));
    assertNull(jsonObject.getAsJsonObject("missing"));
  }

  @Test
    @Timeout(8000)
  public void testAsMapReturnsMembersMap() throws Exception {
    jsonObject.addProperty("key", "value");
    Map<String, JsonElement> map = jsonObject.asMap();
    assertNotNull(map);
    assertEquals(1, map.size());
    assertTrue(map.containsKey("key"));
    assertEquals("value", map.get("key").getAsString());

    // Modifying the returned map should affect jsonObject members
    map.remove("key");
    assertFalse(jsonObject.has("key"));
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonObject other = new JsonObject();
    assertEquals(jsonObject, other);
    assertEquals(jsonObject.hashCode(), other.hashCode());

    jsonObject.addProperty("a", "b");
    assertNotEquals(jsonObject, other);
    other.addProperty("a", "b");
    assertEquals(jsonObject, other);
    assertEquals(jsonObject.hashCode(), other.hashCode());

    other.addProperty("c", "d");
    assertNotEquals(jsonObject, other);
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethodsViaReflection() throws Exception {
    // No private methods shown except constructor and members field, but test deepCopy is overridden
    // Just demonstrate invoking deepCopy via reflection as example

    Method deepCopyMethod = JsonObject.class.getDeclaredMethod("deepCopy");
    deepCopyMethod.setAccessible(true);
    Object copy = deepCopyMethod.invoke(jsonObject);
    assertNotNull(copy);
    assertTrue(copy instanceof JsonObject);
    assertNotSame(jsonObject, copy);
  }
}