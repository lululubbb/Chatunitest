package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TypeToken_isAssignableFromTest {

  private ParameterizedType toType;
  private Map<String, Type> typeVarMap;

  @BeforeEach
  void setUp() {
    typeVarMap = new HashMap<>();
  }

  private ParameterizedType createParameterizedType(final Class<?> raw, final Type... args) {
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return args;
      }

      @Override
      public Type getRawType() {
        return raw;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return raw.equals(that.getRawType()) &&
               java.util.Arrays.equals(args, that.getActualTypeArguments());
      }

      @Override
      public int hashCode() {
        return raw.hashCode() ^ java.util.Arrays.hashCode(args);
      }

      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(raw.getTypeName());
        if (args != null && args.length > 0) {
          sb.append("<");
          for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(args[i].getTypeName());
          }
          sb.append(">");
        }
        return sb.toString();
      }
    };
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullFrom_returnsFalse() throws Exception {
    // from == null -> false
    boolean result = invokeIsAssignableFrom(null, toType, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_toEqualsFrom_returnsTrue() throws Exception {
    // to.equals(from) -> true
    Class<?> raw = Map.class;
    Type[] actualArgs = new Type[] {String.class, Integer.class};
    toType = createParameterizedType(raw, actualArgs);

    boolean result = invokeIsAssignableFrom(toType, toType, new HashMap<>());
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_parameterizedTypeMatchesTypeEqualsTrue() throws Exception {
    // Setup from as ParameterizedType with type variables mapping to actual types
    Class<?> raw = Map.class;
    TypeVariable<?>[] tParams = raw.getTypeParameters();
    TypeVariable<?> kVar = tParams[0];
    TypeVariable<?> vVar = tParams[1];

    // from: Map<K, V>
    ParameterizedType from = createParameterizedType(raw, kVar, vVar);
    // to: Map<String, Integer>
    toType = createParameterizedType(raw, String.class, Integer.class);

    Map<String, Type> map = new HashMap<>();
    map.put(kVar.getName(), String.class);
    map.put(vVar.getName(), Integer.class);

    try (MockedStatic<$Gson$Types> mockedGsonTypes = Mockito.mockStatic($Gson$Types.class)) {
      mockedGsonTypes.when(() -> $Gson$Types.getRawType(from)).thenReturn(raw);
      boolean result = invokeIsAssignableFrom(from, toType, new HashMap<>());
      assertTrue(result);
    }
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_genericInterfaceMatches_returnsTrue() throws Exception {
    // from implements an interface that matches to
    Class<?> raw = MyList.class;
    TypeVariable<?>[] tParams = List.class.getTypeParameters();
    TypeVariable<?> eVar = tParams[0];

    // to: List<String>
    toType = createParameterizedType(List.class, String.class);

    // from: MyList<String> (MyList implements List<String>)
    ParameterizedType from = createParameterizedType(raw, String.class);

    try (MockedStatic<$Gson$Types> mockedGsonTypes = Mockito.mockStatic($Gson$Types.class)) {
      mockedGsonTypes.when(() -> $Gson$Types.getRawType(from)).thenReturn(raw);

      // Instead of spying Class<?> raw (which is not allowed), create a helper class that extends MyList and overrides getGenericInterfaces()
      Class<?> rawHelper = new MyListHelper().getClass();

      // Mock $Gson$Types.getRawType(from) to return rawHelper
      mockedGsonTypes.when(() -> $Gson$Types.getRawType(from)).thenReturn(rawHelper);

      boolean result = invokeIsAssignableFrom(from, toType, new HashMap<>());
      assertTrue(result);
    }
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_noMatchSuperclassChain_returnsFalse() throws Exception {
    // from has superclass chain that does not match to
    Class<?> raw = String.class;
    toType = createParameterizedType(Map.class, String.class, Integer.class);

    ParameterizedType from = createParameterizedType(raw);

    try (MockedStatic<$Gson$Types> mockedGsonTypes = Mockito.mockStatic($Gson$Types.class)) {
      mockedGsonTypes.when(() -> $Gson$Types.getRawType(from)).thenReturn(raw);

      // Use a helper class extending String (impossible since String is final), so instead just use raw directly and mock getGenericInterfaces and getGenericSuperclass via reflection
      // Since String is final and methods are final, we cannot mock them, so just rely on raw as is

      boolean result = invokeIsAssignableFrom(from, toType, new HashMap<>());
      assertFalse(result);
    }
  }

  // Helper to invoke private static method isAssignableFrom(Type, ParameterizedType, Map)
  private boolean invokeIsAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap) throws Exception {
    java.lang.reflect.Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeVarMap);
  }

  // Dummy class for interface test
  interface MyList<E> extends java.util.List<E> {}

  // Helper class that extends MyList<String> and overrides getGenericInterfaces to return toType
  static class MyListHelper implements MyList<String> {
    @Override
    public java.util.Iterator<String> iterator() {
      throw new UnsupportedOperationException();
    }
    @Override
    public int size() {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean contains(Object o) {
      throw new UnsupportedOperationException();
    }
    @Override
    public Object[] toArray() {
      throw new UnsupportedOperationException();
    }
    @Override
    public <T> T[] toArray(T[] a) {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean add(String e) {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean remove(Object o) {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean containsAll(java.util.Collection<?> c) {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean addAll(java.util.Collection<? extends String> c) {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean addAll(int index, java.util.Collection<? extends String> c) {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean removeAll(java.util.Collection<?> c) {
      throw new UnsupportedOperationException();
    }
    @Override
    public boolean retainAll(java.util.Collection<?> c) {
      throw new UnsupportedOperationException();
    }
    @Override
    public void clear() {
      throw new UnsupportedOperationException();
    }
    @Override
    public String get(int index) {
      throw new UnsupportedOperationException();
    }
    @Override
    public String set(int index, String element) {
      throw new UnsupportedOperationException();
    }
    @Override
    public void add(int index, String element) {
      throw new UnsupportedOperationException();
    }
    @Override
    public String remove(int index) {
      throw new UnsupportedOperationException();
    }
    @Override
    public int indexOf(Object o) {
      throw new UnsupportedOperationException();
    }
    @Override
    public int lastIndexOf(Object o) {
      throw new UnsupportedOperationException();
    }
    @Override
    public java.util.ListIterator<String> listIterator() {
      throw new UnsupportedOperationException();
    }
    @Override
    public java.util.ListIterator<String> listIterator(int index) {
      throw new UnsupportedOperationException();
    }
    @Override
    public java.util.List<String> subList(int fromIndex, int toIndex) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Type[] getGenericInterfaces() {
      return new Type[] {new ParameterizedType() {
        @Override
        public Type[] getActualTypeArguments() {
          return new Type[] {String.class};
        }
        @Override
        public Type getRawType() {
          return List.class;
        }
        @Override
        public Type getOwnerType() {
          return null;
        }
      }};
    }
  }
}