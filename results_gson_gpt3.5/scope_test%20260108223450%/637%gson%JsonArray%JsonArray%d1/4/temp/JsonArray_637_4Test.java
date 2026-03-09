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

public class JsonArray_637_4Test {

    private JsonArray jsonArray;

    @BeforeEach
    void setUp() {
        jsonArray = new JsonArray();
    }

    @Test
    @Timeout(8000)
    void testConstructorWithCapacity() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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
    void testAddAndGetElements() {
        jsonArray.add(Boolean.TRUE);
        jsonArray.add('a');
        jsonArray.add(123);
        jsonArray.add("test");
        JsonPrimitive primitive = new JsonPrimitive("elem");
        jsonArray.add(primitive);

        assertEquals(5, jsonArray.size());
        assertTrue(jsonArray.get(0).getAsBoolean());
        assertEquals('a', jsonArray.get(1).getAsCharacter());
        assertEquals(123, jsonArray.get(2).getAsInt());
        assertEquals("test", jsonArray.get(3).getAsString());
        assertEquals(primitive, jsonArray.get(4));
    }

    @Test
    @Timeout(8000)
    void testAddAllAndContains() {
        JsonArray other = new JsonArray();
        other.add(new JsonPrimitive(1));
        other.add(new JsonPrimitive(2));

        jsonArray.add(new JsonPrimitive(0));
        jsonArray.addAll(other);

        assertEquals(3, jsonArray.size());
        assertTrue(jsonArray.contains(new JsonPrimitive(0)));
        assertTrue(jsonArray.contains(new JsonPrimitive(1)));
        assertTrue(jsonArray.contains(new JsonPrimitive(2)));
    }

    @Test
    @Timeout(8000)
    void testSetAndRemoveByIndexAndElement() {
        jsonArray.add(new JsonPrimitive("one"));
        jsonArray.add(new JsonPrimitive("two"));
        jsonArray.add(new JsonPrimitive("three"));

        JsonPrimitive newElem = new JsonPrimitive("new");
        JsonElement old = jsonArray.set(1, newElem);
        assertEquals("two", old.getAsString());
        assertEquals(newElem, jsonArray.get(1));

        boolean removed = jsonArray.remove(newElem);
        assertTrue(removed);
        assertEquals(2, jsonArray.size());

        JsonElement removedByIndex = jsonArray.remove(0);
        assertEquals("one", removedByIndex.getAsString());
        assertEquals(1, jsonArray.size());
    }

    @Test
    @Timeout(8000)
    void testIterator() {
        jsonArray.add(new JsonPrimitive(1));
        jsonArray.add(new JsonPrimitive(2));
        jsonArray.add(new JsonPrimitive(3));

        Iterator<JsonElement> iterator = jsonArray.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            JsonElement elem = iterator.next();
            assertNotNull(elem);
            count++;
        }
        assertEquals(jsonArray.size(), count);
    }

    @Test
    @Timeout(8000)
    void testDeepCopy() {
        jsonArray.add(new JsonPrimitive("value"));
        JsonArray copy = jsonArray.deepCopy();
        assertNotSame(jsonArray, copy);
        assertEquals(jsonArray.size(), copy.size());
        assertEquals(jsonArray.get(0), copy.get(0));
    }

}