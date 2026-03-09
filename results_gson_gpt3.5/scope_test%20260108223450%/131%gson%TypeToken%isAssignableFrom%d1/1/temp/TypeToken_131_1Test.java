package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeToken_isAssignableFrom_Test {

  // Use reflection to access private static method isAssignableFrom(Type, ParameterizedType, Map)
  private static boolean invokeIsAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap)
      throws Exception {
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeVarMap);
  }

  // Helper to create ParameterizedType instances for testing
  private static ParameterizedType parameterizedType(final Class<?> raw, final Type... args) {
    return new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return args;
      }
      @Override public Type getRawType() {
        return raw;
      }
      @Override public Type getOwnerType() {
        return null;
      }
      @Override public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return raw.equals(that.getRawType()) && java.util.Arrays.equals(args, that.getActualTypeArguments());
      }
      @Override public int hashCode() {
        return raw.hashCode() ^ java.util.Arrays.hashCode(args);
      }
      @Override public String toString() {
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

  // Dummy generic classes for testing
  static class GenericBase<T> {}
  static class GenericDerived extends GenericBase<String> {}
  static class GenericDerivedWithVar<T> extends GenericBase<T> {}
  static class RawClass {}

  interface MyInterface<T> {}
  static class Impl implements MyInterface<String> {}

  static class Super<T> {}
  static class Sub extends Super<String> {}

  static class Loop<T extends Number> {}

  @Test
    @Timeout(8000)
  void testFromNullReturnsFalse() throws Exception {
    ParameterizedType to = parameterizedType(GenericBase.class, String.class);
    boolean result = invokeIsAssignableFrom(null, to, new HashMap<>());
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testFromEqualsToReturnsTrue() throws Exception {
    ParameterizedType to = parameterizedType(GenericBase.class, String.class);
    ParameterizedType from = parameterizedType(GenericBase.class, String.class);
    boolean result = invokeIsAssignableFrom(from, to, new HashMap<>());
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testFromParameterizedAssignsTypeVarsAndReturnsTrue() throws Exception {
    ParameterizedType to = parameterizedType(GenericBase.class, String.class);

    // Instead of creating ParameterizedType for GenericDerived (which has no type params),
    // pass the Class GenericDerived.class to represent raw type
    Class<?> from = GenericDerived.class;

    Map<String, Type> map = new HashMap<>();
    boolean result = invokeIsAssignableFrom(from, to, map);
    // Because GenericDerived extends GenericBase<String>, it should be assignable
    assertTrue(result);
    // The map should contain T -> String from GenericBase<T>
    assertEquals(String.class, map.get("T"));
  }

  @Test
    @Timeout(8000)
  void testFromParameterizedWithTypeVariableMapping() throws Exception {
    ParameterizedType to = parameterizedType(GenericBase.class, String.class);
    ParameterizedType from = parameterizedType(GenericDerivedWithVar.class, String.class);
    Map<String, Type> map = new HashMap<>();
    boolean result = invokeIsAssignableFrom(from, to, map);
    assertTrue(result);
    assertEquals(String.class, map.get("T"));
  }

  @Test
    @Timeout(8000)
  void testFromRawClassNotAssignable() throws Exception {
    ParameterizedType to = parameterizedType(GenericBase.class, String.class);
    Class<?> from = RawClass.class;
    Map<String, Type> map = new HashMap<>();
    boolean result = invokeIsAssignableFrom(from, to, map);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testFromClassWithInterfaceAssignable() throws Exception {
    ParameterizedType to = parameterizedType(MyInterface.class, String.class);
    Class<?> from = Impl.class;
    Map<String, Type> map = new HashMap<>();
    boolean result = invokeIsAssignableFrom(from, to, map);
    assertTrue(result);
    assertEquals(String.class, map.get("T"));
  }

  @Test
    @Timeout(8000)
  void testFromClassSuperclassAssignable() throws Exception {
    ParameterizedType to = parameterizedType(Super.class, String.class);
    Class<?> from = Sub.class;
    Map<String, Type> map = new HashMap<>();
    boolean result = invokeIsAssignableFrom(from, to, map);
    assertTrue(result);
    assertEquals(String.class, map.get("T"));
  }

  @Test
    @Timeout(8000)
  void testTypeVariableLoopResolution() throws Exception {
    ParameterizedType to = parameterizedType(Loop.class, Number.class);
    ParameterizedType from = parameterizedType(Loop.class, Number.class);
    Map<String, Type> map = new HashMap<>();
    boolean result = invokeIsAssignableFrom(from, to, map);
    assertTrue(result);
  }
}