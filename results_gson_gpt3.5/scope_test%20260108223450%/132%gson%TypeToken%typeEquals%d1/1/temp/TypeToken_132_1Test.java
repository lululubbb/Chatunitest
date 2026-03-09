package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class TypeToken_typeEquals_Test {

  private static ParameterizedType createParameterizedType(final Class<?> raw, final Type... args) {
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return args;
      }
      @Override
      public Type getRawType() {
        return raw;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return raw.equals(that.getRawType()) &&
               java.util.Arrays.equals(args, that.getActualTypeArguments());
      }
      @Override
      public int hashCode() {
        return raw.hashCode() ^ java.util.Arrays.hashCode(args);
      }
      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(raw.getTypeName());
        if (args.length > 0) {
          sb.append("<");
          for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(args[i].getTypeName());
          }
          sb.append(">");
        }
        return sb.toString();
      }
    };
  }

  private static TypeVariable<?> createTypeVariable() throws Exception {
    // Using reflection to get a TypeVariable instance from Map class (not TypeToken)
    TypeVariable<?>[] vars = Map.class.getTypeParameters();
    if (vars.length > 0) {
      return vars[0];
    }
    throw new IllegalStateException("No type variables found on Map");
  }

  private static boolean invokeTypeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> typeVarMap) throws Exception {
    Method m = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    m.setAccessible(true);
    return (boolean) m.invoke(null, from, to, typeVarMap);
  }

  private static boolean invokeMatches(Type from, Type to, Map<String, Type> typeVarMap) throws Exception {
    Method m = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    m.setAccessible(true);
    return (boolean) m.invoke(null, from, to, typeVarMap);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_sameRawTypeAndAllArgsMatch() throws Exception {
    ParameterizedType from = createParameterizedType(Map.class, String.class, Integer.class);
    ParameterizedType to = createParameterizedType(Map.class, String.class, Integer.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_sameRawTypeAndArgsMatchWithTypeVarMap() throws Exception {
    TypeVariable<?> typeVar = createTypeVariable();
    ParameterizedType from = createParameterizedType(Map.class, typeVar, Integer.class);
    ParameterizedType to = createParameterizedType(Map.class, String.class, Integer.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    // The matches method should resolve type variable to String
    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertTrue(result);
    assertEquals(String.class, typeVarMap.get(typeVar.getName()));
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_sameRawTypeButOneArgDoesNotMatch() throws Exception {
    ParameterizedType from = createParameterizedType(Map.class, String.class, Integer.class);
    ParameterizedType to = createParameterizedType(Map.class, String.class, Double.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_differentRawTypes() throws Exception {
    ParameterizedType from = createParameterizedType(Map.class, String.class, Integer.class);
    ParameterizedType to = createParameterizedType(java.util.List.class, String.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testTypeEquals_emptyTypeVarMapUnchangedOnMismatch() throws Exception {
    ParameterizedType from = createParameterizedType(Map.class, String.class, Integer.class);
    ParameterizedType to = createParameterizedType(Map.class, String.class, Double.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    boolean result = invokeTypeEquals(from, to, typeVarMap);
    assertFalse(result);
    assertTrue(typeVarMap.isEmpty());
  }
}