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

public class JsonObject_414_3Test {

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
    // The LinkedTreeMap should be empty initially
    LinkedTreeMap<?, ?> map = (LinkedTreeMap<?, ?>) members;
    assertTrue(map.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testAddAndGet() {
    JsonPrimitive prim = new JsonPrimitive("value");
    jsonObject.add("key", prim);
    assertEquals(1, jsonObject.size());
    assertTrue(jsonObject.has("key"));
    assertSame(prim, jsonObject.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testRemove() {
    JsonPrimitive prim = new JsonPrimitive("value");
    jsonObject.add("key", prim);
    JsonElement removed = jsonObject.remove("key");
    assertSame(prim, removed);
    assertFalse(jsonObject.has("key"));
    assertEquals(0, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_String() {
    jsonObject.addProperty("str", "value");
    JsonElement element = jsonObject.get("str");
    assertTrue(element.isJsonPrimitive());
    assertEquals("value", element.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_Number() {
    jsonObject.addProperty("num", 123);
    JsonElement element = jsonObject.get("num");
    assertTrue(element.isJsonPrimitive());
    assertEquals(123, element.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_Boolean() {
    jsonObject.addProperty("bool", true);
    JsonElement element = jsonObject.get("bool");
    assertTrue(element.isJsonPrimitive());
    assertTrue(element.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_Character() {
    jsonObject.addProperty("char", 'c');
    JsonElement element = jsonObject.get("char");
    assertTrue(element.isJsonPrimitive());
    assertEquals('c', element.getAsCharacter());
  }

  @Test
    @Timeout(8000)
  public void testEntrySetAndKeySet() {
    jsonObject.addProperty("a", "1");
    jsonObject.addProperty("b", "2");
    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    Set<String> keys = jsonObject.keySet();

    assertEquals(2, entries.size());
    assertEquals(2, keys.size());
    assertTrue(keys.contains("a"));
    assertTrue(keys.contains("b"));
  }

  @Test
    @Timeout(8000)
  public void testSizeAndIsEmpty() {
    assertTrue(jsonObject.isEmpty());
    assertEquals(0, jsonObject.size());

    jsonObject.addProperty("a", "value");
    assertFalse(jsonObject.isEmpty());
    assertEquals(1, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testHas() {
    assertFalse(jsonObject.has("missing"));
    jsonObject.addProperty("present", "yes");
    assertTrue(jsonObject.has("present"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive() {
    jsonObject.addProperty("prim", "value");
    JsonPrimitive prim = jsonObject.getAsJsonPrimitive("prim");
    assertNotNull(prim);
    assertEquals("value", prim.getAsString());

    assertNull(jsonObject.getAsJsonPrimitive("missing"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray() {
    JsonArray array = new JsonArray();
    jsonObject.add("arr", array);
    assertSame(array, jsonObject.getAsJsonArray("arr"));
    assertNull(jsonObject.getAsJsonArray("missing"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject() {
    JsonObject obj = new JsonObject();
    jsonObject.add("obj", obj);
    assertSame(obj, jsonObject.getAsJsonObject("obj"));
    assertNull(jsonObject.getAsJsonObject("missing"));
  }

  @Test
    @Timeout(8000)
  public void testAsMap() {
    jsonObject.addProperty("a", "1");
    jsonObject.addProperty("b", "2");
    Map<String, JsonElement> map = jsonObject.asMap();
    assertEquals(2, map.size());
    assertTrue(map.containsKey("a"));
    assertTrue(map.containsKey("b"));
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    jsonObject.addProperty("a", "1");
    JsonObject copy = jsonObject.deepCopy();
    assertNotSame(jsonObject, copy);
    assertEquals(jsonObject.size(), copy.size());
    assertEquals(jsonObject.get("a"), copy.get("a"));

    // Modify copy should not affect original
    copy.addProperty("b", "2");
    assertFalse(jsonObject.has("b"));
    assertTrue(copy.has("b"));
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonObject o1 = new JsonObject();
    JsonObject o2 = new JsonObject();
    assertEquals(o1, o2);
    assertEquals(o1.hashCode(), o2.hashCode());

    o1.addProperty("key", "value");
    assertNotEquals(o1, o2);
    assertNotEquals(o1.hashCode(), o2.hashCode());

    o2.addProperty("key", "value");
    assertEquals(o1, o2);
    assertEquals(o1.hashCode(), o2.hashCode());

    assertNotEquals(o1, null);
    assertNotEquals(o1, "string");
  }
}