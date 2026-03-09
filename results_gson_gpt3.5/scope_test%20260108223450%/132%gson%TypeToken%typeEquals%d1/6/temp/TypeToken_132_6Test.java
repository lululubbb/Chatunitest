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

class TypeToken_typeEquals_Test {

  private static ParameterizedType createParameterizedType(final Type rawType, final Type... typeArguments) {
    return new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return typeArguments;
      }

      @Override
      public Type getRawType() {
        return rawType;
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return rawType.equals(that.getRawType()) &&
               java.util.Arrays.equals(typeArguments, that.getActualTypeArguments()) &&
               getOwnerType() == that.getOwnerType();
      }

      @Override
      public int hashCode() {
        return rawType.hashCode() ^ java.util.Arrays.hashCode(typeArguments) ^
               (getOwnerType() == null ? 0 : getOwnerType().hashCode());
      }

      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(rawType.getTypeName());
        if (typeArguments.length > 0) {
          sb.append("<");
          for (int i = 0; i < typeArguments.length; i++) {
            if (i != 0) sb.append(", ");
            sb.append(typeArguments[i].getTypeName());
          }
          sb.append(">");
        }
        return sb.toString();
      }
    };
  }

  private static java.lang.reflect.Method getTypeEqualsMethod() throws Exception {
    java.lang.reflect.Method method = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return method;
  }

  @Test
    @Timeout(8000)
  void typeEquals_sameRawTypeAndMatchingArgs_returnsTrue() throws Exception {
    // Arrange
    Type rawType = String.class;
    Type arg1 = Integer.class;
    Type arg2 = Double.class;

    ParameterizedType from = createParameterizedType(rawType, arg1, arg2);
    ParameterizedType to = createParameterizedType(rawType, arg1, arg2);

    Map<String, Type> typeVarMap = new HashMap<>();

    // Act
    var method = getTypeEqualsMethod();
    boolean result = (boolean) method.invoke(null, from, to, typeVarMap);

    // Assert
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_sameRawTypeAndNonMatchingArgs_returnsFalse() throws Exception {
    // Arrange
    Type rawType = String.class;
    Type arg1From = Integer.class;
    Type arg2From = Double.class;
    Type arg1To = Integer.class;
    Type arg2To = Float.class; // different

    ParameterizedType from = createParameterizedType(rawType, arg1From, arg2From);
    ParameterizedType to = createParameterizedType(rawType, arg1To, arg2To);

    Map<String, Type> typeVarMap = new HashMap<>();

    // Act
    var method = getTypeEqualsMethod();
    boolean result = (boolean) method.invoke(null, from, to, typeVarMap);

    // Assert
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_differentRawType_returnsFalse() throws Exception {
    // Arrange
    Type rawTypeFrom = String.class;
    Type rawTypeTo = Integer.class; // different raw type
    Type arg1 = Integer.class;

    ParameterizedType from = createParameterizedType(rawTypeFrom, arg1);
    ParameterizedType to = createParameterizedType(rawTypeTo, arg1);

    Map<String, Type> typeVarMap = new HashMap<>();

    // Act
    var method = getTypeEqualsMethod();
    boolean result = (boolean) method.invoke(null, from, to, typeVarMap);

    // Assert
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_emptyTypeArguments_returnsTrue() throws Exception {
    // Arrange
    Type rawType = String.class;

    ParameterizedType from = createParameterizedType(rawType);
    ParameterizedType to = createParameterizedType(rawType);

    Map<String, Type> typeVarMap = new HashMap<>();

    // Act
    var method = getTypeEqualsMethod();
    boolean result = (boolean) method.invoke(null, from, to, typeVarMap);

    // Assert
    assertTrue(result);
  }
}