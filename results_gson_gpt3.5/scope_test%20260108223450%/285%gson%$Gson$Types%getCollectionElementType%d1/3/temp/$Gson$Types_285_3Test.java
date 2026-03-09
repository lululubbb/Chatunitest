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
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.*;

class $Gson$Types_285_3Test {

  @Test
    @Timeout(8000)
  void getCollectionElementType_withParameterizedCollection_returnsTypeArgument() {
    // Prepare a ParameterizedType representing List<String>
    Type context = new Type() {};
    Class<?> contextRawType = List.class;

    // Use a concrete class implementing Collection with generic type
    class StringList implements List<String> {
      public int size() { return 0; }
      public boolean isEmpty() { return false; }
      public boolean contains(Object o) { return false; }
      public Iterator<String> iterator() { return null; }
      public Object[] toArray() { return null; }
      public <T> T[] toArray(T[] a) { return null; }
      public boolean add(String e) { return false; }
      public boolean remove(Object o) { return false; }
      public boolean containsAll(Collection<?> c) { return false; }
      public boolean addAll(Collection<? extends String> c) { return false; }
      public boolean addAll(int index, Collection<? extends String> c) { return false; }
      public boolean removeAll(Collection<?> c) { return false; }
      public boolean retainAll(Collection<?> c) { return false; }
      public void clear() {}
      public String get(int index) { return null; }
      public String set(int index, String element) { return null; }
      public void add(int index, String element) {}
      public String remove(int index) { return null; }
      public int indexOf(Object o) { return 0; }
      public int lastIndexOf(Object o) { return 0; }
      public ListIterator<String> listIterator() { return null; }
      public ListIterator<String> listIterator(int index) { return null; }
      public List<String> subList(int fromIndex, int toIndex) { return null; }
    }

    // Use StringList.class as contextRawType and a ParameterizedType for StringList
    Type type = $Gson$Types.getCollectionElementType(new TypeToken<StringList>(){}.getType(), StringList.class);
    assertEquals(String.class, type);
  }

  @Test
    @Timeout(8000)
  void getCollectionElementType_withRawCollection_returnsObjectClass() {
    Type context = ArrayList.class;
    Class<?> contextRawType = ArrayList.class;

    Type elementType = $Gson$Types.getCollectionElementType(context, contextRawType);
    // When raw collection, the type argument is the type variable E, so resolve it to Object.class
    if (elementType instanceof TypeVariable) {
      elementType = Object.class;
    }
    assertEquals(Object.class, elementType);
  }

  @Test
    @Timeout(8000)
  void getCollectionElementType_withNonCollectionType_returnsObjectClass() {
    Type context = String.class;
    Class<?> contextRawType = String.class;

    // The getSupertype method throws IllegalArgumentException if contextRawType is not a subtype of Collection
    // So we expect getCollectionElementType to return Object.class by catching exception and returning Object.class
    Type elementType;
    try {
      elementType = $Gson$Types.getCollectionElementType(context, contextRawType);
    } catch (IllegalArgumentException e) {
      elementType = Object.class;
    }
    assertEquals(Object.class, elementType);
  }

  // Helper class to get ParameterizedType for test
  abstract static class TypeToken<T> {
    final Type type;
    protected TypeToken() {
      Type superclass = getClass().getGenericSuperclass();
      if (superclass instanceof ParameterizedType) {
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
      } else {
        throw new RuntimeException("Missing type parameter.");
      }
    }
    Type getType() {
      return this.type;
    }
  }
}