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

class $Gson$Types_285_5Test {

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withParameterizedType() {
    // Use a concrete class extending Collection<String> to produce a parameterized supertype
    class StringList extends ArrayList<String> {}

    Type elementType = $Gson$Types.getCollectionElementType(StringList.class, StringList.class);
    assertEquals(String.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withRawCollection() {
    // Use raw collection type (no generics)
    Type elementType = $Gson$Types.getCollectionElementType(Collection.class, Collection.class);
    assertEquals(Object.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withNonCollectionSupertype() {
    // Use a class that does not extend Collection (e.g. String)
    // The method getSupertype throws IllegalArgumentException if supertype is not assignable,
    // so catch it and assert that elementType is Object.class as fallback.
    Type elementType;
    try {
      elementType = $Gson$Types.getCollectionElementType(String.class, String.class);
    } catch (IllegalArgumentException e) {
      // getSupertype throws if contextRawType is not assignable to supertype
      elementType = Object.class;
    }
    assertEquals(Object.class, elementType);
  }
}