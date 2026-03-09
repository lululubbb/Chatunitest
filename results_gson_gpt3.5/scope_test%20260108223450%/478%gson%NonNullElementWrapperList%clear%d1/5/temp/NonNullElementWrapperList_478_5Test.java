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
import org.mockito.Mockito;

class NonNullElementWrapperList_478_5Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegateMock = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void clear_shouldCallDelegateClear() {
    list.clear();
    verify(delegateMock).clear();
  }

  @Test
    @Timeout(8000)
  void clear_reflectionInvoke_shouldCallDelegateClear() throws Exception {
    Method clearMethod = NonNullElementWrapperList.class.getDeclaredMethod("clear");
    clearMethod.setAccessible(true);
    clearMethod.invoke(list);
    verify(delegateMock).clear();
  }
}