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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NonNullElementWrapperList_471_5Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  public void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  public void constructor_nullDelegate_throwsNPE() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> {
      new NonNullElementWrapperList<String>(null);
    });
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void get_and_size() {
    delegate.add("a");
    delegate.add("b");
    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
  }

  @Test
    @Timeout(8000)
  public void set_replacesElement() {
    delegate.add("x");
    String old = list.set(0, "y");
    assertEquals("x", old);
    assertEquals("y", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  public void set_nullElement_throwsNPE() {
    delegate.add("x");
    NullPointerException ex = assertThrows(NullPointerException.class, () -> list.set(0, null));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void add_insertsElement() {
    list.add(0, "first");
    assertEquals(1, list.size());
    assertEquals("first", list.get(0));
  }

  @Test
    @Timeout(8000)
  public void add_nullElement_throwsNPE() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> list.add(0, null));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void remove_byIndex() {
    delegate.add("a");
    delegate.add("b");
    String removed = list.remove(0);
    assertEquals("a", removed);
    assertEquals(1, list.size());
    assertEquals("b", list.get(0));
  }

  @Test
    @Timeout(8000)
  public void clear_clearsDelegate() {
    delegate.add("a");
    delegate.add("b");
    list.clear();
    assertTrue(delegate.isEmpty());
    assertEquals(0, list.size());
  }

  @Test
    @Timeout(8000)
  public void removeObject_removesAndReturnsTrue() {
    delegate.add("a");
    delegate.add("b");
    boolean removed = list.remove("a");
    assertTrue(removed);
    assertEquals(1, list.size());
    assertFalse(list.remove("x"));
  }

  @Test
    @Timeout(8000)
  public void removeAll_removesAllMatching() {
    delegate.addAll(Arrays.asList("a", "b", "c", "d"));
    boolean changed = list.removeAll(Arrays.asList("b", "d"));
    assertTrue(changed);
    assertEquals(2, list.size());
    assertFalse(list.contains("b"));
    assertFalse(list.contains("d"));
  }

  @Test
    @Timeout(8000)
  public void retainAll_retainOnlySpecified() {
    delegate.addAll(Arrays.asList("a", "b", "c"));
    boolean changed = list.retainAll(Arrays.asList("a", "c"));
    assertTrue(changed);
    assertEquals(2, list.size());
    assertTrue(list.contains("a"));
    assertTrue(list.contains("c"));
    assertFalse(list.contains("b"));
  }

  @Test
    @Timeout(8000)
  public void contains_checksPresence() {
    delegate.add("a");
    assertTrue(list.contains("a"));
    assertFalse(list.contains("x"));
  }

  @Test
    @Timeout(8000)
  public void indexOf_and_lastIndexOf() {
    delegate.addAll(Arrays.asList("a", "b", "a"));
    assertEquals(0, list.indexOf("a"));
    assertEquals(2, list.lastIndexOf("a"));
    assertEquals(-1, list.indexOf("x"));
  }

  @Test
    @Timeout(8000)
  public void toArray_and_toArrayWithType() {
    delegate.addAll(Arrays.asList("a", "b"));
    Object[] arr = list.toArray();
    assertArrayEquals(new Object[]{"a","b"}, arr);

    String[] strArr = list.toArray(new String[0]);
    assertArrayEquals(new String[]{"a","b"}, strArr);

    String[] biggerArr = new String[5];
    Arrays.fill(biggerArr, "fill");
    String[] resultArr = list.toArray(biggerArr);
    assertSame(biggerArr, resultArr);
    assertEquals("a", resultArr[0]);
    assertEquals("b", resultArr[1]);
    assertNull(resultArr[2]);
  }

  @Test
    @Timeout(8000)
  public void equals_and_hashCode() {
    delegate.addAll(Arrays.asList("a", "b"));
    NonNullElementWrapperList<String> other = new NonNullElementWrapperList<>(new ArrayList<>(delegate));
    assertEquals(list, other);
    assertEquals(list.hashCode(), other.hashCode());

    NonNullElementWrapperList<String> different = new NonNullElementWrapperList<>(new ArrayList<>(Arrays.asList("a")));
    assertNotEquals(list, different);
  }

  @Test
    @Timeout(8000)
  public void nonNull_privateMethod_throwsOnNull() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String element = "test";
    Object result = nonNullMethod.invoke(list, element);
    assertEquals(element, result);

    NullPointerException ex = assertThrows(NullPointerException.class, () -> {
      try {
        nonNullMethod.invoke(list, new Object[] {null});
      } catch (Exception e) {
        // unwrap invocation target exception
        Throwable cause = e.getCause();
        if (cause instanceof NullPointerException) {
          throw (NullPointerException) cause;
        }
        throw e;
      }
    });
    assertNotNull(ex);
  }
}