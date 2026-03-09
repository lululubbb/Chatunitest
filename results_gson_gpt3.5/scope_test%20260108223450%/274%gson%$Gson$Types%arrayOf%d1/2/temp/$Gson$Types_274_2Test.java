package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
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
import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GsonTypesArrayOfTest {

  @Test
    @Timeout(8000)
  void testArrayOf_withClassType_returnsGenericArrayTypeImpl() {
    Type componentType = String.class;
    GenericArrayType genericArrayType = $Gson$Types.arrayOf(componentType);
    assertNotNull(genericArrayType);
    assertEquals(componentType, genericArrayType.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void testArrayOf_withParameterizedType_returnsGenericArrayTypeImpl() throws Exception {
    // Use reflection to create a ParameterizedType mock
    Type rawType = Iterable.class;
    Type[] typeArguments = new Type[]{String.class};
    Type parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments);

    GenericArrayType genericArrayType = $Gson$Types.arrayOf(parameterizedType);
    assertNotNull(genericArrayType);
    assertEquals(parameterizedType, genericArrayType.getGenericComponentType());
  }

  @Test
    @Timeout(8000)
  void testArrayOf_internalGenericArrayTypeImpl_classExists() throws Exception {
    Type componentType = Integer.class;

    GenericArrayType genericArrayType = $Gson$Types.arrayOf(componentType);
    assertNotNull(genericArrayType);

    // Validate that the returned instance is indeed an instance of the internal GenericArrayTypeImpl class
    Class<?> genericArrayTypeImplClass = Class.forName("com.google.gson.internal.$Gson$Types$GenericArrayTypeImpl");
    assertTrue(genericArrayTypeImplClass.isInstance(genericArrayType));

    // Use reflection to access the private field 'componentType' in GenericArrayTypeImpl
    Field componentTypeField = genericArrayTypeImplClass.getDeclaredField("componentType");
    componentTypeField.setAccessible(true);
    Object fieldValue = componentTypeField.get(genericArrayType);
    assertEquals(componentType, fieldValue);
  }
}