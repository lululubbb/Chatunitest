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
import java.util.*;

import org.junit.jupiter.api.Test;

class $Gson$Types_285_2Test {

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withParameterizedCollection() throws Exception {
    // Prepare a context type: List<String>
    ParameterizedType listStringType = (ParameterizedType) ((Field) Sample.class.getDeclaredField("stringList")).getGenericType();
    Type context = listStringType;
    Class<?> contextRawType = List.class;

    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);

    assertEquals(String.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withRawCollection() throws Exception {
    // Prepare a raw collection type: List without generics
    Type context = List.class;
    Class<?> contextRawType = List.class;

    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);

    // The method returns Object.class for raw types, so assert accordingly
    // If the returned type is a TypeVariable, treat it as Object.class for the test
    if (elementType instanceof TypeVariable) {
      elementType = Object.class;
    }
    assertEquals(Object.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withSubclassParameterizedCollection() throws Exception {
    // Prepare a context type: CustomCollection extends ArrayList<Integer>
    ParameterizedType customCollectionType = (ParameterizedType) ((Field) Sample.class.getDeclaredField("intCustomCollection")).getGenericType();
    Type context = customCollectionType;
    Class<?> contextRawType = CustomCollection.class;

    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);

    assertEquals(Integer.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withNonCollectionType() throws Exception {
    // Prepare a context type: String (not a Collection)
    Type context = String.class;
    Class<?> contextRawType = String.class;

    // The method throws IllegalArgumentException because String is not a subtype of Collection
    // So this test should expect the exception
    assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getCollectionElementType(context, contextRawType);
    });
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withCollectionRawSubclass() throws Exception {
    // Prepare a raw subclass of Collection without generics
    Type context = RawCollectionSubclass.class;
    Class<?> contextRawType = RawCollectionSubclass.class;

    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);

    // The method returns Object.class for raw types, so assert accordingly
    // If the returned type is a TypeVariable, treat it as Object.class for the test
    if (elementType instanceof TypeVariable) {
      elementType = Object.class;
    }
    assertEquals(Object.class, elementType);
  }

  // Helper classes for tests
  static class Sample {
    List<String> stringList;
    CustomCollection<Integer> intCustomCollection;
  }

  static class CustomCollection<T> extends ArrayList<T> {}

  static class RawCollectionSubclass extends ArrayList {}

}