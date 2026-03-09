package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ReflectionAccessFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ReflectionAccessFilterHelper_102_3Test {

  ReflectionAccessFilter filterAllow;
  ReflectionAccessFilter filterDeny;
  ReflectionAccessFilter filterIndecisive;

  @BeforeEach
  void setUp() {
    filterAllow = mock(ReflectionAccessFilter.class);
    filterDeny = mock(ReflectionAccessFilter.class);
    filterIndecisive = mock(ReflectionAccessFilter.class);
  }

  private ReflectionAccessFilter.FilterResult getFilterResultEnum(String name) throws Exception {
    // Use reflection to get enum constants to avoid direct reference issues
    Class<?> enumClass = Class.forName("com.google.gson.ReflectionAccessFilter$FilterResult");
    Object[] constants = enumClass.getEnumConstants();
    for (Object constant : constants) {
      if (((Enum<?>) constant).name().equals(name)) {
        return (ReflectionAccessFilter.FilterResult) constant;
      }
    }
    throw new IllegalArgumentException("No enum constant " + enumClass.getName() + "." + name);
  }

  @Test
    @Timeout(8000)
  @DisplayName("getFilterResult returns first decisive result ALLOW")
  void testGetFilterResultFirstAllow() throws Exception {
    Class<?> clazz = String.class;
    when(filterAllow.check(clazz)).thenReturn(getFilterResultEnum("ALLOW"));

    List<ReflectionAccessFilter> filters = Arrays.asList(filterAllow, filterDeny, filterIndecisive);

    ReflectionAccessFilter.FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, clazz);

    assertEquals(getFilterResultEnum("ALLOW"), result);
    verify(filterAllow).check(clazz);
    verifyNoMoreInteractions(filterDeny, filterIndecisive);
  }

  @Test
    @Timeout(8000)
  @DisplayName("getFilterResult returns first decisive result DENIED")
  void testGetFilterResultFirstDeny() throws Exception {
    Class<?> clazz = Integer.class;
    when(filterIndecisive.check(clazz)).thenReturn(getFilterResultEnum("INDECISIVE"));
    when(filterDeny.check(clazz)).thenReturn(getFilterResultEnum("DENIED"));

    List<ReflectionAccessFilter> filters = Arrays.asList(filterIndecisive, filterDeny, filterAllow);

    ReflectionAccessFilter.FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, clazz);

    assertEquals(getFilterResultEnum("DENIED"), result);
    verify(filterIndecisive).check(clazz);
    verify(filterDeny).check(clazz);
    verifyNoMoreInteractions(filterAllow);
  }

  @Test
    @Timeout(8000)
  @DisplayName("getFilterResult returns ALLOW when all filters are INDECISIVE")
  void testGetFilterResultAllIndecisive() throws Exception {
    Class<?> clazz = Double.class;
    when(filterIndecisive.check(clazz)).thenReturn(getFilterResultEnum("INDECISIVE"));

    List<ReflectionAccessFilter> filters = Arrays.asList(filterIndecisive, filterIndecisive);

    ReflectionAccessFilter.FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, clazz);

    assertEquals(getFilterResultEnum("ALLOW"), result);
    verify(filterIndecisive, times(2)).check(clazz);
  }

  @Test
    @Timeout(8000)
  @DisplayName("getFilterResult returns ALLOW when filter list is empty")
  void testGetFilterResultEmptyList() throws Exception {
    Class<?> clazz = Object.class;
    List<ReflectionAccessFilter> filters = Collections.emptyList();

    ReflectionAccessFilter.FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, clazz);

    assertEquals(getFilterResultEnum("ALLOW"), result);
  }
}