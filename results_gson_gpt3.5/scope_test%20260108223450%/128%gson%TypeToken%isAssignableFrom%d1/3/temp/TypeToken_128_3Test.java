package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeTokenIsAssignableFromTest {

  private TypeToken<?> typeTokenClass;
  private TypeToken<?> typeTokenParameterized;
  private TypeToken<?> typeTokenGenericArray;

  private Class<?> rawTypeClass;
  private ParameterizedType parameterizedType;
  private GenericArrayType genericArrayType;

  @BeforeEach
  void setUp() throws Exception {
    // rawTypeClass = String.class
    rawTypeClass = String.class;
    typeTokenClass = TypeToken.get(rawTypeClass);

    // parameterizedType = List<String>
    parameterizedType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return java.util.List.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    typeTokenParameterized = TypeToken.get(parameterizedType);

    // genericArrayType = List<String>[]
    genericArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return parameterizedType;
      }
    };
    typeTokenGenericArray = TypeToken.get(genericArrayType);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullType() {
    // Explicitly call isAssignableFrom(Type) to resolve ambiguity
    assertFalse(typeTokenClass.isAssignableFrom((Type) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_sameType() {
    // Explicitly call isAssignableFrom(Type)
    assertTrue(typeTokenClass.isAssignableFrom((Type) rawTypeClass));
    assertTrue(typeTokenClass.isAssignableFrom(typeTokenClass.getType()));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_typeIsClass_assignable() {
    TypeToken<String> token = TypeToken.get(String.class);
    Type fromType = Integer.class;
    // String.class.isAssignableFrom(Integer.class) == false
    assertFalse(token.isAssignableFrom(fromType));
    // String.class.isAssignableFrom(String.class) == true
    assertTrue(token.isAssignableFrom(String.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_typeIsParameterizedType() {
    TypeToken<?> token = typeTokenParameterized;
    // from type equal to parameterizedType itself
    assertTrue(token.isAssignableFrom(parameterizedType));
    // from type raw type is assignable from parameterizedType raw type
    Type from = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return java.util.ArrayList.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    // ArrayList is assignable to List? Yes, ArrayList implements List.
    assertTrue(token.isAssignableFrom(from));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_typeIsGenericArrayType() {
    TypeToken<?> token = typeTokenGenericArray;
    // from is array of List<String>
    Type from = Array.newInstance(java.util.List.class, 0).getClass();
    assertTrue(token.isAssignableFrom(from));

    // from is not array type
    assertFalse(token.isAssignableFrom(parameterizedType));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_typeUnexpectedType_throws() {
    Type unexpectedType = new Type() {};
    TypeToken<?> tokenWithUnexpectedType;
    try {
      // Create a normal TypeToken<Object> instance
      tokenWithUnexpectedType = new TypeToken<Object>() {};
      // Use reflection to set the private final 'type' field to unexpectedType
      Field typeField = TypeToken.class.getDeclaredField("type");
      typeField.setAccessible(true);

      // Remove final modifier from the field (optional, depending on JVM)
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(typeField, typeField.getModifiers() & ~Modifier.FINAL);

      typeField.set(tokenWithUnexpectedType, unexpectedType);

      // Also update rawType field to avoid NPE or inconsistent state
      Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
      rawTypeField.setAccessible(true);
      rawTypeField.set(tokenWithUnexpectedType, Object.class);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    AssertionError thrown = assertThrows(AssertionError.class, () -> tokenWithUnexpectedType.isAssignableFrom(String.class));
    assertTrue(thrown.getMessage().contains("Unexpected type"));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_privateStaticIsAssignableFromParameterizedType() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);

    ParameterizedType to = parameterizedType;
    Map<String, Type> typeVarMap = new HashMap<>();

    // from is parameterized type equal to 'to'
    Type from = parameterizedType;
    boolean result = (boolean) method.invoke(null, from, to, typeVarMap);
    assertTrue(result);

    // from is parameterized type with different raw type
    ParameterizedType fromDiffRaw = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }

      @Override
      public Type getRawType() {
        return java.util.Map.class;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    result = (boolean) method.invoke(null, fromDiffRaw, to, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_privateStaticIsAssignableFromGenericArrayType() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    GenericArrayType to = genericArrayType;

    // from is array type with matching component type
    Type from = Array.newInstance(java.util.List.class, 0).getClass();
    boolean result = (boolean) method.invoke(null, from, to);
    assertTrue(result);

    // from is not array type
    result = (boolean) method.invoke(null, parameterizedType, to);
    assertFalse(result);
  }
}