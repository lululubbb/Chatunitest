package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeToken_typeEqualsTest {

  private ParameterizedType fromType;
  private ParameterizedType toTypeEqual;
  private ParameterizedType toTypeNotEqualRaw;
  private ParameterizedType toTypeNotEqualArgs;

  private Map<String, Type> typeVarMap;

  @BeforeEach
  void setup() throws Exception {
    // Create ParameterizedType instances for testing

    // fromType: Map<String, Integer>
    fromType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return this.getRawType().equals(that.getRawType()) &&
            java.util.Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments()) &&
            java.util.Objects.equals(this.getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Objects.hash(getRawType(), java.util.Arrays.hashCode(getActualTypeArguments()), getOwnerType());
      }
    };

    // toTypeEqual: Map<String, Integer>
    toTypeEqual = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return this.getRawType().equals(that.getRawType()) &&
            java.util.Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments()) &&
            java.util.Objects.equals(this.getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Objects.hash(getRawType(), java.util.Arrays.hashCode(getActualTypeArguments()), getOwnerType());
      }
    };

    // toTypeNotEqualRaw: HashMap<String, Integer> (different raw type)
    toTypeNotEqualRaw = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }
      @Override
      public Type getRawType() {
        return java.util.HashMap.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return this.getRawType().equals(that.getRawType()) &&
            java.util.Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments()) &&
            java.util.Objects.equals(this.getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Objects.hash(getRawType(), java.util.Arrays.hashCode(getActualTypeArguments()), getOwnerType());
      }
    };

    // toTypeNotEqualArgs: Map<String, String> (same raw type, different args)
    toTypeNotEqualArgs = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, String.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return this.getRawType().equals(that.getRawType()) &&
            java.util.Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments()) &&
            java.util.Objects.equals(this.getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Objects.hash(getRawType(), java.util.Arrays.hashCode(getActualTypeArguments()), getOwnerType());
      }
    };

    typeVarMap = new HashMap<>();
  }

  @Test
    @Timeout(8000)
  void typeEquals_returnsTrue_whenRawTypeAndArgsMatch() throws Exception {
    boolean result = invokeTypeEquals(fromType, toTypeEqual, typeVarMap);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_returnsFalse_whenRawTypeDiffers() throws Exception {
    boolean result = invokeTypeEquals(fromType, toTypeNotEqualRaw, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_returnsFalse_whenArgsDiffer() throws Exception {
    boolean result = invokeTypeEquals(fromType, toTypeNotEqualArgs, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void typeEquals_returnsTrue_whenArgsContainTypeVariablesMatching() throws Exception {
    // fromType with TypeVariable argument
    ParameterizedType fromWithTypeVar = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {new DummyTypeVariable("K"), Integer.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return this.getRawType().equals(that.getRawType()) &&
            java.util.Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments()) &&
            java.util.Objects.equals(this.getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Objects.hash(getRawType(), java.util.Arrays.hashCode(getActualTypeArguments()), getOwnerType());
      }
    };

    // toType with concrete types matching typeVarMap
    ParameterizedType toWithConcrete = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return this.getRawType().equals(that.getRawType()) &&
            java.util.Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments()) &&
            java.util.Objects.equals(this.getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Objects.hash(getRawType(), java.util.Arrays.hashCode(getActualTypeArguments()), getOwnerType());
      }
    };

    Map<String, Type> typeVarMap = new HashMap<>();
    typeVarMap.put("K", String.class);

    boolean result = invokeTypeEquals(fromWithTypeVar, toWithConcrete, typeVarMap);
    assertTrue(result);
  }

  // Helper method to invoke private static typeEquals via reflection
  private boolean invokeTypeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> map) throws Exception {
    java.lang.reflect.Method method = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, map);
  }

  // Dummy TypeVariable implementation for testing
  private static class DummyTypeVariable implements TypeVariable<Class<DummyTypeVariable>> {

    private final String name;

    DummyTypeVariable(String name) {
      this.name = name;
    }

    @Override
    public Type[] getBounds() {
      return new Type[] {Object.class};
    }

    @Override
    public Class<DummyTypeVariable> getGenericDeclaration() {
      return DummyTypeVariable.class;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public AnnotatedType[] getAnnotatedBounds() {
      return new AnnotatedType[0];
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
      return null;
    }

    @Override
    public Annotation[] getAnnotations() {
      return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
      return new Annotation[0];
    }

    @Override
    public String toString() {
      return name;
    }
  }
}