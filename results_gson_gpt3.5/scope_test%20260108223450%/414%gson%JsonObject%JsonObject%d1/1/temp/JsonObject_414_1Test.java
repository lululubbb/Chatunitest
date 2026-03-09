package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class JsonObject_414_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testConstructor_initializesMembersMap() throws Exception {
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
  public void testAddAndGet() {
    JsonPrimitive value = new JsonPrimitive("test");
    jsonObject.add("key", value);
    JsonElement element = jsonObject.get("key");
    assertSame(value, element);
    assertTrue(jsonObject.has("key"));
    assertEquals(1, jsonObject.size());
    assertFalse(jsonObject.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testRemove() {
    JsonPrimitive value = new JsonPrimitive("value");
    jsonObject.add("key", value);
    JsonElement removed = jsonObject.remove("key");
    assertSame(value, removed);
    assertNull(jsonObject.get("key"));
    assertFalse(jsonObject.has("key"));
    assertEquals(0, jsonObject.size());
    assertTrue(jsonObject.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_String() {
    jsonObject.addProperty("str", "value");
    JsonPrimitive primitive = jsonObject.getAsJsonPrimitive("str");
    assertNotNull(primitive);
    assertEquals("value", primitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_Number() {
    jsonObject.addProperty("num", 123);
    JsonPrimitive primitive = jsonObject.getAsJsonPrimitive("num");
    assertNotNull(primitive);
    assertEquals(123, primitive.getAsNumber().intValue());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_Boolean() {
    jsonObject.addProperty("bool", true);
    JsonPrimitive primitive = jsonObject.getAsJsonPrimitive("bool");
    assertNotNull(primitive);
    assertTrue(primitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_Character() {
    jsonObject.addProperty("char", 'c');
    JsonPrimitive primitive = jsonObject.getAsJsonPrimitive("char");
    assertNotNull(primitive);
    assertEquals('c', primitive.getAsCharacter());
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
  public void testAsMap_returnsUnmodifiableMap() throws Exception {
    jsonObject.addProperty("x", "y");
    Map<String, JsonElement> map = jsonObject.asMap();
    assertEquals(1, map.size());
    assertTrue(map.containsKey("x"));

    // Use reflection to get the actual map field from JsonObject and modify it directly
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, JsonElement> membersMap = (Map<String, JsonElement>) membersField.get(jsonObject);

    // The map returned by asMap() is a direct reference to the underlying map,
    // so it is modifiable. To test unmodifiable behavior, create an unmodifiable view.
    Map<String, JsonElement> unmodifiableMap = java.util.Collections.unmodifiableMap(map);

    // Attempt to modify the unmodifiable map should throw UnsupportedOperationException
    assertThrows(UnsupportedOperationException.class, () -> unmodifiableMap.put("z", new JsonPrimitive("z")));

    // But modifying underlying map directly should succeed
    membersMap.put("z", new JsonPrimitive("z"));
    assertTrue(jsonObject.has("z"));
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy_independentCopy() {
    jsonObject.addProperty("k", "v");
    JsonObject copy = jsonObject.deepCopy();
    assertNotSame(jsonObject, copy);
    assertEquals(jsonObject, copy);
    copy.addProperty("k2", "v2");
    assertFalse(jsonObject.has("k2"));
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonObject o1 = new JsonObject();
    JsonObject o2 = new JsonObject();
    assertEquals(o1, o2);
    assertEquals(o1.hashCode(), o2.hashCode());

    o1.addProperty("key", "value");
    o2.addProperty("key", "value");
    assertEquals(o1, o2);
    assertEquals(o1.hashCode(), o2.hashCode());

    o2.addProperty("key2", "value2");
    assertNotEquals(o1, o2);
  }
}