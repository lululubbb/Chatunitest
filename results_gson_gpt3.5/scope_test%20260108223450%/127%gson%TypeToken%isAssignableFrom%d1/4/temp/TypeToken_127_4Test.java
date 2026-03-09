package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeTokenIsAssignableFromTest {

  private TypeToken<?> typeToken;
  private Class<?> rawType;

  @BeforeEach
  void setup() throws Exception {
    // Using reflection to create an instance of TypeToken with a specific type.
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    rawType = String.class;
    typeToken = constructor.newInstance(rawType);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withClass_sameClass() {
    assertTrue(typeToken.isAssignableFrom(String.class));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withClass_subClass() {
    // String isAssignableFrom Object should be false, but Object isAssignableFrom String is true.
    // Fixing test to reflect correct assignability: String is not assignable from Object.
    assertFalse(typeToken.isAssignableFrom(Object.class));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withClass_unrelatedClass() {
    assertFalse(typeToken.isAssignableFrom(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withClass_null() throws Exception {
    // Resolve ambiguity by explicitly calling the isAssignableFrom(Type) method via reflection
    Method isAssignableFromType = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromType.setAccessible(true);
    boolean result = (boolean) isAssignableFromType.invoke(typeToken, (Object) null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withTypeParameterizedType() throws Exception {
    // Create a ParameterizedType representing List<String>
    ParameterizedType listStringType = mock(ParameterizedType.class);
    when(listStringType.getRawType()).thenReturn(List.class);
    when(listStringType.getActualTypeArguments()).thenReturn(new Type[] {String.class});

    // Create a TypeToken with rawType List<String>
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> listToken = constructor.newInstance(listStringType);

    // Use reflection to invoke private isAssignableFrom(Type) method
    Method isAssignableFromType = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromType.setAccessible(true);

    // List<String> should be assignable from ArrayList<String> type
    ParameterizedType arrayListStringType = mock(ParameterizedType.class);
    when(arrayListStringType.getRawType()).thenReturn(ArrayList.class);
    when(arrayListStringType.getActualTypeArguments()).thenReturn(new Type[] {String.class});

    boolean result = (boolean) isAssignableFromType.invoke(listToken, arrayListStringType);
    assertTrue(result);

    // List<String> should not be assignable from ArrayList<Integer> type
    ParameterizedType arrayListIntegerType = mock(ParameterizedType.class);
    when(arrayListIntegerType.getRawType()).thenReturn(ArrayList.class);
    when(arrayListIntegerType.getActualTypeArguments()).thenReturn(new Type[] {Integer.class});

    result = (boolean) isAssignableFromType.invoke(listToken, arrayListIntegerType);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withTypeVariable() throws Exception {
    // Create a TypeVariable mock
    TypeVariable<?> typeVariable = mock(TypeVariable.class);
    when(typeVariable.getName()).thenReturn("T");
    when(typeVariable.getBounds()).thenReturn(new Type[] {Object.class});

    Method isAssignableFromType = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromType.setAccessible(true);

    // Should return false for TypeVariable input
    boolean result = (boolean) isAssignableFromType.invoke(typeToken, typeVariable);
    // The real implementation may vary, here we assert false as safe fallback
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withGenericArrayType() throws Exception {
    // Create a GenericArrayType mock representing String[]
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    when(genericArrayType.getGenericComponentType()).thenReturn(String.class);

    Method isAssignableFromType = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromType.setAccessible(true);

    // The rawType is String.class, test assignability from generic array type
    boolean result = (boolean) isAssignableFromType.invoke(typeToken, genericArrayType);
    // Likely false because String and String[] are different types
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_withTypeToken() {
    TypeToken<String> stringToken = TypeToken.get(String.class);
    TypeToken<Object> objectToken = TypeToken.get(Object.class);
    TypeToken<Integer> integerToken = TypeToken.get(Integer.class);

    // String is assignable from String
    assertTrue(stringToken.isAssignableFrom(stringToken));

    // Object is assignable from String
    assertTrue(objectToken.isAssignableFrom(stringToken));

    // String is not assignable from Integer
    assertFalse(stringToken.isAssignableFrom(integerToken));
  }
}