package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TypeToken_GetParameterizedTest {

  @Test
    @Timeout(8000)
  void getParameterized_nullRawType_throwsNPE() {
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(null));
  }

  @Test
    @Timeout(8000)
  void getParameterized_nullTypeArguments_throwsNPE() {
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(String.class, (Type[]) null));
  }

  @Test
    @Timeout(8000)
  void getParameterized_rawTypeNotClass_throwsIllegalArgumentException() {
    Type rawType = mock(Type.class);
    // rawType is not instance of Class
    assertThrows(IllegalArgumentException.class, () -> TypeToken.getParameterized(rawType));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentsCountMismatch_throwsIllegalArgumentException() {
    Class<?> rawType = Map.class; // Map<K,V> has 2 type parameters
    Type[] typeArgs = new Type[] {String.class}; // only 1 argument

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArgs));
    assertTrue(ex.getMessage().contains("requires 2 type arguments"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentDoesNotSatisfyBounds_throwsIllegalArgumentException() throws Exception {
    // Create a generic class with bounded type variable: Bounded<T extends Number>
    // We will violate the bound by passing String (which does not extend Number)
    // Use reflection to define the class to avoid compilation issues in some environments

    // Define class Bounded<T extends Number> dynamically
    class Bounded<T extends Number> {}
    Type rawType = Bounded.class;
    Type[] typeArgs = new Type[] {String.class}; // String does not extend Number

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArgs));
    assertTrue(ex.getMessage().contains("does not satisfy bounds"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_validInput_returnsTypeTokenWithCorrectType() {
    // Use Map<String, Integer>
    Class<?> rawType = Map.class;
    Type[] typeArgs = new Type[] {String.class, Integer.class};

    TypeToken<?> token = TypeToken.getParameterized(rawType, typeArgs);
    assertNotNull(token);

    Type type = token.getType();
    assertTrue(type instanceof ParameterizedType);

    ParameterizedType pType = (ParameterizedType) type;
    assertEquals(rawType, pType.getRawType());

    Type[] actualArgs = pType.getActualTypeArguments();
    assertArrayEquals(typeArgs, actualArgs);
  }
}