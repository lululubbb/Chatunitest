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

public class JsonArray_636_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  public void testNoArgConstructor_createsEmptyArray() {
    assertNotNull(jsonArray);
    assertEquals(0, jsonArray.size());
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testIntCapacityConstructor_createsArrayWithCapacity() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    JsonArray arrayWithCapacity = constructor.newInstance(10);
    assertNotNull(arrayWithCapacity);
    assertEquals(0, arrayWithCapacity.size());
    assertTrue(arrayWithCapacity.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testAddAndGetElements() {
    jsonArray.add(new JsonPrimitive("test"));
    jsonArray.add(new JsonPrimitive(123));
    jsonArray.add(new JsonPrimitive(true));

    assertEquals(3, jsonArray.size());
    assertEquals("test", jsonArray.get(0).getAsString());
    assertEquals(123, jsonArray.get(1).getAsInt());
    assertEquals(true, jsonArray.get(2).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddBooleanCharacterNumberStringAndJsonElement() {
    jsonArray.add(Boolean.TRUE);
    jsonArray.add(Character.valueOf('a'));
    jsonArray.add(123);
    jsonArray.add("string");
    JsonPrimitive primitive = new JsonPrimitive("element");
    jsonArray.add(primitive);

    assertEquals(5, jsonArray.size());
    assertEquals(true, jsonArray.get(0).getAsBoolean());
    assertEquals('a', jsonArray.get(1).getAsCharacter());
    assertEquals(123, jsonArray.get(2).getAsInt());
    assertEquals("string", jsonArray.get(3).getAsString());
    assertEquals(primitive, jsonArray.get(4));
  }

  @Test
    @Timeout(8000)
  public void testAddAllAddsElementsFromAnotherJsonArray() {
    JsonArray other = new JsonArray();
    other.add(new JsonPrimitive("one"));
    other.add(new JsonPrimitive("two"));

    jsonArray.add(new JsonPrimitive("zero"));
    jsonArray.addAll(other);

    assertEquals(3, jsonArray.size());
    assertEquals("zero", jsonArray.get(0).getAsString());
    assertEquals("one", jsonArray.get(1).getAsString());
    assertEquals("two", jsonArray.get(2).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testSetReplacesElement() {
    jsonArray.add(new JsonPrimitive("old"));
    JsonElement replaced = jsonArray.set(0, new JsonPrimitive("new"));
    assertEquals("old", replaced.getAsString());
    assertEquals("new", jsonArray.get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testRemoveByElementAndIndex() {
    JsonPrimitive elem1 = new JsonPrimitive("one");
    JsonPrimitive elem2 = new JsonPrimitive("two");
    jsonArray.add(elem1);
    jsonArray.add(elem2);

    assertTrue(jsonArray.remove(elem1));
    assertEquals(1, jsonArray.size());
    assertEquals(elem2, jsonArray.get(0));

    JsonElement removed = jsonArray.remove(0);
    assertEquals(elem2, removed);
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testContains() {
    JsonPrimitive elem = new JsonPrimitive("test");
    jsonArray.add(elem);
    assertTrue(jsonArray.contains(elem));
    assertFalse(jsonArray.contains(new JsonPrimitive("other")));
  }

  @Test
    @Timeout(8000)
  public void testIsEmptyAndSize() {
    assertTrue(jsonArray.isEmpty());
    assertEquals(0, jsonArray.size());
    jsonArray.add(new JsonPrimitive("not empty"));
    assertFalse(jsonArray.isEmpty());
    assertEquals(1, jsonArray.size());
  }

  @Test
    @Timeout(8000)
  public void testIterator() {
    jsonArray.add(new JsonPrimitive("a"));
    jsonArray.add(new JsonPrimitive("b"));

    Iterator<JsonElement> it = jsonArray.iterator();
    assertTrue(it.hasNext());
    assertEquals("a", it.next().getAsString());
    assertTrue(it.hasNext());
    assertEquals("b", it.next().getAsString());
    assertFalse(it.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElementPrivateMethod() throws Exception {
    jsonArray.add(new JsonPrimitive("single"));

    var method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    JsonElement singleElement = (JsonElement) method.invoke(jsonArray);
    assertEquals("single", singleElement.getAsString());

    jsonArray.add(new JsonPrimitive("two"));
    // Should throw IllegalStateException if more than one element
    assertThrows(InvocationTargetException.class, () -> method.invoke(jsonArray));
  }

  @Test
    @Timeout(8000)
  public void testDeepCopyCreatesDistinctCopy() {
    jsonArray.add(new JsonPrimitive("copy"));
    JsonArray copy = jsonArray.deepCopy();
    assertNotSame(jsonArray, copy);
    assertEquals(jsonArray.size(), copy.size());
    assertEquals(jsonArray.get(0), copy.get(0));
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();
    assertEquals(array1, array2);
    assertEquals(array1.hashCode(), array2.hashCode());

    array1.add(new JsonPrimitive("a"));
    assertNotEquals(array1, array2);
    array2.add(new JsonPrimitive("a"));
    assertEquals(array1, array2);
    assertEquals(array1.hashCode(), array2.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testAsListReturnsList() {
    jsonArray.add(new JsonPrimitive("list"));
    var list = jsonArray.asList();
    assertEquals(1, list.size());
    assertEquals("list", list.get(0).getAsString());
  }
}