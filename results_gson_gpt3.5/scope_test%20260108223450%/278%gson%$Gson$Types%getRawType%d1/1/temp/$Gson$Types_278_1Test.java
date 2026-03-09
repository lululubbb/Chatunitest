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
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class GsonTypesGetRawTypeTest {

  @Test
    @Timeout(8000)
  public void testGetRawType_withClass() {
    Class<String> clazz = String.class;
    Class<?> result = $Gson$Types.getRawType(clazz);
    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withParameterizedType() throws Exception {
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    when(parameterizedType.getRawType()).thenReturn(String.class);

    try (MockedStatic<com.google.gson.internal.$Gson$Preconditions> preconditions = Mockito.mockStatic(com.google.gson.internal.$Gson$Preconditions.class)) {
      preconditions.when(() -> com.google.gson.internal.$Gson$Preconditions.checkArgument(true)).thenAnswer(invocation -> null);
      Class<?> result = $Gson$Types.getRawType(parameterizedType);
      assertSame(String.class, result);
      preconditions.verify(() -> com.google.gson.internal.$Gson$Preconditions.checkArgument(true));
    }
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withGenericArrayType() throws Exception {
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    when(genericArrayType.getGenericComponentType()).thenReturn(String.class);

    Class<?> result = $Gson$Types.getRawType(genericArrayType);
    assertTrue(result.isArray());
    assertSame(String.class, result.getComponentType());
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withTypeVariable() throws Exception {
    // Create a TypeVariable instance via reflection from a generic class
    TypeVariable<?> typeVariable = SampleClass.class.getTypeParameters()[0];
    Class<?> result = $Gson$Types.getRawType(typeVariable);
    assertSame(Object.class, result);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withWildcardType() {
    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {String.class};
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };
    Class<?> result = $Gson$Types.getRawType(wildcardType);
    assertSame(String.class, result);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withNull_throws() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(null);
    });
    assertTrue(thrown.getMessage().contains("null"));
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withUnknownType_throws() {
    Type unknownType = new Type() {};
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(unknownType);
    });
    assertTrue(thrown.getMessage().contains(unknownType.getClass().getName()));
  }

  // Helper generic class to obtain a TypeVariable instance
  static class SampleClass<T> {}
}