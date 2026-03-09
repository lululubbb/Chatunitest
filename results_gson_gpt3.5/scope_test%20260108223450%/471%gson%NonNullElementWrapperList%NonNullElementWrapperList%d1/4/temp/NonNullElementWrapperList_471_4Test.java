package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_471_4Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void constructor_nullDelegate_throwsNPE() {
    assertThrows(NullPointerException.class, () -> new NonNullElementWrapperList<>(null));
  }

  @Test
    @Timeout(8000)
  void get_and_size() {
    delegate.add("a");
    delegate.add("b");
    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
  }

  @Test
    @Timeout(8000)
  void set_replacesElement() {
    delegate.add("a");
    String old = list.set(0, "b");
    assertEquals("a", old);
    assertEquals("b", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void set_nullElement_throwsNPE() {
    delegate.add("a");
    assertThrows(NullPointerException.class, () -> list.set(0, null));
  }

  @Test
    @Timeout(8000)
  void add_insertsElement() {
    list.add(0, "x");
    assertEquals(1, list.size());
    assertEquals("x", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void add_nullElement_throwsNPE() {
    assertThrows(NullPointerException.class, () -> list.add(0, null));
  }

  @Test
    @Timeout(8000)
  void remove_byIndex() {
    delegate.add("a");
    delegate.add("b");
    String removed = list.remove(0);
    assertEquals("a", removed);
    assertEquals(1, list.size());
    assertEquals("b", list.get(0));
  }

  @Test
    @Timeout(8000)
  void clear_clearsDelegate() {
    delegate.add("a");
    delegate.add("b");
    list.clear();
    assertTrue(list.isEmpty());
    assertTrue(delegate.isEmpty());
  }

  @Test
    @Timeout(8000)
  void remove_byObject() {
    delegate.add("a");
    delegate.add("b");
    boolean removed = list.remove("a");
    assertTrue(removed);
    assertEquals(1, list.size());
    assertFalse(delegate.contains("a"));
  }

  @Test
    @Timeout(8000)
  void remove_byObject_notPresent() {
    delegate.add("a");
    boolean removed = list.remove("x");
    assertFalse(removed);
    assertEquals(1, list.size());
  }

  @Test
    @Timeout(8000)
  void removeAll_removesAllMatching() {
    delegate.addAll(Arrays.asList("a", "b", "c"));
    boolean changed = list.removeAll(Arrays.asList("a", "c"));
    assertTrue(changed);
    assertEquals(1, list.size());
    assertEquals("b", list.get(0));
  }

  @Test
    @Timeout(8000)
  void removeAll_noMatches_noChange() {
    delegate.addAll(Arrays.asList("a", "b"));
    boolean changed = list.removeAll(Collections.singleton("x"));
    assertFalse(changed);
    assertEquals(2, list.size());
  }

  @Test
    @Timeout(8000)
  void retainAll_retainSubset() {
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
  void retainAll_retainAll_noChange() {
    delegate.addAll(Arrays.asList("a", "b"));
    boolean changed = list.retainAll(Arrays.asList("a", "b"));
    assertFalse(changed);
    assertEquals(2, list.size());
  }

  @Test
    @Timeout(8000)
  void contains_checksPresence() {
    delegate.add("a");
    assertTrue(list.contains("a"));
    assertFalse(list.contains("x"));
  }

  @Test
    @Timeout(8000)
  void indexOf_and_lastIndexOf() {
    delegate.addAll(Arrays.asList("a", "b", "a"));
    assertEquals(0, list.indexOf("a"));
    assertEquals(2, list.lastIndexOf("a"));
    assertEquals(-1, list.indexOf("x"));
  }

  @Test
    @Timeout(8000)
  void toArray_variants() {
    delegate.addAll(Arrays.asList("a", "b"));
    Object[] arr = list.toArray();
    assertArrayEquals(new Object[] {"a", "b"}, arr);

    String[] strArr = list.toArray(new String[0]);
    assertArrayEquals(new String[] {"a", "b"}, strArr);

    String[] biggerArr = new String[5];
    String[] resultArr = list.toArray(biggerArr);
    assertSame(biggerArr, resultArr);
    assertEquals("a", biggerArr[0]);
    assertEquals("b", biggerArr[1]);
    assertNull(biggerArr[2]);
  }

  @Test
    @Timeout(8000)
  void equals_and_hashCode() {
    delegate.addAll(Arrays.asList("a", "b"));
    NonNullElementWrapperList<String> sameList = new NonNullElementWrapperList<>(new ArrayList<>(delegate));
    NonNullElementWrapperList<String> differentList = new NonNullElementWrapperList<>(new ArrayList<>(Arrays.asList("x", "y")));

    assertEquals(list, sameList);
    assertEquals(list.hashCode(), sameList.hashCode());
    assertNotEquals(list, differentList);
    assertNotEquals(list.hashCode(), differentList.hashCode());
    assertNotEquals(list, null);
    assertNotEquals(list, "string");
  }

  @Test
    @Timeout(8000)
  void nonNull_privateMethod_throwsOnNull() throws Exception {
    Constructor<NonNullElementWrapperList> constructor =
        NonNullElementWrapperList.class.getDeclaredConstructor(ArrayList.class);
    constructor.setAccessible(true);
    NonNullElementWrapperList<String> instance = constructor.newInstance(new ArrayList<>());

    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    // Should return the same object if non-null
    String val = "test";
    Object ret = nonNullMethod.invoke(instance, val);
    assertSame(val, ret);

    // Should throw NullPointerException if null passed
    assertThrows(
        NullPointerException.class,
        () -> {
          try {
            nonNullMethod.invoke(instance, new Object[] {null});
          } catch (Exception e) {
            // unwrap invocation target exception
            throw e.getCause();
          }
        });
  }
}