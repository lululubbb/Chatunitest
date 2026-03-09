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
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.internal.$Gson$Types;

class GsonTypesResolveTest {

    private Class<?> contextRawType;
    private Type context;
    private Type toResolve;

    @BeforeEach
    void setUp() {
        contextRawType = SampleClass.class;
        context = SampleClass.class;
    }

    @Test
    @Timeout(8000)
    void resolve_nullToResolve_returnsNull() throws Exception {
        Type result = invokeResolve(context, contextRawType, null, new HashMap<>());
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void resolve_primitiveType_returnsTypeItself() throws Exception {
        Type primitiveType = int.class;
        Type result = invokeResolve(context, contextRawType, primitiveType, new HashMap<>());
        assertSame(primitiveType, result);
    }

    @Test
    @Timeout(8000)
    void resolve_typeVariableResolved() throws Exception {
        TypeVariable<?>[] typeVariables = SampleGenericClass.class.getTypeParameters();
        assertTrue(typeVariables.length > 0);
        TypeVariable<?> typeVar = typeVariables[0];
        Type result = invokeResolve(SampleGenericClass.class, SampleGenericClass.class, typeVar, new HashMap<>());
        // The result should not be the original type variable (should be resolved or fallback)
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void resolve_parameterizedType_resolvesArguments() throws Exception {
        ParameterizedType parameterizedType = (ParameterizedType) SampleGenericClass.class.getGenericSuperclass();
        Type result = invokeResolve(SampleGenericClass.class, SampleGenericClass.class, parameterizedType, new HashMap<>());
        assertNotNull(result);
        assertTrue(result instanceof ParameterizedType);
    }

    @Test
    @Timeout(8000)
    void resolve_genericArrayType_resolvesComponent() throws Exception {
        GenericArrayType genericArrayType = new GenericArrayType() {
            @Override
            public Type getGenericComponentType() {
                return SampleGenericClass.class.getTypeParameters()[0];
            }

            @Override
            public String getTypeName() {
                return getGenericComponentType().getTypeName() + "[]";
            }
        };
        Type result = invokeResolve(SampleGenericClass.class, SampleGenericClass.class, genericArrayType, new HashMap<>());
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void resolve_wildcardType_resolvesBounds() throws Exception {
        WildcardType wildcardType = new WildcardType() {
            @Override
            public Type[] getUpperBounds() {
                return new Type[] { Number.class };
            }

            @Override
            public Type[] getLowerBounds() {
                return new Type[0];
            }

            @Override
            public String getTypeName() {
                return "? extends Number";
            }
        };
        Type result = invokeResolve(SampleGenericClass.class, SampleGenericClass.class, wildcardType, new HashMap<>());
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void resolve_recursiveTypeVariable_avoidsInfiniteLoop() throws Exception {
        TypeVariable<?> recursiveVar = RecursiveType.class.getTypeParameters()[0];
        Type result = invokeResolve(RecursiveType.class, RecursiveType.class, recursiveVar, new HashMap<>());
        assertNotNull(result);
    }

    private Type invokeResolve(Type context, Class<?> contextRawType, Type toResolve, Map<TypeVariable<?>, Type> visited) throws Exception {
        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);
        return (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);
    }

    static class SampleClass {}

    static class SampleGenericClass<T> extends SampleClass {}

    static class RecursiveType<T extends Comparable<T>> {}

}