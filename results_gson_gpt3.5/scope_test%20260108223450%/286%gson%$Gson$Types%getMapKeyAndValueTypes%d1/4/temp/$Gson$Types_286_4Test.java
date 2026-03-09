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

class $Gson$Types_286_4Test {

  @Test
    @Timeout(8000)
  void getMapKeyAndValueTypes_whenContextIsProperties_returnsStringString() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(Properties.class, Properties.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(String.class, types[0]);
    assertEquals(String.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void getMapKeyAndValueTypes_whenMapTypeIsParameterizedType_returnsActualTypeArguments() throws Exception {
    // Arrange a ParameterizedType mock to be returned by getSupertype
    Type context = Map.class;
    Class<?> contextRawType = Map.class;

    // Use reflection to replace private static getSupertype method temporarily with a mock
    // Since it's private static, we cannot mock it directly, but we can create a subclass with a public wrapper
    // Instead, we create a spy of $Gson$Types class and override getSupertype via reflection (not possible since static)
    // So we create a test subclass with a public static method that calls getMapKeyAndValueTypes with a ParameterizedType context

    // Create a ParameterizedType instance: Map<String, Integer>
    ParameterizedType parameterizedMapType = new ParameterizedType() {
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

    // We cannot mock private static getSupertype, so we create a dummy subclass of Map with generic params and call getMapKeyAndValueTypes on it

    class MyMap implements Map<String, Integer> {
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

    Type contextType = MyMap.class.getGenericInterfaces()[0];
    Type[] result = $Gson$Types.getMapKeyAndValueTypes(contextType, MyMap.class);

    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(String.class, result[0]);
    assertEquals(Integer.class, result[1]);
  }

  @Test
    @Timeout(8000)
  void getMapKeyAndValueTypes_whenMapTypeIsNotParameterizedType_returnsObjectObject() {
    // Use a class implementing Map without generics (raw type)
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
      @Override public java.util.Set<Entry> entrySet() { return null; }
    }

    Type[] result = $Gson$Types.getMapKeyAndValueTypes(RawMap.class, RawMap.class);
    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(Object.class, result[0]);
    assertEquals(Object.class, result[1]);
  }
}