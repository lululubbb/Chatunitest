package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_637_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCapacity() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    JsonArray arrayWithCapacity = constructor.newInstance(5);
    assertNotNull(arrayWithCapacity);
    assertEquals(0, arrayWithCapacity.size());
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructorCreatesEmptyArray() {
    assertNotNull(jsonArray);
    assertEquals(0, jsonArray.size());
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testAddAndContainsAndSize() {
    JsonPrimitive element = new JsonPrimitive("test");
    jsonArray.add(element);
    assertTrue(jsonArray.contains(element));
    assertEquals(1, jsonArray.size());
  }

  @Test
    @Timeout(8000)
  void testAddAllAddsElements() {
    JsonArray other = new JsonArray();
    other.add(new JsonPrimitive("a"));
    other.add(new JsonPrimitive("b"));
    jsonArray.addAll(other);
    assertEquals(2, jsonArray.size());
    assertTrue(jsonArray.contains(new JsonPrimitive("a")));
    assertTrue(jsonArray.contains(new JsonPrimitive("b")));
  }

  @Test
    @Timeout(8000)
  void testSetReplacesElement() {
    jsonArray.add(new JsonPrimitive("old"));
    JsonPrimitive newElement = new JsonPrimitive("new");
    JsonElement replaced = jsonArray.set(0, newElement);
    assertEquals("old", replaced.getAsString());
    assertEquals("new", jsonArray.get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  void testRemoveByElement() {
    JsonPrimitive element = new JsonPrimitive("removeMe");
    jsonArray.add(element);
    assertTrue(jsonArray.remove(element));
    assertFalse(jsonArray.contains(element));
  }

  @Test
    @Timeout(8000)
  void testRemoveByIndex() {
    jsonArray.add(new JsonPrimitive("one"));
    jsonArray.add(new JsonPrimitive("two"));
    JsonElement removed = jsonArray.remove(0);
    assertEquals("one", removed.getAsString());
    assertEquals(1, jsonArray.size());
  }

  @Test
    @Timeout(8000)
  void testIterator() {
    jsonArray.add(new JsonPrimitive("x"));
    jsonArray.add(new JsonPrimitive("y"));
    Iterator<JsonElement> it = jsonArray.iterator();
    assertTrue(it.hasNext());
    assertEquals("x", it.next().getAsString());
    assertEquals("y", it.next().getAsString());
    assertFalse(it.hasNext());
  }

  @Test
    @Timeout(8000)
  void testGet() {
    JsonPrimitive element = new JsonPrimitive("value");
    jsonArray.add(element);
    JsonElement gotten = jsonArray.get(0);
    assertEquals("value", gotten.getAsString());
  }

  @Test
    @Timeout(8000)
  void testDeepCopyCreatesDistinctCopy() {
    jsonArray.add(new JsonPrimitive("copyMe"));
    JsonArray copy = jsonArray.deepCopy();
    assertNotSame(jsonArray, copy);
    assertEquals(jsonArray.size(), copy.size());
    assertEquals(jsonArray.get(0).getAsString(), copy.get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() {
    JsonArray other = new JsonArray();
    assertEquals(jsonArray, other);
    assertEquals(jsonArray.hashCode(), other.hashCode());

    jsonArray.add(new JsonPrimitive("x"));
    other.add(new JsonPrimitive("x"));
    assertEquals(jsonArray, other);
    assertEquals(jsonArray.hashCode(), other.hashCode());

    other.add(new JsonPrimitive("y"));
    assertNotEquals(jsonArray, other);
  }

  @Test
    @Timeout(8000)
  void testAsListReturnsList() {
    jsonArray.add(new JsonPrimitive("listTest"));
    assertEquals(1, jsonArray.asList().size());
    assertEquals("listTest", jsonArray.asList().get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElementPrivateMethod() throws Exception {
    jsonArray.add(new JsonPrimitive("single"));
    var method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    JsonElement result = (JsonElement) method.invoke(jsonArray);
    assertEquals("single", result.getAsString());
  }

}