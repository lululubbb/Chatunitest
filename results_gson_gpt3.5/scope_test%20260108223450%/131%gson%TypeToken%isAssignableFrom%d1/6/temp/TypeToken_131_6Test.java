package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeToken_isAssignableFrom_Test {

  private TypeToken<?> typeToken;

  @BeforeEach
  void setup() {
    typeToken = new TypeToken<String>() {};
  }

  /**
   * Helper to invoke private static isAssignableFrom(Type, ParameterizedType, Map) via reflection.
   */
  private boolean invokeIsAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap)
      throws Exception {
    java.lang.reflect.Method method = TypeToken.class.getDeclaredMethod(
        "isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeVarMap);
  }

  /**
   * Helper to create a ParameterizedType instance.
   */
  private ParameterizedType parameterizedType(final Class<?> raw, final Type... args) {
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
        return raw.equals(that.getRawType()) &&
            java.util.Arrays.equals(args, that.getActualTypeArguments());
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
            if (i > 0) sb.append(",");
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
  void testFromNull_returnsFalse() throws Exception {
    Map<String, Type> typeVarMap = new HashMap<>();
    ParameterizedType to = parameterizedType(Map.class, String.class, Integer.class);
    assertFalse(invokeIsAssignableFrom(null, to, typeVarMap));
  }

  @Test
    @Timeout(8000)
  void testFromEqualsTo_returnsTrue() throws Exception {
    Map<String, Type> typeVarMap = new HashMap<>();
    ParameterizedType to = parameterizedType(Map.class, String.class, Integer.class);
    // from equals to exactly
    assertTrue(invokeIsAssignableFrom(to, to, typeVarMap));
  }

  @Test
    @Timeout(8000)
  void testParameterizedTypeMappingAndTypeEqualsTrue() throws Exception {
    // from: Map<String, Integer>
    ParameterizedType from = parameterizedType(Map.class, String.class, Integer.class);
    // to: Map<K,V>
    TypeVariable<Class<Map>>[] params = Map.class.getTypeParameters();
    ParameterizedType to = parameterizedType(Map.class, params[0], params[1]);
    Map<String, Type> typeVarMap = new HashMap<>();
    // Should map K->String, V->Integer and typeEquals returns true
    assertTrue(invokeIsAssignableFrom(from, to, typeVarMap));
    // typeVarMap should contain correct mappings
    assertEquals(String.class, typeVarMap.get("K"));
    assertEquals(Integer.class, typeVarMap.get("V"));
  }

  @Test
    @Timeout(8000)
  void testParameterizedTypeMappingWithTypeVariableInFrom() throws Exception {
    // from: class with type variable T extends Number
    class GenericNumber<T extends Number> {}
    TypeVariable<?>[] tparams = GenericNumber.class.getTypeParameters();
    TypeVariable<?> tVar = tparams[0];
    // from: GenericNumber<T>
    ParameterizedType from = parameterizedType(GenericNumber.class, tVar);
    // to: GenericNumber<Number>
    ParameterizedType to = parameterizedType(GenericNumber.class, Number.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    // Provide mapping for T->Number
    typeVarMap.put(tVar.getName(), Number.class);
    // Should return true because after mapping T->Number, types match
    assertTrue(invokeIsAssignableFrom(from, to, typeVarMap));
  }

  @Test
    @Timeout(8000)
  void testRecursiveInterfaceCheckReturnsTrue() throws Exception {
    // from: ArrayList<String>
    ParameterizedType from = parameterizedType(java.util.ArrayList.class, String.class);
    // to: List<String>
    ParameterizedType to = parameterizedType(java.util.List.class, String.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    // ArrayList implements List, so should return true
    assertTrue(invokeIsAssignableFrom(from, to, typeVarMap));
  }

  @Test
    @Timeout(8000)
  void testSuperclassCheckReturnsTrue() throws Exception {
    // from: Integer
    Type from = Integer.class;
    // to: Number (as ParameterizedType with no type arguments)
    ParameterizedType to = parameterizedType(Number.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    // Integer extends Number, so should return true via superclass check
    assertTrue(invokeIsAssignableFrom(from, to, typeVarMap));
  }

  @Test
    @Timeout(8000)
  void testSuperclassCheckReturnsFalse() throws Exception {
    // from: String
    Type from = String.class;
    // to: Number (as ParameterizedType with no type args)
    ParameterizedType to = parameterizedType(Number.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    // String does not extend Number, should return false
    assertFalse(invokeIsAssignableFrom(from, to, typeVarMap));
  }

  @Test
    @Timeout(8000)
  void testTypeVariableLoopMapping() throws Exception {
    // from: class with T extends Comparable<T>
    class ComparableClass<T extends Comparable<T>> {}
    TypeVariable<?>[] tparams = ComparableClass.class.getTypeParameters();
    TypeVariable<?> tVar = tparams[0];
    ParameterizedType from = parameterizedType(ComparableClass.class, tVar);
    ParameterizedType to = parameterizedType(ComparableClass.class, tVar);
    Map<String, Type> typeVarMap = new HashMap<>();
    // Should handle loop in type variable mapping without infinite loop
    assertTrue(invokeIsAssignableFrom(from, to, typeVarMap));
  }

  @Test
    @Timeout(8000)
  void testTypeEqualsFalseLeadsToInterfaceCheck() throws Exception {
    // from: HashMap<String,String>
    ParameterizedType from = parameterizedType(java.util.HashMap.class, String.class, String.class);
    // to: Map<String,Object>
    ParameterizedType to = parameterizedType(java.util.Map.class, String.class, Object.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    // Because value types differ (String vs Object), typeEquals returns false, but interface check should still succeed
    assertTrue(invokeIsAssignableFrom(from, to, typeVarMap));
  }
}