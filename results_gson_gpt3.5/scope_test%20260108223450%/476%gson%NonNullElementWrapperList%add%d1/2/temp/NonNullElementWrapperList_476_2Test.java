package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_476_2Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void add_insertsElementAtIndex() {
    delegate.add("existing");
    list.add(0, "newElement");
    assertEquals(2, delegate.size());
    assertEquals("newElement", delegate.get(0));
    assertEquals("existing", delegate.get(1));
  }

  @Test
    @Timeout(8000)
  void add_throwsNullPointerException_whenElementIsNull() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      nonNullMethod.invoke(list, (Object) null);
    });
    assertNotNull(thrown);
    assertTrue(thrown.getCause() instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void add_allowsNonNullElement() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String element = "element";
    @SuppressWarnings("unchecked")
    String result = (String) nonNullMethod.invoke(list, element);
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  void add_insertsElementAtEnd() {
    list.add(0, "first");
    list.add(1, "second");
    assertEquals(2, delegate.size());
    assertEquals("first", delegate.get(0));
    assertEquals("second", delegate.get(1));
  }

  @Test
    @Timeout(8000)
  void add_insertsElementInMiddle() {
    delegate.add("zero");
    delegate.add("two");
    list.add(1, "one");
    assertEquals(3, delegate.size());
    assertEquals("zero", delegate.get(0));
    assertEquals("one", delegate.get(1));
    assertEquals("two", delegate.get(2));
  }
}