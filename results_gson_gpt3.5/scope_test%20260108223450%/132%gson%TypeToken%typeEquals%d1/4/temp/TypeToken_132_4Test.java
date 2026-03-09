package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeToken_typeEqualsTest {

  // Helper to invoke private static method typeEquals via reflection
  private boolean invokeTypeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> typeVarMap) throws Exception {
    var method = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeVarMap);
  }

  // Helper to create a ParameterizedType instance for testing
  private ParameterizedType createParameterizedType(final Class<?> raw, final Type[] args) {
    return new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() { return args; }
      @Override public Type getRawType() { return raw; }
      @Override public Type getOwnerType() { return null; }
      @Override public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return raw.equals(that.getRawType()) && 
          java.util.Arrays.equals(args, that.getActualTypeArguments());
      }
      @Override public int hashCode() {
        return raw.hashCode() ^ java.util.Arrays.hashCode(args);
      }
      @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(raw.getTypeName());
        if (args != null && args.length > 0) {
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

  @Test
    @Timeout(8000)
  void typeEquals_sameRawTypeAndMatchingArguments_returnsTrue() throws Exception {
    Type[] fromArgs = new Type[] { String.class, Integer.class };
    Type[] toArgs = new Type[] { String.class, Integer.class };
    ParameterizedType from = createParameterizedType(Map.class, fromArgs);
    ParameterizedType to = createParameterizedType(Map.class, toArgs);
    Map<String, Type> typeVarMap = new HashMap<>();

    boolean result = invokeTypeEquals(from, to, typeVarMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_sameRawTypeAndNonMatchingArguments_returnsFalse() throws Exception {
    Type[] fromArgs = new Type[] { String.class, Integer.class };
    Type[] toArgs = new Type[] { String.class, Double.class };
    ParameterizedType from = createParameterizedType(Map.class, fromArgs);
    ParameterizedType to = createParameterizedType(Map.class, toArgs);
    Map<String, Type> typeVarMap = new HashMap<>();

    boolean result = invokeTypeEquals(from, to, typeVarMap);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_differentRawType_returnsFalse() throws Exception {
    Type[] fromArgs = new Type[] { String.class };
    Type[] toArgs = new Type[] { String.class };
    ParameterizedType from = createParameterizedType(Map.class, fromArgs);
    ParameterizedType to = createParameterizedType(Class.class, toArgs);
    Map<String, Type> typeVarMap = new HashMap<>();

    boolean result = invokeTypeEquals(from, to, typeVarMap);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_argumentsWithTypeVariables_matching() throws Exception {
    // fromArgs has a TypeVariable, toArgs has concrete type matching mapping
    TypeVariable<Class<Map>>[] typeParams = Map.class.getTypeParameters();
    TypeVariable<?> typeVarK = typeParams[0]; // K
    TypeVariable<?> typeVarV = typeParams[1]; // V
    
    Type[] fromArgs = new Type[] { typeVarK, typeVarV };
    Type[] toArgs = new Type[] { String.class, Integer.class };
    ParameterizedType from = createParameterizedType(Map.class, fromArgs);
    ParameterizedType to = createParameterizedType(Map.class, toArgs);
    
    Map<String, Type> typeVarMap = new HashMap<>();
    typeVarMap.put(typeVarK.getName(), String.class);
    typeVarMap.put(typeVarV.getName(), Integer.class);

    boolean result = invokeTypeEquals(from, to, typeVarMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_argumentsWithTypeVariables_notMatching() throws Exception {
    TypeVariable<Class<Map>>[] typeParams = Map.class.getTypeParameters();
    TypeVariable<?> typeVarK = typeParams[0];
    TypeVariable<?> typeVarV = typeParams[1];

    Type[] fromArgs = new Type[] { typeVarK, typeVarV };
    Type[] toArgs = new Type[] { String.class, Double.class };
    ParameterizedType from = createParameterizedType(Map.class, fromArgs);
    ParameterizedType to = createParameterizedType(Map.class, toArgs);

    Map<String, Type> typeVarMap = new HashMap<>();
    typeVarMap.put(typeVarK.getName(), String.class);
    typeVarMap.put(typeVarV.getName(), Integer.class);

    boolean result = invokeTypeEquals(from, to, typeVarMap);

    assertFalse(result);
  }
}