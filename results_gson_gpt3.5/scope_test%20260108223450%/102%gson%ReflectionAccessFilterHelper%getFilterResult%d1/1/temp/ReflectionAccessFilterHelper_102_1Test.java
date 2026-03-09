package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ReflectionAccessFilterHelper_102_1Test {

  @Test
    @Timeout(8000)
  void getFilterResult_returnsAllow_whenNoFilters() {
    List<ReflectionAccessFilter> filters = Collections.emptyList();
    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);
    assertEquals(FilterResult.ALLOW, result);
  }

  @Test
    @Timeout(8000)
  void getFilterResult_returnsFirstNonIndecisiveResult_allow() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);
    when(filter1.check(String.class)).thenReturn(FilterResult.INDECISIVE);
    when(filter2.check(String.class)).thenReturn(FilterResult.ALLOW);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2);
    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);

    assertEquals(FilterResult.ALLOW, result);
    verify(filter1).check(String.class);
    verify(filter2).check(String.class);
  }

  @Test
    @Timeout(8000)
  void getFilterResult_returnsFirstNonIndecisiveResult_deny() throws Exception {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);

    // Use reflection to get DENY constant from FilterResult enum
    FilterResult denyResult = (FilterResult) Class.forName("com.google.gson.ReflectionAccessFilter$FilterResult")
        .getField("DENY").get(null);

    when(filter1.check(String.class)).thenReturn(denyResult);
    when(filter2.check(String.class)).thenReturn(FilterResult.ALLOW);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2);
    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);

    assertEquals(denyResult, result);
    verify(filter1).check(String.class);
    verify(filter2, never()).check(String.class);
  }

  @Test
    @Timeout(8000)
  void getFilterResult_allIndecisive_returnsAllow() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);
    when(filter1.check(String.class)).thenReturn(FilterResult.INDECISIVE);
    when(filter2.check(String.class)).thenReturn(FilterResult.INDECISIVE);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2);
    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, String.class);

    assertEquals(FilterResult.ALLOW, result);
    verify(filter1).check(String.class);
    verify(filter2).check(String.class);
  }
}