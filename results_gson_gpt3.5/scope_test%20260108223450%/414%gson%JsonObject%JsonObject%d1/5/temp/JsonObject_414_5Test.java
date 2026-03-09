package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class JsonObject_414_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testConstructor_initialState() throws Exception {
    // Use reflection to access private field members
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertNotNull(members);
    assertTrue(members.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testAddAndHasAndGet() {
    JsonPrimitive primitive = new JsonPrimitive("value");
    jsonObject.add("key", primitive);

    assertTrue(jsonObject.has("key"));
    assertEquals(primitive, jsonObject.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testRemove() {
    JsonPrimitive primitive = new JsonPrimitive("value");
    jsonObject.add("key", primitive);

    JsonElement removed = jsonObject.remove("key");
    assertEquals(primitive, removed);
    assertFalse(jsonObject.has("key"));

    // Removing non-existing key returns null
    assertNull(jsonObject.remove("nonexistent"));
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
    jsonObject.addProperty("char", 'a');
    JsonElement element = jsonObject.get("char");
    assertTrue(element.isJsonPrimitive());
    assertEquals('a', element.getAsCharacter());
  }

  @Test
    @Timeout(8000)
  public void testEntrySetAndKeySetAndSizeAndIsEmpty() {
    assertTrue(jsonObject.isEmpty());
    assertEquals(0, jsonObject.size());

    jsonObject.addProperty("a", "1");
    jsonObject.addProperty("b", 2);

    Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
    Set<String> keys = jsonObject.keySet();

    assertEquals(2, entries.size());
    assertEquals(2, keys.size());
    assertTrue(keys.contains("a"));
    assertTrue(keys.contains("b"));
    assertFalse(jsonObject.isEmpty());
    assertEquals(2, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive() {
    jsonObject.addProperty("p", "val");
    JsonElement element = jsonObject.get("p");
    assertTrue(element.isJsonPrimitive());
    JsonElement primitive = jsonObject.get("p");
    assertTrue(primitive.isJsonPrimitive());
    assertEquals("val", primitive.getAsString());

    // Non-existing key returns null
    assertNull(jsonObject.getAsJsonPrimitive("nonexistent"));

    // Add non-primitive element and test returns null
    JsonObject obj = new JsonObject();
    jsonObject.add("obj", obj);
    assertNull(jsonObject.getAsJsonPrimitive("obj"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray() {
    JsonArray array = new JsonArray();
    jsonObject.add("arr", array);
    JsonElement arrElement = jsonObject.get("arr");
    assertTrue(arrElement.isJsonArray());
    assertEquals(array, jsonObject.getAsJsonArray("arr"));

    assertNull(jsonObject.getAsJsonArray("nonexistent"));

    jsonObject.addProperty("p", "val");
    assertNull(jsonObject.getAsJsonArray("p"));
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject() {
    JsonObject obj = new JsonObject();
    jsonObject.add("obj", obj);
    JsonElement objElement = jsonObject.get("obj");
    assertTrue(objElement.isJsonObject());
    assertEquals(obj, jsonObject.getAsJsonObject("obj"));

    assertNull(jsonObject.getAsJsonObject("nonexistent"));

    jsonObject.addProperty("p", "val");
    assertNull(jsonObject.getAsJsonObject("p"));
  }

  @Test
    @Timeout(8000)
  public void testAsMap() {
    jsonObject.addProperty("k1", "v1");
    jsonObject.addProperty("k2", 2);

    Map<String, JsonElement> map = jsonObject.asMap();
    assertEquals(2, map.size());
    assertTrue(map.containsKey("k1"));
    assertTrue(map.containsKey("k2"));
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonObject obj1 = new JsonObject();
    JsonObject obj2 = new JsonObject();

    assertEquals(obj1, obj2);
    assertEquals(obj1.hashCode(), obj2.hashCode());

    obj1.addProperty("a", "1");
    assertNotEquals(obj1, obj2);

    obj2.addProperty("a", "1");
    assertEquals(obj1, obj2);
    assertEquals(obj1.hashCode(), obj2.hashCode());

    obj2.addProperty("b", "2");
    assertNotEquals(obj1, obj2);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    jsonObject.addProperty("a", "1");
    JsonObject copy = jsonObject.deepCopy();

    assertNotSame(jsonObject, copy);
    assertEquals(jsonObject, copy);

    // Modify original and ensure copy does not change
    jsonObject.addProperty("b", "2");
    assertNotEquals(jsonObject, copy);
  }
}