package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.NonNullElementWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

class NonNullElementWrapperList_487_6Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() throws Exception {
    delegateMock = spy(new ArrayList<>());
    // Use reflection to create NonNullElementWrapperList with mocked delegate
    list = new NonNullElementWrapperList<>(new ArrayList<>());
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, delegateMock);
  }

  @Test
    @Timeout(8000)
  void equals_withSameDelegateObject_returnsTrue() {
    // Since equals is final on Object, we cannot stub it.
    // Instead, test the actual behavior by calling equals on the spy.
    // So we do not stub delegateMock.equals(), just call and verify.
    assertTrue(list.equals(delegateMock));
  }

  @Test
    @Timeout(8000)
  void equals_withDifferentObject_returnsDelegateEqualsResult() {
    Object other = new Object();
    // We cannot stub equals, so just call and check result.
    boolean result = list.equals(other);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void equals_withNullObject_returnsDelegateEqualsResult() {
    boolean result = list.equals(null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void equals_withSameNonNullElementWrapperList_returnsDelegateEqualsResult() throws Exception {
    NonNullElementWrapperList<String> otherList = new NonNullElementWrapperList<>(new ArrayList<>());
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);

    // Set the otherList's delegate to the same list instance as list's delegate
    // but not the spy, to ensure symmetric equals behavior.
    ArrayList<String> sameListInstance = new ArrayList<>();
    delegateField.set(list, sameListInstance);
    delegateField.set(otherList, sameListInstance);

    assertTrue(list.equals(otherList));
    assertTrue(otherList.equals(list));
  }
}