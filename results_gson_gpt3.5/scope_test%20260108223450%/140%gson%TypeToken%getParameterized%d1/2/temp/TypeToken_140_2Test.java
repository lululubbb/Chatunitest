package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TypeToken_140_2Test {

  @Test
    @Timeout(8000)
  void getParameterized_nullRawType_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(null));
  }

  @Test
    @Timeout(8000)
  void getParameterized_nullTypeArguments_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> TypeToken.getParameterized(String.class, (Type[]) null));
  }

  @Test
    @Timeout(8000)
  void getParameterized_rawTypeNotClass_throwsIllegalArgumentException() {
    Type rawType = mock(Type.class);
    // rawType is not a Class instance
    assertThrows(IllegalArgumentException.class, () -> TypeToken.getParameterized(rawType));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentsCountMismatch_throwsIllegalArgumentException() {
    Class<?> rawType = HashMap.class; // Has 2 type parameters
    Type[] typeArgs = new Type[] {String.class};
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArgs));
    assertTrue(ex.getMessage().contains(HashMap.class.getName()));
    assertTrue(ex.getMessage().contains("requires 2"));
    assertTrue(ex.getMessage().contains("but got 1"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_typeArgumentDoesNotSatisfyBounds_throwsIllegalArgumentException() {
    // Create a class with a type variable bound, e.g. Comparable<T>
    class ComparableString implements Comparable<String> {
      @Override
      public int compareTo(String o) { return 0; }
    }
    Class<?> rawType = Comparable.class; // Comparable<T extends Object>
    Type[] typeArgs = new Type[] {int.class}; // int.class is primitive and not assignable to Object
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> TypeToken.getParameterized(rawType, typeArgs));
    assertTrue(ex.getMessage().contains("does not satisfy bounds"));
  }

  @Test
    @Timeout(8000)
  void getParameterized_validArguments_returnsTypeToken() {
    Class<?> rawType = Map.class; // Map<K,V>
    Type keyType = String.class;
    Type valueType = Integer.class;

    TypeToken<?> token = TypeToken.getParameterized(rawType, keyType, valueType);
    assertNotNull(token);

    // Check that the TypeToken's type is a ParameterizedType with expected raw type and args
    Type type = token.getType();
    assertTrue(type instanceof ParameterizedType);
    ParameterizedType pType = (ParameterizedType) type;
    assertEquals(rawType, pType.getRawType());
    Type[] args = pType.getActualTypeArguments();
    assertEquals(2, args.length);
    assertEquals(keyType, args[0]);
    assertEquals(valueType, args[1]);
  }

  @Test
    @Timeout(8000)
  void getParameterized_withOwnerType_nullOwnerHandled() throws Exception {
    // Use reflection to verify that newParameterizedTypeWithOwner is called with null owner
    // We cannot mock static $Gson$Types easily, so we test indirectly by checking the created type

    Class<?> rawType = Map.class;
    Type[] typeArgs = new Type[] {String.class, Integer.class};
    TypeToken<?> token = TypeToken.getParameterized(rawType, typeArgs);
    Type type = token.getType();

    assertTrue(type instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) type;
    assertNull(pt.getOwnerType());
  }
}