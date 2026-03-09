package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import org.junit.jupiter.api.Test;

class TypeToken_140_3Test {

  @Test
    @Timeout(8000)
  void getParameterized_validInput_returnsTypeToken() {
    Class<List> rawType = List.class;
    Type[] typeArgs = new Type[] {String.class};

    TypeToken<?> result = TypeToken.getParameterized(rawType, typeArgs);

    assertNotNull(result);
    Type tokenType = result.getType();
    assertTrue(tokenType instanceof ParameterizedType);
    ParameterizedType pType = (ParameterizedType) tokenType;
    assertEquals(rawType, pType.getRawType());
    assertArrayEquals(typeArgs, pType.getActualTypeArguments());
  }

  @Test
    @Timeout(8000)
  void getParameterized_nullRawType_throwsNullPointerException() {
    Type[] typeArgs = new Type[] {String.class};
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(null, typeArgs));
  }

  @Test
    @Timeout(8000)
  void getParameterized_nullTypeArguments_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(List.class, (Type[]) null));
  }

  @Test
    @Timeout(8000)
  void getParameterized_rawTypeNotClass_throwsIllegalArgumentException() {
    Type rawType = mock(Type.class);
    // Make sure rawType is not a Class instance
    assertFalse(rawType instanceof Class);

    Type[] typeArgs = new Type[] {String.class};
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArgs));
    assertTrue(ex.getMessage().contains("rawType must be of type Class"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_argumentCountMismatch_throwsIllegalArgumentException() {
    Class<List> rawType = List.class;
    Type[] typeArgs = new Type[0]; // List has 1 type parameter

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArgs));
    assertTrue(ex.getMessage().contains(rawType.getName()));
    assertTrue(ex.getMessage().contains("requires 1 type arguments"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentViolatesBounds_throwsIllegalArgumentException() {
    // Create rawType with bounded type variable: Comparable<T>
    Class<Comparable> rawType = Comparable.class;
    // Comparable<T> has one type param with bound Object (which all classes satisfy),
    // so to cause violation, we mock TypeVariable and $Gson$Types.getRawType to simulate a violation.
    // But since $Gson$Types is a final class with static methods, mocking is complex.
    // Instead, create a custom class with bounded type variable and a type argument that violates bounds.

    // We'll create a dummy rawType with a type parameter bounded by Number,
    // and pass String.class which does not satisfy Number.

    class Bounded<T extends Number> {}
    Type rawBoundedType = Bounded.class;
    Type[] args = new Type[] {String.class}; // String does not extend Number

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawBoundedType, args));
    assertTrue(ex.getMessage().contains("does not satisfy bounds"));
  }
}