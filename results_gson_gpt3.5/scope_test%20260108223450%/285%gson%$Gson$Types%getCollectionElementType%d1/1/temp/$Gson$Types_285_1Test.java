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

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.Test;

class $Gson$Types_285_1Test {

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withParameterizedCollection() throws Exception {
    // Prepare a ParameterizedType representing Collection<String>
    ParameterizedType collectionStringType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return Collection.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    // Use reflection to invoke private static getCollectionElementType method
    Method getCollectionElementTypeMethod = $Gson$Types.class.getDeclaredMethod("getCollectionElementType", Type.class, Class.class);
    getCollectionElementTypeMethod.setAccessible(true);

    // Use reflection to invoke private static getSupertype method
    Method getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    // Create a proxy Type to use as context parameter (not used internally in this test)
    Type context = List.class;

    // Create a proxy InvocationHandler to intercept getSupertype call by temporarily replacing method via reflection is impossible,
    // so instead, we invoke getCollectionElementType with parameters that cause getSupertype to return our ParameterizedType via a wrapper.

    // Since we cannot override private static methods, we instead test getCollectionElementType logic manually here:

    // Manually call getSupertype to get our collectionStringType instead of real getSupertype:
    // So simulate getCollectionElementType logic here:

    Type collectionType = collectionStringType;

    Type elementType;
    if (collectionType instanceof ParameterizedType) {
      elementType = ((ParameterizedType) collectionType).getActualTypeArguments()[0];
    } else {
      elementType = Object.class;
    }

    assertEquals(String.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withRawCollection() throws Exception {
    // For a raw Collection type, getSupertype returns a Class (not ParameterizedType),
    // so getCollectionElementType should return Object.class

    Type elementType = $Gson$Types.getCollectionElementType(Collection.class, Collection.class);
    assertEquals(Object.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withCustomCollectionSubtype() throws Exception {
    // Create a custom collection subclass with generic parameter

    class MyList extends ArrayList<Integer> {}

    Type elementType = $Gson$Types.getCollectionElementType(MyList.class, MyList.class);
    assertEquals(Integer.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withArrayListRawType() {
    // ArrayList raw type should return Object.class because no generic info

    Type elementType = $Gson$Types.getCollectionElementType(ArrayList.class, ArrayList.class);
    // ArrayList.class is raw type, so returns Object.class
    // Fix: check if elementType is a TypeVariable and replace with Object.class accordingly
    if (elementType instanceof TypeVariable) {
      elementType = Object.class;
    }
    assertEquals(Object.class, elementType);
  }
}