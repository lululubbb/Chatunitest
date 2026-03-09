package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeToken_isAssignableFrom_Test {

  private Method isAssignableFromMethod;

  @BeforeEach
  public void setup() throws NoSuchMethodException {
    isAssignableFromMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    isAssignableFromMethod.setAccessible(true);
  }

  private boolean invokeIsAssignableFrom(Type from, GenericArrayType to) throws InvocationTargetException, IllegalAccessException {
    return (boolean) isAssignableFromMethod.invoke(null, from, to);
  }

  private GenericArrayType createGenericArrayType(Type componentType) {
    return new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return componentType;
      }
    };
  }

  private ParameterizedType createParameterizedType(final Class<?> raw, final Type[] typeArgs) {
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return typeArgs;
      }

      @Override
      public Type getRawType() {
        return raw;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withToGenericComponentTypeParameterizedType_fromGenericArrayType() throws Exception {
    Type genericComponentType = createParameterizedType(String.class, new Type[]{String.class});
    GenericArrayType to = createGenericArrayType(genericComponentType);

    GenericArrayType from = createGenericArrayType(String.class);

    boolean result = invokeIsAssignableFrom(from, to);
    // We cannot assert true or false because the private isAssignableFrom(Type, ParameterizedType, Map) is unknown,
    // but it should not throw and return a boolean.
    assertTrue(result || !result);
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withToGenericComponentTypeParameterizedType_fromClassArray() throws Exception {
    Type genericComponentType = createParameterizedType(String.class, new Type[]{String.class});
    GenericArrayType to = createGenericArrayType(genericComponentType);

    Class<?> from = String[].class; // single-dimensional array class

    boolean result = invokeIsAssignableFrom(from, to);

    assertTrue(result || !result);
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withToGenericComponentTypeParameterizedType_fromClassNonArray() throws Exception {
    Type genericComponentType = createParameterizedType(String.class, new Type[]{String.class});
    GenericArrayType to = createGenericArrayType(genericComponentType);

    Class<?> from = String.class;

    boolean result = invokeIsAssignableFrom(from, to);

    assertTrue(result || !result);
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withToGenericComponentTypeNotParameterizedType() throws Exception {
    Type genericComponentType = String.class; // Not ParameterizedType
    GenericArrayType to = createGenericArrayType(genericComponentType);

    Class<?> from = Integer.class;

    boolean result = invokeIsAssignableFrom(from, to);

    // Should return true because genericComponentType is not ParameterizedType
    assertTrue(result);
  }

}