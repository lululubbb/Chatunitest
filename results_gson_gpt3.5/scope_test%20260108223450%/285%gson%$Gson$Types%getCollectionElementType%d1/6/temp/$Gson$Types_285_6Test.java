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

class $Gson$Types_285_6Test {

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withParameterizedCollection() throws Exception {
    // Prepare a ParameterizedType representing Collection<String>
    ParameterizedType collectionStringType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() { return new Type[] {String.class}; }
      @Override public Type getRawType() { return Collection.class; }
      @Override public Type getOwnerType() { return null; }
    };

    // Create a dummy class implementing Collection<String>
    class StringCollection implements Collection<String> {
      public int size() { return 0; }
      public boolean isEmpty() { return true; }
      public boolean contains(Object o) { return false; }
      public Iterator<String> iterator() { return Collections.emptyIterator(); }
      public Object[] toArray() { return new Object[0]; }
      public <T> T[] toArray(T[] a) { return a; }
      public boolean add(String e) { return false; }
      public boolean remove(Object o) { return false; }
      public boolean containsAll(Collection<?> c) { return false; }
      public boolean addAll(Collection<? extends String> c) { return false; }
      public boolean removeAll(Collection<?> c) { return false; }
      public boolean retainAll(Collection<?> c) { return false; }
      public void clear() {}
    }

    // Use a generic field to get a ParameterizedType representing Collection<String>
    class Holder {
      Collection<String> strings;
      Collection rawCollection;
    }

    Field fieldStrings = Holder.class.getDeclaredField("strings");
    Type context = fieldStrings.getGenericType();
    Class<?> contextRawType = (Class<?>) ((ParameterizedType) context).getRawType();

    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);
    assertEquals(String.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withRawCollection() throws Exception {
    // Use raw Collection type (no generics)
    Class<?> rawCollection = Collection.class;
    Type context = rawCollection;

    Type elementType = $Gson$Types.getCollectionElementType(context, rawCollection);
    assertEquals(Object.class, elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withSubtype() throws Exception {
    // Use a subclass of Collection without generic info, e.g. ArrayList raw type

    Type context = ArrayList.class;
    Class<?> contextRawType = ArrayList.class;

    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);
    // Since ArrayList is a raw type here, elementType should be Object.class
    assertEquals(Object.class, elementType instanceof TypeVariable ? Object.class : elementType);
  }

  @Test
    @Timeout(8000)
  void testGetCollectionElementType_withParameterizedSubtype() throws Exception {
    // Use a subclass of Collection with generics: ArrayList<String>
    // Use reflection to get generic superclass of ArrayList<String> field

    class Holder {
      ArrayList<String> stringList;
    }

    Field field = Holder.class.getDeclaredField("stringList");
    Type context = field.getGenericType();
    Class<?> contextRawType = (Class<?>) ((ParameterizedType) context).getRawType();

    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);
    assertEquals(String.class, elementType);
  }
}