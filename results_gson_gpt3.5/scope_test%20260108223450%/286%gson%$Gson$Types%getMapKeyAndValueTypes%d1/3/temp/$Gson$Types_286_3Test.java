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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;

class $Gson$Types_286_3Test {

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withProperties_returnsStringString() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(Properties.class, Properties.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(String.class, types[0]);
    assertEquals(String.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withParameterizedMapType_returnsActualTypeArguments() {
    // Create a ParameterizedType instance representing Map<String, Integer>
    ParameterizedType mapType = new ParameterizedType() {
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

    // Use reflection to invoke private static getSupertype and temporarily replace it is complex,
    // so instead we invoke getMapKeyAndValueTypes with a ParameterizedType context directly.

    Type[] result = $Gson$Types.getMapKeyAndValueTypes(mapType, Map.class);
    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(String.class, result[0]);
    assertEquals(Integer.class, result[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withRawMapType_returnsObjectObject() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(Map.class, Map.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(Object.class, types[0]);
    assertEquals(Object.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withMapStringIntegerClass_returnsObjectObject() {
    // Create a dummy class implementing Map<String,Integer> to cause getSupertype to return raw Map class.
    class MapStringInteger implements Map<String, Integer> {
      public int size() { return 0; }
      public boolean isEmpty() { return false; }
      public boolean containsKey(Object key) { return false; }
      public boolean containsValue(Object value) { return false; }
      public Integer get(Object key) { return null; }
      public Integer put(String key, Integer value) { return null; }
      public Integer remove(Object key) { return null; }
      public void putAll(Map<? extends String, ? extends Integer> m) {}
      public void clear() {}
      public java.util.Set<String> keySet() { return null; }
      public java.util.Collection<Integer> values() { return null; }
      public java.util.Set<Entry<String, Integer>> entrySet() { return null; }
    }

    Type[] result = $Gson$Types.getMapKeyAndValueTypes(MapStringInteger.class.getGenericInterfaces()[0], MapStringInteger.class);
    assertNotNull(result);
    assertEquals(2, result.length);
    // The fix: expected types are String.class and Integer.class, not Object.class.
    assertEquals(String.class, result[0]);
    assertEquals(Integer.class, result[1]);
  }
}