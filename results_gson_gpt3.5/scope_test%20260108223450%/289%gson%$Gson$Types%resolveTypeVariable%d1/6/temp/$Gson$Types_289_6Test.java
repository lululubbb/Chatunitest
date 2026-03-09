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
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonTypesResolveTypeVariableTest {

  private Class<?> contextRawType;
  private TypeVariable<?> unknown;
  private Type context;

  // Helper to get a TypeVariable from a class by name
  private static TypeVariable<?> getTypeVariable(Class<?> clazz, String name) {
    for (TypeVariable<?> tv : clazz.getTypeParameters()) {
      if (tv.getName().equals(name)) {
        return tv;
      }
    }
    return null;
  }

  // Helper to invoke private static method resolveTypeVariable via reflection
  private static Type invokeResolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) throws Exception {
    Method method = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
    method.setAccessible(true);
    return (Type) method.invoke(null, context, contextRawType, unknown);
  }

  // A generic superclass to test with parameterized type
  static class GenericSuperClass<T> {}

  // A subclass specifying the generic type parameter
  static class SubClass extends GenericSuperClass<String> {}

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaringClassIsNull_returnsUnknown() throws Exception {
    // Create a TypeVariable with no declaring class (anonymous TypeVariable mock)
    TypeVariable<?> tv = mock(TypeVariable.class);
    when(tv.getGenericDeclaration()).thenReturn(mock(GenericDeclaration.class));
    // declaringClassOf returns null if genericDeclaration is not a Class, so this simulates that

    Type resolved = invokeResolveTypeVariable(String.class, String.class, tv);
    assertSame(tv, resolved);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawParameterizedType_returnsActualTypeArgument() throws Exception {
    // contextRawType is SubClass.class
    contextRawType = SubClass.class;
    // context is SubClass.class (raw type)
    context = SubClass.class;
    // unknown is T from GenericSuperClass<T>
    unknown = getTypeVariable(GenericSuperClass.class, "T");
    assertNotNull(unknown);

    Type resolved = invokeResolveTypeVariable(context, contextRawType, unknown);
    // Should resolve to String.class because SubClass extends GenericSuperClass<String>
    assertEquals(String.class, resolved);
  }

  @Test
    @Timeout(8000)
  void resolveTypeVariable_declaredByRawNotParameterizedType_returnsUnknown() throws Exception {
    // contextRawType is GenericSuperClass.class (no parameterization)
    contextRawType = GenericSuperClass.class;
    context = GenericSuperClass.class;
    unknown = getTypeVariable(GenericSuperClass.class, "T");
    assertNotNull(unknown);

    // We expect it to return unknown because getGenericSupertype returns raw type (not ParameterizedType)
    Type resolved = invokeResolveTypeVariable(context, contextRawType, unknown);
    assertSame(unknown, resolved);
  }
}