package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonArray_636_6Test {

    private JsonArray jsonArray;

    @BeforeEach
    void setUp() {
        jsonArray = new JsonArray();
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test no-arg constructor initializes empty elements list")
    void testNoArgConstructor() throws Exception {
        // Use reflection to create a new instance with no-arg constructor
        Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        JsonArray instance = constructor.newInstance();

        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        Object elements = elementsField.get(instance);

        assertNotNull(elements);
        assertTrue(elements instanceof ArrayList);
        assertEquals(0, ((ArrayList<?>) elements).size());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test elements list is private final and initialized")
    void testElementsField() throws Exception {
        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);

        Object elements = elementsField.get(jsonArray);
        assertNotNull(elements);
        assertTrue(elements instanceof ArrayList);
        assertEquals(0, ((ArrayList<?>) elements).size());

        int modifiers = elementsField.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPrivate(modifiers));
        assertTrue(java.lang.reflect.Modifier.isFinal(modifiers));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test iterator on empty JsonArray")
    void testIteratorEmpty() {
        Iterator<JsonElement> iterator = jsonArray.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test iterator on non-empty JsonArray")
    void testIteratorNonEmpty() {
        JsonPrimitive element1 = new JsonPrimitive("foo");
        JsonPrimitive element2 = new JsonPrimitive(123);
        jsonArray.add(element1);
        jsonArray.add(element2);

        Iterator<JsonElement> iterator = jsonArray.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(element1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(element2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test add(JsonElement) and size()")
    void testAddAndSize() {
        assertEquals(0, jsonArray.size());

        JsonPrimitive element1 = new JsonPrimitive("test");
        jsonArray.add(element1);

        assertEquals(1, jsonArray.size());
        assertEquals(element1, jsonArray.get(0));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test add(Boolean), add(Character), add(Number), add(String)")
    void testAddOverloads() {
        jsonArray.add(true);
        jsonArray.add('c');
        jsonArray.add(42);
        jsonArray.add("hello");

        assertEquals(4, jsonArray.size());

        assertTrue(jsonArray.get(0).getAsBoolean());
        assertEquals('c', jsonArray.get(1).getAsCharacter());
        assertEquals(42, jsonArray.get(2).getAsInt());
        assertEquals("hello", jsonArray.get(3).getAsString());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test addAll(JsonArray)")
    void testAddAll() {
        JsonArray other = new JsonArray();
        other.add(new JsonPrimitive("a"));
        other.add(new JsonPrimitive("b"));

        jsonArray.add(new JsonPrimitive("start"));
        jsonArray.addAll(other);

        assertEquals(3, jsonArray.size());
        assertEquals("start", jsonArray.get(0).getAsString());
        assertEquals("a", jsonArray.get(1).getAsString());
        assertEquals("b", jsonArray.get(2).getAsString());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test set(int, JsonElement)")
    void testSet() {
        jsonArray.add(new JsonPrimitive("old"));
        JsonPrimitive newElement = new JsonPrimitive("new");

        JsonElement oldElement = jsonArray.set(0, newElement);

        assertEquals("old", oldElement.getAsString());
        assertEquals("new", jsonArray.get(0).getAsString());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test remove(JsonElement) and remove(int)")
    void testRemove() {
        JsonPrimitive element1 = new JsonPrimitive("one");
        JsonPrimitive element2 = new JsonPrimitive("two");
        jsonArray.add(element1);
        jsonArray.add(element2);

        boolean removed = jsonArray.remove(element1);
        assertTrue(removed);
        assertEquals(1, jsonArray.size());
        assertEquals(element2, jsonArray.get(0));

        JsonElement removedElement = jsonArray.remove(0);
        assertEquals(element2, removedElement);
        assertEquals(0, jsonArray.size());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test contains(JsonElement)")
    void testContains() {
        JsonPrimitive element1 = new JsonPrimitive("x");
        jsonArray.add(element1);

        assertTrue(jsonArray.contains(element1));
        assertFalse(jsonArray.contains(new JsonPrimitive("y")));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test isEmpty()")
    void testIsEmpty() {
        assertTrue(jsonArray.isEmpty());
        jsonArray.add(new JsonPrimitive("not empty"));
        assertFalse(jsonArray.isEmpty());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test get(int) with valid and invalid index")
    void testGet() {
        JsonPrimitive element = new JsonPrimitive("val");
        jsonArray.add(element);
        assertEquals(element, jsonArray.get(0));

        assertThrows(IndexOutOfBoundsException.class, () -> jsonArray.get(1));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test asList() returns list with same elements")
    void testAsList() {
        JsonPrimitive element1 = new JsonPrimitive("a");
        JsonPrimitive element2 = new JsonPrimitive("b");
        jsonArray.add(element1);
        jsonArray.add(element2);

        List<JsonElement> list = jsonArray.asList();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(element1, list.get(0));
        assertEquals(element2, list.get(1));
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test equals() and hashCode()")
    void testEqualsAndHashCode() {
        JsonArray other = new JsonArray();
        assertEquals(jsonArray, other);
        assertEquals(jsonArray.hashCode(), other.hashCode());

        jsonArray.add(new JsonPrimitive("val"));
        assertNotEquals(jsonArray, other);
        other.add(new JsonPrimitive("val"));
        assertEquals(jsonArray, other);
        assertEquals(jsonArray.hashCode(), other.hashCode());
    }
}