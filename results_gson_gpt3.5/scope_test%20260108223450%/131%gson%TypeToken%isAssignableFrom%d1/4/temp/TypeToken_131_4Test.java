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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeToken_isAssignableFromTest {

  private Map<String, Type> emptyMap;

  @BeforeEach
  void setup() {
    emptyMap = new HashMap<>();
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullFrom_returnsFalse() throws Exception {
    // from = null
    ParameterizedType to = Mockito.mock(ParameterizedType.class);
    boolean result = invokeIsAssignableFrom(null, to, emptyMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_toEqualsFrom_returnsTrue() throws Exception {
    ParameterizedType to = Mockito.mock(ParameterizedType.class);
    // from == to
    boolean result = invokeIsAssignableFrom(to, to, emptyMap);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_fromParameterizedType_typeEqualsTrue_returnsTrue() throws Exception {
    // Setup a ParameterizedType from and to with matching raw types and type arguments
    Class<?> rawClass = SampleGeneric.class;

    ParameterizedType from = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return rawClass;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType other = (ParameterizedType) o;
        return rawClass.equals(other.getRawType());
      }
    };

    ParameterizedType to = from;

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(from)).thenReturn(rawClass);
      mocked.when(() -> $Gson$Types.getRawType(to)).thenReturn(rawClass);
      boolean result = invokeIsAssignableFrom(from, to, new HashMap<>());
      assertTrue(result);
    }
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_fromParameterizedType_typeEqualsFalse_interfaceMatch_returnsTrue() throws Exception {
    // Setup from as a ParameterizedType implementing an interface matching to
    Class<?> rawClass = SampleGenericWithInterface.class;

    ParameterizedType to = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return SampleInterface.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType other = (ParameterizedType) o;
        return SampleInterface.class.equals(other.getRawType());
      }
    };

    ParameterizedType from = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return rawClass;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType other = (ParameterizedType) o;
        return rawClass.equals(other.getRawType());
      }
    };

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(from)).thenReturn(rawClass);
      mocked.when(() -> $Gson$Types.getRawType(to)).thenReturn(SampleInterface.class);
      // Mock getRawType for interfaces implemented by rawClass
      mocked.when(() -> $Gson$Types.getRawType(SampleInterface.class)).thenReturn(SampleInterface.class);
      // Mock getGenericInterfaces to return to for rawClass
      mocked.when(() -> $Gson$Types.getRawType(Mockito.any())).thenCallRealMethod();

      boolean result = invokeIsAssignableFrom(from, to, new HashMap<>());
      assertTrue(result);
    }
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_fromParameterizedType_typeEqualsFalse_interfaceNoMatch_superclassMatch_returnsTrue() throws Exception {
    // Setup from as ParameterizedType whose superclass matches to
    Class<?> rawClass = SampleGenericWithSuperclass.class;

    ParameterizedType to = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return SampleSuperclass.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType other = (ParameterizedType) o;
        return SampleSuperclass.class.equals(other.getRawType());
      }
    };

    ParameterizedType from = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return rawClass;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType other = (ParameterizedType) o;
        return rawClass.equals(other.getRawType());
      }
    };

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(from)).thenReturn(rawClass);
      mocked.when(() -> $Gson$Types.getRawType(to)).thenReturn(SampleSuperclass.class);
      // Mock getRawType for superclass of rawClass
      mocked.when(() -> $Gson$Types.getRawType(SampleSuperclass.class)).thenReturn(SampleSuperclass.class);
      // Mock getGenericSuperclass to return to for rawClass
      mocked.when(() -> $Gson$Types.getRawType(Mockito.any())).thenCallRealMethod();

      boolean result = invokeIsAssignableFrom(from, to, new HashMap<>());
      assertTrue(result);
    }
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_fromNotParameterizedType_interfaceAndSuperclassNoMatch_returnsFalse() throws Exception {
    Class<?> rawClass = String.class;
    ParameterizedType to = Mockito.mock(ParameterizedType.class);

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(Mockito.any())).thenReturn(rawClass);
      // from is not ParameterizedType
      boolean result = invokeIsAssignableFrom(rawClass, to, new HashMap<>());
      assertFalse(result);
    }
  }

  // Helper method to invoke private static boolean isAssignableFrom(Type, ParameterizedType, Map<String, Type>)
  private static boolean invokeIsAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap)
      throws Exception {
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeVarMap);
  }

  // Sample classes to support tests
  static class SampleGeneric<T> {}

  interface SampleInterface<T> {}

  static class SampleGenericWithInterface implements SampleInterface<String> {}

  static class SampleSuperclass<T> {}

  static class SampleGenericWithSuperclass extends SampleSuperclass<String> {}
}