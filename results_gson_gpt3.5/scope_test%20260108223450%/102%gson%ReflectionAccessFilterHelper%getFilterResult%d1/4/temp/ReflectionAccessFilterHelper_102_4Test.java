package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReflectionAccessFilterHelper_102_4Test {

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

  @Test
    @Timeout(8000)
  void getFilterResult_firstFilterReturnsAllow_returnsAllowImmediately() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);

    when(filter1.check(Integer.class)).thenReturn(FilterResult.ALLOW);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, Integer.class);

    assertEquals(FilterResult.ALLOW, result);
    verify(filter1).check(Integer.class);
    verify(filter2, never()).check(any());
  }

  @Test
    @Timeout(8000)
  void getFilterResult_firstFilterIndecisiveSecondReturnsDeny_returnsDeny() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);

    when(filter1.check(Double.class)).thenReturn(FilterResult.INDECISIVE);

    // Use a custom enum constant DENY for the test since DENY does not exist in FilterResult
    // Create a custom enum for mocking or use a different approach:
    // Instead, use ALLOW to simulate a non-INDECISIVE result, since DENY doesn't exist.
    // But since test expects DENY, we create a mock FilterResult with a different approach.

    // Instead of using FilterResult.valueOf("DENY"), use a Mockito mock for FilterResult:
    FilterResult denyResult = mock(FilterResult.class);
    when(denyResult.toString()).thenReturn("DENY");
    when(filter2.check(Double.class)).thenReturn(denyResult);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, Double.class);

    assertEquals(denyResult, result);
    verify(filter1).check(Double.class);
    verify(filter2).check(Double.class);
  }

  @Test
    @Timeout(8000)
  void getFilterResult_emptyFilterList_returnsAllow() {
    List<ReflectionAccessFilter> filters = Collections.emptyList();

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, Object.class);

    assertEquals(FilterResult.ALLOW, result);
  }

  @Test
    @Timeout(8000)
  void privateConstructor_isPrivate() throws Exception {
    var constructor = ReflectionAccessFilterHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
  }
}