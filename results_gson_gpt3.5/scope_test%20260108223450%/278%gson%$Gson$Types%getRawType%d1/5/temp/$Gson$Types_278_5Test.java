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

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import org.junit.jupiter.api.Test;

class GsonTypesGetRawTypeTest {

  @Test
    @Timeout(8000)
  public void testGetRawType_withClass() {
    Class<String> clazz = String.class;
    Class<?> rawType = $Gson$Types.getRawType(clazz);
    assertSame(String.class, rawType);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withParameterizedType() throws Exception {
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    when(parameterizedType.getRawType()).thenReturn(String.class);

    Class<?> rawType = $Gson$Types.getRawType(parameterizedType);

    assertSame(String.class, rawType);
    verify(parameterizedType).getRawType();
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withGenericArrayType() throws Exception {
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    Type componentType = String.class;
    when(genericArrayType.getGenericComponentType()).thenReturn(componentType);

    Class<?> rawType = $Gson$Types.getRawType(genericArrayType);

    assertTrue(rawType.isArray());
    assertSame(String.class, rawType.getComponentType());
    verify(genericArrayType).getGenericComponentType();
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withTypeVariable() throws Exception {
    TypeVariable<?> typeVariable = mock(TypeVariable.class);
    when(typeVariable.getBounds()).thenReturn(new Type[] {Object.class});

    // Since getRawType returns Object.class for any TypeVariable, verify this
    Class<?> rawType = $Gson$Types.getRawType(typeVariable);

    assertSame(Object.class, rawType);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withWildcardType() throws Exception {
    WildcardType wildcardType = mock(WildcardType.class);
    Type[] upperBounds = new Type[] {String.class};
    when(wildcardType.getUpperBounds()).thenReturn(upperBounds);

    Class<?> rawType = $Gson$Types.getRawType(wildcardType);

    assertSame(String.class, rawType);
    verify(wildcardType).getUpperBounds();
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withWildcardType_assertBoundsLength() throws Exception {
    WildcardType wildcardType = mock(WildcardType.class);
    Type[] upperBounds = new Type[] {String.class};
    when(wildcardType.getUpperBounds()).thenReturn(upperBounds);

    Class<?> clazz = $Gson$Types.class;
    Method getRawTypeMethod = clazz.getDeclaredMethod("getRawType", Type.class);
    getRawTypeMethod.setAccessible(true);
    Class<?> rawType = (Class<?>) getRawTypeMethod.invoke(null, wildcardType);

    assertSame(String.class, rawType);
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withInvalidType_throwsIllegalArgumentException() {
    Type invalidType = new Type() {
      @Override
      public String toString() {
        return "InvalidType";
      }
    };

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(invalidType);
    });

    assertTrue(thrown.getMessage().contains("Expected a Class, ParameterizedType, or GenericArrayType"));
    assertTrue(thrown.getMessage().contains("InvalidType"));
    assertTrue(thrown.getMessage().contains(invalidType.getClass().getName()));
  }

  @Test
    @Timeout(8000)
  public void testGetRawType_withNullType_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(null);
    });

    assertTrue(thrown.getMessage().contains("Expected a Class, ParameterizedType, or GenericArrayType"));
    assertTrue(thrown.getMessage().contains("null"));
  }
}