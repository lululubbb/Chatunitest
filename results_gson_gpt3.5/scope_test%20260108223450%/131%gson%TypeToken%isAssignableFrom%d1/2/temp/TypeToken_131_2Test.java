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

import java.lang.reflect.*;
import java.util.*;

import org.junit.jupiter.api.Test;

class TypeTokenIsAssignableFromTest {

  // Helper to invoke private static method isAssignableFrom(Type, ParameterizedType, Map)
  private boolean invokeIsAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap) throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeVarMap);
  }

  // Helper to create a ParameterizedType instance for testing
  private ParameterizedType newParameterizedType(final Class<?> raw, final Type... args) {
    return new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() { return args; }
      @Override public Type getRawType() { return raw; }
      @Override public Type getOwnerType() { return null; }
      @Override public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return Objects.equals(raw, that.getRawType()) &&
               Arrays.equals(args, that.getActualTypeArguments());
      }
      @Override public int hashCode() {
        return Arrays.hashCode(args) ^ Objects.hashCode(raw);
      }
      @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(raw.getTypeName());
        if (args.length > 0) {
          sb.append("<");
          for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            if (args[i] instanceof Class<?>) {
              sb.append(((Class<?>) args[i]).getTypeName());
            } else {
              sb.append(args[i].getTypeName());
            }
          }
          sb.append(">");
        }
        return sb.toString();
      }
    };
  }

  @Test
    @Timeout(8000)
  void testFromNullReturnsFalse() throws Exception {
    ParameterizedType to = newParameterizedType(List.class, String.class);
    Map<String, Type> map = new HashMap<>();
    assertFalse(invokeIsAssignableFrom(null, to, map));
  }

  @Test
    @Timeout(8000)
  void testFromEqualsToReturnsTrue() throws Exception {
    ParameterizedType to = newParameterizedType(List.class, String.class);
    Map<String, Type> map = new HashMap<>();
    assertTrue(invokeIsAssignableFrom(to, to, map));
  }

  @Test
    @Timeout(8000)
  void testFromParameterizedTypeMatchesTypeEqualsTrue() throws Exception {
    ParameterizedType to = newParameterizedType(List.class, String.class);
    ParameterizedType from = newParameterizedType(ArrayList.class, String.class);
    Map<String, Type> map = new HashMap<>();
    assertTrue(invokeIsAssignableFrom(from, to, map));
  }

  @Test
    @Timeout(8000)
  void testFromParameterizedTypeWithTypeVariableMapping() throws Exception {
    Class<?> clazz = Map.class;
    TypeVariable<?>[] typeParams = clazz.getTypeParameters();
    TypeVariable<?> K = typeParams[0];
    TypeVariable<?> V = typeParams[1];

    ParameterizedType to = newParameterizedType(Map.class, String.class, Integer.class);
    ParameterizedType from = newParameterizedType(Map.class, K, V);

    Map<String, Type> map = new HashMap<>();
    map.put("K", String.class);
    map.put("V", Integer.class);

    assertTrue(invokeIsAssignableFrom(from, to, map));
  }

  @Test
    @Timeout(8000)
  void testFromClassWithInterfaces() throws Exception {
    ParameterizedType to = newParameterizedType(List.class, String.class);
    Map<String, Type> map = new HashMap<>();
    Type from = ArrayList.class;
    assertTrue(invokeIsAssignableFrom(from, to, map));
  }

  @Test
    @Timeout(8000)
  void testFromClassWithSuperclass() throws Exception {
    ParameterizedType to = newParameterizedType(AbstractList.class, String.class);
    Map<String, Type> map = new HashMap<>();
    Type from = ArrayList.class;
    assertTrue(invokeIsAssignableFrom(from, to, map));
  }

  @Test
    @Timeout(8000)
  void testFromClassNoMatchReturnsFalse() throws Exception {
    ParameterizedType to = newParameterizedType(Set.class, String.class);
    Map<String, Type> map = new HashMap<>();
    Type from = ArrayList.class;
    assertFalse(invokeIsAssignableFrom(from, to, map));
  }

  @Test
    @Timeout(8000)
  void testTypeVariableResolutionInLoop() throws Exception {
    Class<?> clazz = Map.class;
    TypeVariable<?>[] typeParams = clazz.getTypeParameters();
    TypeVariable<?> K = typeParams[0];

    ParameterizedType to = newParameterizedType(Map.class, String.class, Integer.class);
    ParameterizedType from = newParameterizedType(Map.class, K, Integer.class);

    Map<String, Type> map = new HashMap<>();
    map.put("K", K);

    assertFalse(invokeIsAssignableFrom(from, to, map));
  }
}