package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NonNullElementWrapperList_478_3Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegateMock = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void clear_callsDelegateClear() {
    list.clear();
    verify(delegateMock).clear();
  }
}