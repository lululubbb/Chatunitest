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
import org.mockito.Mockito;

class NonNullElementWrapperList_475_4Test {

  private NonNullElementWrapperList<String> wrapperList;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    wrapperList = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testSet_replacesElementAndReturnsOld() {
    delegate.add("oldValue");
    String oldValue = wrapperList.set(0, "newValue");
    assertEquals("oldValue", oldValue);
    verify(delegate).set(0, "newValue");
  }

  @Test
    @Timeout(8000)
  void testSet_elementIsNull_throwsNullPointerException() {
    delegate.add("initial");
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> wrapperList.set(0, null));
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void testNonNull_privateMethod_returnsElementIfNotNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);
    String element = "element";
    Object result = nonNullMethod.invoke(wrapperList, element);
    assertEquals(element, result);
  }

  @Test
    @Timeout(8000)
  void testNonNull_privateMethod_throwsIfNull() throws NoSuchMethodException {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);
    assertThrows(InvocationTargetException.class, () -> {
      try {
        nonNullMethod.invoke(wrapperList, (Object) null);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof NullPointerException) {
          throw e;
        }
        fail("Unexpected exception cause");
      }
    });
  }
}