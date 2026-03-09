package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NonNullElementWrapperList_471_1Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  public void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_nullDelegate_throwsNPE() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      new NonNullElementWrapperList<String>(null);
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void testAddAndGetSizeAndGet() {
    list.add(0, "a");
    assertEquals(1, list.size());
    assertEquals("a", list.get(0));
  }

  @Test
    @Timeout(8000)
  public void testSet() {
    list.add(0, "a");
    String old = list.set(0, "b");
    assertEquals("a", old);
    assertEquals("b", list.get(0));
  }

  @Test
    @Timeout(8000)
  public void testRemoveByIndex() {
    list.add(0, "a");
    String removed = list.remove(0);
    assertEquals("a", removed);
    assertEquals(0, list.size());
  }

  @Test
    @Timeout(8000)
  public void testClear() {
    list.add(0, "a");
    list.clear();
    assertEquals(0, list.size());
  }

  @Test
    @Timeout(8000)
  public void testRemoveObject() {
    list.add(0, "a");
    boolean removed = list.remove("a");
    assertTrue(removed);
    assertEquals(0, list.size());
    boolean notRemoved = list.remove("b");
    assertFalse(notRemoved);
  }

  @Test
    @Timeout(8000)
  public void testRemoveAllAndRetainAll() {
    list.add(0, "a");
    list.add(1, "b");
    ArrayList<String> toRemove = new ArrayList<>();
    toRemove.add("a");
    boolean changed = list.removeAll(toRemove);
    assertTrue(changed);
    assertEquals(1, list.size());
    ArrayList<String> toRetain = new ArrayList<>();
    toRetain.add("b");
    boolean changed2 = list.retainAll(toRetain);
    assertFalse(changed2);
  }

  @Test
    @Timeout(8000)
  public void testContainsIndexOfLastIndexOf() {
    list.add(0, "a");
    list.add(1, "b");
    list.add(2, "a");
    assertTrue(list.contains("a"));
    assertEquals(0, list.indexOf("a"));
    assertEquals(2, list.lastIndexOf("a"));
    assertFalse(list.contains("c"));
    assertEquals(-1, list.indexOf("c"));
    assertEquals(-1, list.lastIndexOf("c"));
  }

  @Test
    @Timeout(8000)
  public void testToArray() {
    list.add(0, "a");
    Object[] arr = list.toArray();
    assertArrayEquals(new Object[] {"a"}, arr);

    String[] arr2 = list.toArray(new String[0]);
    assertArrayEquals(new String[] {"a"}, arr2);
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    list.add(0, "a");
    NonNullElementWrapperList<String> other = new NonNullElementWrapperList<>(new ArrayList<>());
    other.add(0, "a");
    assertEquals(list, other);
    assertEquals(list.hashCode(), other.hashCode());

    other.add(1, "b");
    assertNotEquals(list, other);
  }

  @Test
    @Timeout(8000)
  public void testNonNullMethodViaReflection() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    // Should return the element when non-null
    String result = (String) nonNullMethod.invoke(list, "test");
    assertEquals("test", result);

    // Should throw NullPointerException when null
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      nonNullMethod.invoke(list, new Object[] {null});
    });
    assertTrue(thrown.getCause() instanceof NullPointerException);
  }
}