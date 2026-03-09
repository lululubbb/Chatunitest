package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_487_3Test {

  NonNullElementWrapperList<String> wrapperList;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() throws Exception {
    delegateMock = spy(new ArrayList<>());
    wrapperList = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void equals_whenDelegateEqualsReturnsTrue_shouldReturnTrue() {
    Object other = new Object();
    ArrayList<String> spyDelegate = spy(delegateMock);
    wrapperList = new NonNullElementWrapperList<>(spyDelegate);

    doReturn(true).when(spyDelegate).equals(other);

    boolean result = wrapperList.equals(other);

    assertTrue(result);
    verify(spyDelegate).equals(other);
  }

  @Test
    @Timeout(8000)
  void equals_whenDelegateEqualsReturnsFalse_shouldReturnFalse() {
    Object other = new Object();
    ArrayList<String> spyDelegate = spy(delegateMock);
    wrapperList = new NonNullElementWrapperList<>(spyDelegate);

    doReturn(false).when(spyDelegate).equals(other);

    boolean result = wrapperList.equals(other);

    assertFalse(result);
    verify(spyDelegate).equals(other);
  }

  @Test
    @Timeout(8000)
  void equals_whenComparedWithItself_shouldReturnTrue() {
    ArrayList<String> spyDelegate = spy(delegateMock);
    wrapperList = new NonNullElementWrapperList<>(spyDelegate);

    doReturn(true).when(spyDelegate).equals(wrapperList);

    boolean result = wrapperList.equals(wrapperList);

    assertTrue(result);
    verify(spyDelegate).equals(wrapperList);
  }

  @Test
    @Timeout(8000)
  void equals_whenComparedWithNull_shouldReturnFalse() {
    ArrayList<String> spyDelegate = spy(delegateMock);
    wrapperList = new NonNullElementWrapperList<>(spyDelegate);

    doReturn(false).when(spyDelegate).equals(null);

    boolean result = wrapperList.equals(null);

    assertFalse(result);
    verify(spyDelegate).equals(null);
  }

  @Test
    @Timeout(8000)
  void equals_whenDelegateFieldIsAccessedViaReflection_shouldInvokeEqualsOnDelegate() throws Exception {
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    Object delegateValue = delegateField.get(wrapperList);

    assertSame(delegateMock, delegateValue);

    ArrayList<String> spyDelegate = spy(delegateMock);
    wrapperList = new NonNullElementWrapperList<>(spyDelegate);

    doReturn(true).when(spyDelegate).equals("test");
    boolean result = wrapperList.equals("test");

    assertTrue(result);
    verify(spyDelegate).equals("test");
  }
}