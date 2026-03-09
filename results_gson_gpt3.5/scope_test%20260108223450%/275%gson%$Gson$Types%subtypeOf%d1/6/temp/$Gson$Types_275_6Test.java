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
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import com.google.gson.internal.$Gson$Types;

class GsonTypes_SubtypeOfTest {

  @Test
    @Timeout(8000)
  void subtypeOf_withWildcardType_returnsWildcardTypeWithSameUpperBounds() throws Exception {
    // Arrange
    WildcardType mockWildcard = mock(WildcardType.class);
    Type[] mockUpperBounds = new Type[] { String.class };
    when(mockWildcard.getUpperBounds()).thenReturn(mockUpperBounds);

    // Act
    WildcardType result = $Gson$Types.subtypeOf(mockWildcard);

    // Assert
    assertNotNull(result);
    // The returned WildcardType is an instance of $Gson$Types.WildcardTypeImpl (private inner class)
    // We use reflection to verify upper bounds
    Type[] upperBounds = (Type[]) getFieldValue(result, "upperBounds");
    Type[] lowerBounds = (Type[]) getFieldValue(result, "lowerBounds");
    assertArrayEquals(mockUpperBounds, upperBounds);
    assertArrayEquals(new Type[0], lowerBounds);
  }

  @Test
    @Timeout(8000)
  void subtypeOf_withNonWildcardType_returnsWildcardTypeWithBoundAsUpperBound() throws Exception {
    // Arrange
    Type bound = Number.class;

    // Act
    WildcardType result = $Gson$Types.subtypeOf(bound);

    // Assert
    assertNotNull(result);
    Type[] upperBounds = (Type[]) getFieldValue(result, "upperBounds");
    Type[] lowerBounds = (Type[]) getFieldValue(result, "lowerBounds");
    assertArrayEquals(new Type[] { bound }, upperBounds);
    assertArrayEquals(new Type[0], lowerBounds);
  }

  private Object getFieldValue(Object instance, String fieldName) throws Exception {
    Class<?> clazz = instance.getClass();
    while (clazz != null) {
      try {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy");
  }
}