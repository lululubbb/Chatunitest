package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;

class $Gson$Types_286_6Test {

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_propertiesReturnsStringString() {
    Type[] result = $Gson$Types.getMapKeyAndValueTypes(Properties.class, Properties.class);
    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(String.class, result[0]);
    assertEquals(String.class, result[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_parameterizedMapType() throws Exception {
    // Create a ParameterizedType representing Map<String, Integer>
    ParameterizedType mapStringInteger = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class, Integer.class };
      }

      @Override
      public Type getRawType() {
        return Map.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    // Use reflection to invoke private static getSupertype to return our ParameterizedType
    var getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    // Mock getSupertype to return our ParameterizedType when called from getMapKeyAndValueTypes
    // Since getSupertype is private static, we cannot mock it directly.
    // Instead, create a subclass of $Gson$Types with a static method to call getMapKeyAndValueTypes with a context that triggers getSupertype to return our mocked ParameterizedType.
    // This is not feasible here, so test the public method with a real class implementing Map with parameters.

    // Instead, test with a real class that extends Map<String, Integer>
    class StringIntegerMap implements Map<String, Integer> {
      @Override public int size() { return 0; }
      @Override public boolean isEmpty() { return false; }
      @Override public boolean containsKey(Object key) { return false; }
      @Override public boolean containsValue(Object value) { return false; }
      @Override public Integer get(Object key) { return null; }
      @Override public Integer put(String key, Integer value) { return null; }
      @Override public Integer remove(Object key) { return null; }
      @Override public void putAll(Map<? extends String, ? extends Integer> m) {}
      @Override public void clear() {}
      @Override public java.util.Set<String> keySet() { return null; }
      @Override public java.util.Collection<Integer> values() { return null; }
      @Override public java.util.Set<Entry<String, Integer>> entrySet() { return null; }
    }

    Type[] types = $Gson$Types.getMapKeyAndValueTypes(StringIntegerMap.class, StringIntegerMap.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(String.class, types[0]);
    assertEquals(Integer.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_nonParameterizedMapType() {
    // Use a class that implements Map raw type without parameters
    class RawMap implements Map {
      @Override public int size() { return 0; }
      @Override public boolean isEmpty() { return false; }
      @Override public boolean containsKey(Object key) { return false; }
      @Override public boolean containsValue(Object value) { return false; }
      @Override public Object get(Object key) { return null; }
      @Override public Object put(Object key, Object value) { return null; }
      @Override public Object remove(Object key) { return null; }
      @Override public void putAll(Map m) {}
      @Override public void clear() {}
      @Override public java.util.Set keySet() { return null; }
      @Override public java.util.Collection values() { return null; }
      @Override public java.util.Set entrySet() { return null; }
    }

    Type[] types = $Gson$Types.getMapKeyAndValueTypes(RawMap.class, RawMap.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(Object.class, types[0]);
    assertEquals(Object.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_mapClassReturnsObjectObject() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(Map.class, Map.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(Object.class, types[0]);
    assertEquals(Object.class, types[1]);
  }
}