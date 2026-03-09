package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
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
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Map;

import org.junit.jupiter.api.Test;

class GsonTypesTest {

  @Test
    @Timeout(8000)
  public void testGetRawType_withClass() {
    Class<String> clazz = String.class;
    Class<?> rawType = $Gson$Types.getRawType(clazz);
    assertSame(String.class, rawType);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withParameterizedType() {
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    when(parameterizedType.getRawType()).thenReturn(Map.class);
    Class<?> rawType = $Gson$Types.getRawType(parameterizedType);
    assertSame(Map.class, rawType);
    verify(parameterizedType).getRawType();
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withGenericArrayType() throws Exception {
    // Create a GenericArrayType for String[]
    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return String.class;
      }
    };
    Class<?> rawType = $Gson$Types.getRawType(genericArrayType);
    assertTrue(rawType.isArray());
    assertSame(String.class, rawType.getComponentType());
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withTypeVariable() throws Exception {
    // Create a TypeVariable using reflection to avoid mocking issues
    TypeVariable<?> typeVariable = createTypeVariable();
    Class<?> rawType = $Gson$Types.getRawType(typeVariable);
    assertSame(Object.class, rawType);
  }

  private static TypeVariable<?> createTypeVariable() throws Exception {
    class GenericClass<T> {}
    TypeVariable<?>[] typeParameters = GenericClass.class.getTypeParameters();
    return typeParameters[0];
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withWildcardType() {
    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Number.class};
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };
    Class<?> rawType = $Gson$Types.getRawType(wildcardType);
    assertSame(Number.class, rawType);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withWildcardType_multipleBounds_assertion() {
    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Number.class, Serializable.class};
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };
    // The method asserts bounds.length == 1, triggering AssertionError if assertions enabled
    // But since assertions are usually disabled in test env, it will proceed.
    // We test that it returns the first bound.
    Class<?> rawType = $Gson$Types.getRawType(wildcardType);
    assertSame(Number.class, rawType);
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
}