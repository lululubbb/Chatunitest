package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeTokenIsAssignableFromTest {

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_nullFrom_returnsFalse() throws Exception {
    ParameterizedType to = Mockito.mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    boolean result = invokeIsAssignableFrom(null, to, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_fromEqualsTo_returnsTrue() throws Exception {
    ParameterizedType to = Mockito.mock(ParameterizedType.class);
    boolean result = invokeIsAssignableFrom(to, to, new HashMap<>());
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_fromNotParameterizedRawTypeAndNoInterfacesOrSuperclass_returnsFalse() throws Exception {
    // from is a Class (not ParameterizedType), with no interfaces or superclass
    Class<?> from = EmptyClass.class;
    ParameterizedType to = Mockito.mock(ParameterizedType.class);
    // Mock $Gson$Types.getRawType to return from class
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(from)).thenReturn(from);
      // from is not ParameterizedType, so ptype null, no type args processed
      // EmptyClass has no interfaces and its superclass is Object
      // We mock to.equals(from) false by default
      boolean result = invokeIsAssignableFrom(from, to, new HashMap<>());
      assertFalse(result);
    }
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_fromParameterizedType_typeEqualsTrue_returnsTrue() throws Exception {
    // Setup ParameterizedType from and to, with matching type arguments
    Class<?> raw = SampleGeneric.class;
    TypeVariable<?>[] typeParams = raw.getTypeParameters();

    // Create a ParameterizedType from SampleGeneric<String>
    ParameterizedType from = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }
      @Override public Type getRawType() {
        return raw;
      }
      @Override public Type getOwnerType() {
        return null;
      }
      @Override public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType other = (ParameterizedType) o;
        return raw.equals(other.getRawType()) &&
               java.util.Arrays.equals(getActualTypeArguments(), other.getActualTypeArguments());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getActualTypeArguments()) ^ raw.hashCode();
      }
    };

    // Create a ParameterizedType to SampleGeneric<String>
    ParameterizedType to = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }
      @Override public Type getRawType() {
        return raw;
      }
      @Override public Type getOwnerType() {
        return null;
      }
      @Override public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType other = (ParameterizedType) o;
        return raw.equals(other.getRawType()) &&
               java.util.Arrays.equals(getActualTypeArguments(), other.getActualTypeArguments());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getActualTypeArguments()) ^ raw.hashCode();
      }
    };

    // We mock $Gson$Types.getRawType to return raw class
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(from)).thenReturn(raw);

      Map<String, Type> typeVarMap = new HashMap<>();
      boolean result = invokeIsAssignableFrom(from, to, typeVarMap);
      assertTrue(result);
      // typeVarMap should contain mapping for T -> String
      assertEquals(String.class, typeVarMap.get(typeParams[0].getName()));
    }
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_fromParameterizedType_typeEqualsFalse_checksInterfacesAndSuperclass() throws Exception {
    // Setup a class with interface and superclass to test recursive calls
    Class<?> raw = ClassWithInterface.class;
    ParameterizedType to = Mockito.mock(ParameterizedType.class);

    ParameterizedType from = Mockito.mock(ParameterizedType.class);
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(from)).thenReturn(raw);
      Mockito.when(from.getActualTypeArguments()).thenReturn(new Type[0]);
      Mockito.when(from.getRawType()).thenReturn(raw);

      // to.equals(from) false by default

      // The interface generic type should be tested
      boolean result = invokeIsAssignableFrom(from, to, new HashMap<>());
      // It will recurse through interface and superclass, expect false since to is mock
      assertFalse(result);
    }
  }

  // Helper method to invoke private static isAssignableFrom(Type, ParameterizedType, Map)
  private static boolean invokeIsAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap) throws Exception {
    java.lang.reflect.Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeVarMap);
  }

  // Supporting classes for tests
  private static class EmptyClass {}

  private static class SampleGeneric<T> {}

  private interface InterfaceExample {}

  private static class ClassWithInterface extends BaseClass implements InterfaceExample {}

  private static class BaseClass {}
}