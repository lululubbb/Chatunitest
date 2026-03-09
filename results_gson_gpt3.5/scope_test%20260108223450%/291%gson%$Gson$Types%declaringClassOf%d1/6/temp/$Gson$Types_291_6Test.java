package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;

import org.junit.jupiter.api.Test;

class GsonTypesDeclaringClassOfTest {

  @Test
    @Timeout(8000)
  void declaringClassOf_genericDeclarationIsClass_returnsClass() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    TypeVariable<?> typeVariable = mock(TypeVariable.class);
    Class<?> declaringClass = String.class;
    GenericDeclaration genericDeclaration = declaringClass;
    when(typeVariable.getGenericDeclaration()).thenReturn(genericDeclaration);

    // Use reflection to access private method
    var method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);

    // Act
    Object result = method.invoke(null, typeVariable);

    // Assert
    assertSame(declaringClass, result);
  }

  @Test
    @Timeout(8000)
  void declaringClassOf_genericDeclarationIsNotClass_returnsNull() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    TypeVariable<?> typeVariable = mock(TypeVariable.class);
    GenericDeclaration genericDeclaration = mock(GenericDeclaration.class);
    when(typeVariable.getGenericDeclaration()).thenReturn(genericDeclaration);

    // Use reflection to access private method
    var method = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
    method.setAccessible(true);

    // Act
    Object result = method.invoke(null, typeVariable);

    // Assert
    assertNull(result);
  }
}