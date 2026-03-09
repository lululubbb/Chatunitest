package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.InstanceCreator;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.reflect.ReflectionHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_154_1Test {

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_withValidEnumSetParameterizedType_constructsEnumSet() throws Exception {
    Type enumSetType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {SampleEnum.class};
      }
      @Override public Type getRawType() {
        return EnumSet.class;
      }
      @Override public Type getOwnerType() {
        return null;
      }
    };
    @SuppressWarnings("unchecked")
    Class<? super EnumSet<SampleEnum>> rawType = (Class<? super EnumSet<SampleEnum>>) (Class<?>) EnumSet.class;

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<EnumSet<SampleEnum>> constructor =
        (ConstructorConstructor.ObjectConstructor<EnumSet<SampleEnum>>) invokeNewSpecialCollectionConstructor(enumSetType, rawType);

    EnumSet<SampleEnum> result = constructor.construct();
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(EnumSet.noneOf(SampleEnum.class), result);
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_withInvalidEnumSetType_throwsJsonIOException() throws Exception {
    Type invalidType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {new Object() {}.getClass()};
      }
      @Override public Type getRawType() {
        return EnumSet.class;
      }
      @Override public Type getOwnerType() {
        return null;
      }
    };
    @SuppressWarnings("unchecked")
    Class<? super EnumSet<?>> rawType = (Class<? super EnumSet<?>>) (Class<?>) EnumSet.class;

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<EnumSet<?>> constructor =
        (ConstructorConstructor.ObjectConstructor<EnumSet<?>>) invokeNewSpecialCollectionConstructor(invalidType, rawType);

    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_withNonParameterizedEnumSetType_throwsJsonIOException() throws Exception {
    Type nonParameterizedType = EnumSet.class;
    @SuppressWarnings("unchecked")
    Class<? super EnumSet<?>> rawType = (Class<? super EnumSet<?>>) (Class<?>) EnumSet.class;

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<EnumSet<?>> constructor =
        (ConstructorConstructor.ObjectConstructor<EnumSet<?>>) invokeNewSpecialCollectionConstructor(nonParameterizedType, rawType);

    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_withValidEnumMapParameterizedType_constructsEnumMap() throws Exception {
    Type enumMapType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {SampleEnum.class, String.class};
      }
      @Override public Type getRawType() {
        return EnumMap.class;
      }
      @Override public Type getOwnerType() {
        return null;
      }
    };
    @SuppressWarnings("unchecked")
    Class<? super EnumMap<SampleEnum, String>> rawType = (Class<? super EnumMap<SampleEnum, String>>) (Class<?>) EnumMap.class;

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<EnumMap<SampleEnum, String>> constructor =
        (ConstructorConstructor.ObjectConstructor<EnumMap<SampleEnum, String>>) invokeNewSpecialCollectionConstructor(enumMapType, rawType);

    EnumMap<SampleEnum, String> result = constructor.construct();
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(new EnumMap<>(SampleEnum.class), result);
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_withInvalidEnumMapType_throwsJsonIOException() throws Exception {
    Type invalidType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {new Object() {}.getClass(), String.class};
      }
      @Override public Type getRawType() {
        return EnumMap.class;
      }
      @Override public Type getOwnerType() {
        return null;
      }
    };
    @SuppressWarnings("unchecked")
    Class<? super EnumMap<?, String>> rawType = (Class<? super EnumMap<?, String>>) (Class<?>) EnumMap.class;

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<EnumMap<?, String>> constructor =
        (ConstructorConstructor.ObjectConstructor<EnumMap<?, String>>) invokeNewSpecialCollectionConstructor(invalidType, rawType);

    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_withNonParameterizedEnumMapType_throwsJsonIOException() throws Exception {
    Type nonParameterizedType = EnumMap.class;
    @SuppressWarnings("unchecked")
    Class<? super EnumMap<?, ?>> rawType = (Class<? super EnumMap<?, ?>>) (Class<?>) EnumMap.class;

    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<EnumMap<?, ?>> constructor =
        (ConstructorConstructor.ObjectConstructor<EnumMap<?, ?>>) invokeNewSpecialCollectionConstructor(nonParameterizedType, rawType);

    JsonIOException ex = assertThrows(JsonIOException.class, constructor::construct);
    assertTrue(ex.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_withNonCollectionType_returnsNull() throws Exception {
    Type stringType = String.class;
    Class<? super String> rawType = String.class;

    Object constructor = invokeNewSpecialCollectionConstructor(stringType, rawType);

    assertNull(constructor);
  }

  @SuppressWarnings("unchecked")
  private static <T> ConstructorConstructor.ObjectConstructor<T> invokeNewSpecialCollectionConstructor(Type type, Class<? super T> rawType) throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newSpecialCollectionConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (ConstructorConstructor.ObjectConstructor<T>) method.invoke(null, type, rawType);
  }

  private enum SampleEnum {
    A, B
  }
}