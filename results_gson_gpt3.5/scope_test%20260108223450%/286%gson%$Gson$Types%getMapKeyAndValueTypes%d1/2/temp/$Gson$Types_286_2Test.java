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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class $Gson$Types_286_2Test {

  private Type mapTypeRaw;
  private Type mapTypeParameterized;

  private Class<?> mapRawClass;

  @BeforeEach
  void setUp() {
    mapRawClass = Map.class;
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_propertiesClass() {
    Type[] result = $Gson$Types.getMapKeyAndValueTypes(Properties.class, Properties.class);
    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(String.class, result[0]);
    assertEquals(String.class, result[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_parameterizedMapType() {
    // For this test, create a dummy class extending Map<String, Integer> to simulate parameterized map type
    class MapStringInteger implements Map<String, Integer> {
      @Override public int size() {return 0;}
      @Override public boolean isEmpty() {return false;}
      @Override public boolean containsKey(Object key) {return false;}
      @Override public boolean containsValue(Object value) {return false;}
      @Override public Integer get(Object key) {return null;}
      @Override public Integer put(String key, Integer value) {return null;}
      @Override public Integer remove(Object key) {return null;}
      @Override public void putAll(Map<? extends String, ? extends Integer> m) {}
      @Override public void clear() {}
      @Override public java.util.Set<String> keySet() {return null;}
      @Override public java.util.Collection<Integer> values() {return null;}
      @Override public java.util.Set<Entry<String, Integer>> entrySet() {return null;}
    }

    Type context = MapStringInteger.class;
    Class<?> contextRawType = MapStringInteger.class;

    Type[] result = $Gson$Types.getMapKeyAndValueTypes(context, contextRawType);

    assertNotNull(result);
    assertEquals(2, result.length);
    // Since MapStringInteger is a parameterized type implementing Map<String, Integer>,
    // the method returns String.class and Integer.class
    assertEquals(String.class, result[0]);
    assertEquals(Integer.class, result[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_nonParameterizedMapType() {
    // Use raw Map class as context and contextRawType
    Type context = Map.class;
    Class<?> contextRawType = Map.class;

    Type[] result = $Gson$Types.getMapKeyAndValueTypes(context, contextRawType);

    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(Object.class, result[0]);
    assertEquals(Object.class, result[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_nullContext() {
    // Passing null context should lead to Object.class, Object.class as no supertype found
    Type[] result = $Gson$Types.getMapKeyAndValueTypes(null, Map.class);
    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(Object.class, result[0]);
    assertEquals(Object.class, result[1]);
  }
}