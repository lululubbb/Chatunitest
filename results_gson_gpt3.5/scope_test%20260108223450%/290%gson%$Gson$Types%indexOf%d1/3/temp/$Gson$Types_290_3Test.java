package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GsonTypesTest {

  private static Method indexOfMethod;

  @BeforeAll
  static void setUp() throws NoSuchMethodException {
    indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void indexOf_returnsIndexWhenElementFound() throws Throwable {
    String[] array = {"a", "b", "c"};
    Object toFind = "b";

    int index = invokeIndexOf(array, toFind);

    assertEquals(1, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_returnsZeroWhenFirstElementMatches() throws Throwable {
    Integer[] array = {10, 20, 30};
    Object toFind = 10;

    int index = invokeIndexOf(array, toFind);

    assertEquals(0, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_returnsLastIndexWhenLastElementMatches() throws Throwable {
    Double[] array = {1.1, 2.2, 3.3};
    Object toFind = 3.3;

    int index = invokeIndexOf(array, toFind);

    assertEquals(2, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_throwsNoSuchElementExceptionWhenNotFound() {
    String[] array = {"x", "y", "z"};
    Object toFind = "a";

    NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
      invokeIndexOf(array, toFind);
    });

    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void indexOf_throwsNullPointerExceptionWhenToFindIsNull() {
    String[] array = {"a", "b", "c"};
    Object toFind = null;

    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      invokeIndexOf(array, toFind);
    });

    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void indexOf_throwsNullPointerExceptionWhenArrayIsNull() {
    Object[] array = null;
    Object toFind = "a";

    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      invokeIndexOf(array, toFind);
    });

    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void indexOf_throwsNoSuchElementExceptionWhenArrayEmpty() {
    String[] array = new String[0];
    Object toFind = "a";

    NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
      invokeIndexOf(array, toFind);
    });

    assertNotNull(thrown);
  }

  private static int invokeIndexOf(Object[] array, Object toFind) throws Throwable {
    try {
      return (int) indexOfMethod.invoke(null, (Object) array, toFind);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}