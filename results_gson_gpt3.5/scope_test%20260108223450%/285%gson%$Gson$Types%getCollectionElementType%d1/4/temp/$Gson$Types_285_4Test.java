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

class $Gson$Types_285_4Test {

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withParameterizedCollection() throws Exception {
    // Prepare a parameterized type representing List<String>
    ParameterizedType listStringType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{String.class};
      }
      @Override
      public Type getRawType() {
        return List.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    // Use getCollectionElementType directly by using a subclass with generic superclass
    class StringList extends ArrayList<String> {}
    Type genericSuper = StringList.class.getGenericSuperclass();

    Type elementType = $Gson$Types.getCollectionElementType(genericSuper, StringList.class);
    assertEquals(String.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withRawCollection() {
    // Use raw collection type (no parameterization)
    // Use raw ArrayList class as context (not anonymous subclass)
    Type context = ArrayList.class;
    Class<?> contextRawType = ArrayList.class;

    // For raw types, getCollectionElementType should return Object.class
    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);
    assertEquals(Object.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withCustomCollectionParameterized() {
    // Create a parameterized type for Collection<Integer>
    ParameterizedType collectionIntegerType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{Integer.class};
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

    // Use getCollectionElementType directly with this parameterized type and Collection.class
    Type elementType = $Gson$Types.getCollectionElementType(collectionIntegerType, Collection.class);
    assertEquals(Integer.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withCollectionWithoutTypeArguments() {
    // Create a subclass of Collection without type arguments by extending AbstractCollection raw
    class RawCollection extends AbstractCollection<Object> {
      @Override
      public Iterator<Object> iterator() { return Collections.emptyIterator(); }
      @Override
      public int size() { return 0; }
    }

    Type elementType = $Gson$Types.getCollectionElementType(RawCollection.class, RawCollection.class);
    // Should return Object.class because RawCollection is raw type
    assertEquals(Object.class, elementType);
  }

}