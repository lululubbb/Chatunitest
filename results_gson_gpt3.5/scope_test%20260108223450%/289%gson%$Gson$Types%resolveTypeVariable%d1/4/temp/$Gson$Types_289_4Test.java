package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

class GsonTypesResolveTypeVariableTest {

    private Method resolveTypeVariableMethod;
    private Method declaringClassOfMethod;
    private Method getGenericSupertypeMethod;
    private Method indexOfMethod;

    @BeforeEach
    void setUp() throws Exception {
        // Access private static methods via reflection
        resolveTypeVariableMethod = $Gson$Types.class.getDeclaredMethod("resolveTypeVariable", Type.class, Class.class, TypeVariable.class);
        resolveTypeVariableMethod.setAccessible(true);

        declaringClassOfMethod = $Gson$Types.class.getDeclaredMethod("declaringClassOf", TypeVariable.class);
        declaringClassOfMethod.setAccessible(true);

        getGenericSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
        getGenericSupertypeMethod.setAccessible(true);

        indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
        indexOfMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testResolveTypeVariable_declaringClassNull_returnsUnknown() throws Throwable {
        // Create a dummy TypeVariable with no declaring class by using a dynamic proxy
        TypeVariable<?> dummyTypeVariable = createDummyTypeVariable();

        // Invoke declaringClassOf directly to confirm behavior.
        Class<?> declaringClass = (Class<?>) declaringClassOfMethod.invoke(null, dummyTypeVariable);
        assertNull(declaringClass, "declaringClassOf should return null for dummy TypeVariable");

        // Call resolveTypeVariable with dummyTypeVariable
        Type result = (Type) resolveTypeVariableMethod.invoke(null, Object.class, Object.class, dummyTypeVariable);

        // Because declaringClassOf returns null, result should be the same dummyTypeVariable
        assertSame(dummyTypeVariable, result);
    }

    @Test
    @Timeout(8000)
    void testResolveTypeVariable_declaredByRawIsNotNull_declaredByParameterizedType_returnsActualTypeArgument() throws Throwable {
        // Use SampleParameterizedClass as context and contextRawType
        Type context = SampleParameterizedClass.class.getGenericSuperclass();
        Class<?> contextRawType = SampleParameterizedClass.class;

        TypeVariable<?> unknown = SampleGenericSuperClass.class.getTypeParameters()[0]; // T

        // invoke resolveTypeVariable
        Type result = (Type) resolveTypeVariableMethod.invoke(null, context, contextRawType, unknown);

        // The actual type argument for T in SampleParameterizedClass<String> is String.class
        assertEquals(String.class, result);
    }

    @Test
    @Timeout(8000)
    void testResolveTypeVariable_declaredByRawIsNotNull_declaredByNotParameterizedType_returnsUnknown() throws Throwable {
        // Use context and contextRawType where getGenericSupertype returns Class, not ParameterizedType

        Type context = SampleClass.class;
        Class<?> contextRawType = SampleClass.class;

        TypeVariable<?> unknown = SampleClass.class.getTypeParameters()[0];

        // invoke resolveTypeVariable
        Type result = (Type) resolveTypeVariableMethod.invoke(null, context, contextRawType, unknown);

        // Because getGenericSupertype returns Class (not ParameterizedType), returns unknown
        assertSame(unknown, result);
    }

    // Helper method to create a dummy TypeVariable with no declaring class
    private static TypeVariable<?> createDummyTypeVariable() {
        InvocationHandler handler = (proxy, method, args) -> {
            switch (method.getName()) {
                case "getName":
                    return "DUMMY";
                case "getBounds":
                    return new Type[] { Object.class };
                case "getGenericDeclaration":
                    return null; // simulate no declaring class
                case "toString":
                    return "DUMMY";
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                default:
                    throw new UnsupportedOperationException("Unsupported method: " + method.getName());
            }
        };

        // Cast Proxy to TypeVariable<?> explicitly
        return (TypeVariable<?>) Proxy.newProxyInstance(
                TypeVariable.class.getClassLoader(),
                new Class<?>[] { TypeVariable.class },
                handler);
    }

    // Helper classes for testing

    static class SampleClass<T> {
    }

    static class SampleGenericSuperClass<T> {
    }

    static class SampleParameterizedClass extends SampleGenericSuperClass<String> {
    }
}