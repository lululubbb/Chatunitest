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

class JsonArray_637_2Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructorCreatesEmptyArray() {
    assertEquals(0, jsonArray.size());
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testCapacityConstructorCreatesEmptyArray() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    JsonArray arrayWithCapacity = constructor.newInstance(10);
    assertNotNull(arrayWithCapacity);
    assertEquals(0, arrayWithCapacity.size());
    assertTrue(arrayWithCapacity.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testAddAndGetElements() {
    jsonArray.add(Boolean.TRUE);
    jsonArray.add('a');
    jsonArray.add(123);
    jsonArray.add("test");
    JsonPrimitive primitive = new JsonPrimitive("jsonElement");
    jsonArray.add(primitive);

    assertEquals(5, jsonArray.size());
    assertEquals(JsonPrimitive.class, jsonArray.get(0).getClass());
    assertEquals(JsonPrimitive.class, jsonArray.get(1).getClass());
    assertEquals(JsonPrimitive.class, jsonArray.get(2).getClass());
    assertEquals(JsonPrimitive.class, jsonArray.get(3).getClass());
    assertSame(primitive, jsonArray.get(4));
  }

  @Test
    @Timeout(8000)
  void testAddAllAndContains() {
    JsonArray other = new JsonArray();
    other.add("one");
    other.add("two");

    jsonArray.add("zero");
    jsonArray.addAll(other);

    assertEquals(3, jsonArray.size());
    assertTrue(jsonArray.contains(new JsonPrimitive("one")));
    assertTrue(jsonArray.contains(new JsonPrimitive("zero")));
    assertFalse(jsonArray.contains(new JsonPrimitive("three")));
  }

  @Test
    @Timeout(8000)
  void testSetAndRemove() {
    jsonArray.add("first");
    jsonArray.add("second");
    jsonArray.add("third");

    JsonPrimitive replacement = new JsonPrimitive("replacement");
    JsonElement old = jsonArray.set(1, replacement);

    assertEquals(new JsonPrimitive("second"), old);
    assertEquals(replacement, jsonArray.get(1));

    boolean removed = jsonArray.remove(replacement);
    assertTrue(removed);
    assertEquals(2, jsonArray.size());

    JsonElement removedByIndex = jsonArray.remove(0);
    assertEquals(new JsonPrimitive("first"), removedByIndex);
    assertEquals(1, jsonArray.size());
  }

  @Test
    @Timeout(8000)
  void testIterator() {
    jsonArray.add("a");
    jsonArray.add("b");
    jsonArray.add("c");

    Iterator<JsonElement> it = jsonArray.iterator();
    assertTrue(it.hasNext());
    assertEquals(new JsonPrimitive("a"), it.next());
    assertEquals(new JsonPrimitive("b"), it.next());
    assertEquals(new JsonPrimitive("c"), it.next());
    assertFalse(it.hasNext());
  }

  @Test
    @Timeout(8000)
  void testDeepCopyCreatesDistinctButEqualArray() {
    jsonArray.add(new JsonPrimitive("copy1"));
    jsonArray.add(new JsonPrimitive("copy2"));

    JsonArray copy = jsonArray.deepCopy();

    assertNotSame(jsonArray, copy);
    assertEquals(jsonArray.size(), copy.size());
    for (int i = 0; i < jsonArray.size(); i++) {
      assertEquals(jsonArray.get(i), copy.get(i));
      // Only assertNotSame if the element is not a JsonPrimitive with immutable value
      JsonElement original = jsonArray.get(i);
      JsonElement copied = copy.get(i);
      if (!(original instanceof JsonPrimitive)) {
        assertNotSame(original, copied);
      }
    }
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElementReflection() throws Exception {
    jsonArray.add("onlyElement");
    var method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    Object result = method.invoke(jsonArray);
    assertEquals(new JsonPrimitive("onlyElement"), result);

    jsonArray = new JsonArray(); // reset to empty array to avoid IllegalStateException
    jsonArray.add("anotherElement");
    // Remove the second element to avoid IllegalStateException
    // Or test with only one element to avoid exception
    // So remove the second element before invoking
    // Actually, test the case with size != 1 returns null
    jsonArray.add("yetAnotherElement");
    // Instead of invoking directly, catch the exception and assert null if size != 1
    try {
      result = method.invoke(jsonArray);
      fail("Expected IllegalStateException for size != 1");
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof IllegalStateException) {
        // Expected exception, test passes here
      } else {
        throw e;
      }
    }
  }

  @Test
    @Timeout(8000)
  void testGetAsNumberDelegates() {
    jsonArray.add(42);
    Number number = jsonArray.getAsNumber();
    assertEquals(42, number.intValue());
  }

  @Test
    @Timeout(8000)
  void testGetAsStringDelegates() {
    jsonArray.add("stringValue");
    String str = jsonArray.getAsString();
    assertEquals("stringValue", str);
  }

  @Test
    @Timeout(8000)
  void testGetAsDoubleDelegates() {
    jsonArray.add(1.23);
    double d = jsonArray.getAsDouble();
    assertEquals(1.23, d);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimalDelegates() {
    jsonArray.add("123.45");
    assertEquals(new java.math.BigDecimal("123.45"), jsonArray.getAsBigDecimal());
  }

  @Test
    @Timeout(8000)
  void testGetAsBigIntegerDelegates() {
    jsonArray.add("12345");
    assertEquals(new java.math.BigInteger("12345"), jsonArray.getAsBigInteger());
  }

  @Test
    @Timeout(8000)
  void testGetAsFloatDelegates() {
    jsonArray.add(1.23f);
    assertEquals(1.23f, jsonArray.getAsFloat());
  }

  @Test
    @Timeout(8000)
  void testGetAsLongDelegates() {
    jsonArray.add(123L);
    assertEquals(123L, jsonArray.getAsLong());
  }

  @Test
    @Timeout(8000)
  void testGetAsIntDelegates() {
    jsonArray.add(123);
    assertEquals(123, jsonArray.getAsInt());
  }

  @Test
    @Timeout(8000)
  void testGetAsByteDelegates() {
    jsonArray.add((byte) 12);
    assertEquals(12, jsonArray.getAsByte());
  }

  @Test
    @Timeout(8000)
  void testGetAsCharacterDeprecated() {
    jsonArray.add('z');
    assertEquals('z', jsonArray.getAsCharacter());
  }

  @Test
    @Timeout(8000)
  void testGetAsShortDelegates() {
    jsonArray.add((short) 123);
    assertEquals(123, jsonArray.getAsShort());
  }

  @Test
    @Timeout(8000)
  void testGetAsBooleanDelegates() {
    jsonArray.add(true);
    assertTrue(jsonArray.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  void testAsListReturnsListOfElements() {
    jsonArray.add("a");
    jsonArray.add("b");
    var list = jsonArray.asList();
    assertEquals(2, list.size());
    assertEquals(new JsonPrimitive("a"), list.get(0));
    assertEquals(new JsonPrimitive("b"), list.get(1));
  }

  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() {
    JsonArray a1 = new JsonArray();
    JsonArray a2 = new JsonArray();

    a1.add("x");
    a2.add("x");
    assertEquals(a1, a2);
    assertEquals(a1.hashCode(), a2.hashCode());

    a2.add("y");
    assertNotEquals(a1, a2);
  }
}