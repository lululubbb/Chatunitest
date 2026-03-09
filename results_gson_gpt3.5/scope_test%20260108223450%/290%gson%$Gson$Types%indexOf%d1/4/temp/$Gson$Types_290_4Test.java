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
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.lang.reflect.InvocationTargetException;

class GsonTypesIndexOfTest {

  @Test
    @Timeout(8000)
  void indexOf_findsElementInArray_returnsCorrectIndex() throws Exception {
    Object[] array = new Object[] { "a", "b", "c", "d" };
    Object toFind = "c";

    Method indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);

    int index = (int) indexOfMethod.invoke(null, (Object) array, toFind);

    assertEquals(2, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementAtFirstPosition_returnsZero() throws Exception {
    Object[] array = new Object[] { "first", "second", "third" };
    Object toFind = "first";

    Method indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);

    int index = (int) indexOfMethod.invoke(null, (Object) array, toFind);

    assertEquals(0, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementAtLastPosition_returnsLastIndex() throws Exception {
    Object[] array = new Object[] { "x", "y", "z" };
    Object toFind = "z";

    Method indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);

    int index = (int) indexOfMethod.invoke(null, (Object) array, toFind);

    assertEquals(array.length - 1, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementNotFound_throwsNoSuchElementException() throws Exception {
    Object[] array = new Object[] { "one", "two", "three" };
    Object toFind = "four";

    Method indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      indexOfMethod.invoke(null, (Object) array, toFind);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof NoSuchElementException);
  }

  @Test
    @Timeout(8000)
  void indexOf_nullElementInArray_throwsNullPointerException() throws Exception {
    Object[] array = new Object[] { "a", null, "c" };
    Object toFind = null;

    Method indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      indexOfMethod.invoke(null, (Object) array, toFind);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void indexOf_nullElementToFind_callsEqualsOnNull_throwsException() throws Exception {
    Object[] array = new Object[] { "a", "b", "c" };
    Object toFind = null;

    Method indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      indexOfMethod.invoke(null, (Object) array, toFind);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof NullPointerException);
  }
}