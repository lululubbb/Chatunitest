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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConstructorConstructor_154_5Test {

  private ConstructorConstructor constructorConstructor;

  @BeforeEach
  void setUp() {
    constructorConstructor = new ConstructorConstructor(null, false, null);
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_EnumSetWithParameterizedType_ConstructsEnumSet() throws Exception {
    // Arrange
    Type enumSetType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {SampleEnum.class};
      }

      @Override
      public Type getRawType() {
        return EnumSet.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    // Act
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumSetType, EnumSet.class);
    Object instance = constructor.construct();

    // Assert
    assertTrue(instance instanceof EnumSet);
    assertTrue(((EnumSet<?>) instance).isEmpty());
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_EnumSetWithNonClassType_ThrowsJsonIOException() throws Exception {
    Type invalidEnumSetType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {new ParameterizedType() {
          @Override
          public Type[] getActualTypeArguments() { return new Type[0]; }
          @Override
          public Type getRawType() { return Object.class; }
          @Override
          public Type getOwnerType() { return null; }
        }};
      }

      @Override
      public Type getRawType() {
        return EnumSet.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(invalidEnumSetType, EnumSet.class);
      constructor.construct();
    });
    assertTrue(thrown.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_EnumSetWithNonParameterizedType_ThrowsJsonIOException() throws Exception {
    Type rawEnumSetType = EnumSet.class;

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(rawEnumSetType, EnumSet.class);
      constructor.construct();
    });
    assertTrue(thrown.getMessage().contains("Invalid EnumSet type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_EnumMapWithParameterizedType_ConstructsEnumMap() throws Exception {
    Type enumMapType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {SampleEnum.class, String.class};
      }

      @Override
      public Type getRawType() {
        return EnumMap.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(enumMapType, EnumMap.class);
    Object instance = constructor.construct();

    assertTrue(instance instanceof EnumMap);
    assertTrue(((EnumMap<?, ?>) instance).isEmpty());
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_EnumMapWithNonClassType_ThrowsJsonIOException() throws Exception {
    Type invalidEnumMapType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {new ParameterizedType() {
          @Override
          public Type[] getActualTypeArguments() { return new Type[0]; }
          @Override
          public Type getRawType() { return Object.class; }
          @Override
          public Type getOwnerType() { return null; }
        }, String.class};
      }

      @Override
      public Type getRawType() {
        return EnumMap.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(invalidEnumMapType, EnumMap.class);
      constructor.construct();
    });
    assertTrue(thrown.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_EnumMapWithNonParameterizedType_ThrowsJsonIOException() throws Exception {
    Type rawEnumMapType = EnumMap.class;

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(rawEnumMapType, EnumMap.class);
      constructor.construct();
    });
    assertTrue(thrown.getMessage().contains("Invalid EnumMap type"));
  }

  @Test
    @Timeout(8000)
  void newSpecialCollectionConstructor_OtherRawType_ReturnsNull() throws Exception {
    ObjectConstructor<?> constructor = invokeNewSpecialCollectionConstructor(Object.class, Object.class);
    assertNull(constructor);
  }

  // Helper method to invoke private static newSpecialCollectionConstructor via reflection
  @SuppressWarnings("unchecked")
  private <T> com.google.gson.internal.ObjectConstructor<T> invokeNewSpecialCollectionConstructor(Type type, Class<? super T> rawType)
      throws Exception {
    var method = ConstructorConstructor.class.getDeclaredMethod("newSpecialCollectionConstructor", Type.class, Class.class);
    method.setAccessible(true);
    return (com.google.gson.internal.ObjectConstructor<T>) method.invoke(null, type, rawType);
  }

  private enum SampleEnum {
    A, B, C
  }
}