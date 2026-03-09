package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_478_2Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void clear_CallsDelegateClear() {
    list.clear();
    verify(delegateMock).clear();
  }

  @Test
    @Timeout(8000)
  void clear_DelegateClearCalledOnce() {
    list.clear();
    verify(delegateMock, times(1)).clear();
  }

  @Test
    @Timeout(8000)
  void testClear_ReflectionInvoke() throws Exception {
    Method clearMethod = NonNullElementWrapperList.class.getDeclaredMethod("clear");
    clearMethod.setAccessible(true);
    clearMethod.invoke(list);
    verify(delegateMock).clear();
  }
}