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

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class GsonTypesResolveTest {

    @Test
    @Timeout(8000)
    void testResolve_nullToResolve_returnsToResolve() throws Exception {
        Type context = String.class;
        Class<?> contextRawType = String.class;
        Type toResolve = null;

        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Type result = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, new HashMap<TypeVariable<?>, Type>());

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testResolve_TypeVariable_resolvesCorrectly() throws Exception {
        class GenericClass<T> {}

        // Create a ParameterizedType representing GenericClass<String>
        ParameterizedType parameterizedContext = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }

            @Override
            public Type getRawType() {
                return GenericClass.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        TypeVariable<?> typeVariable = GenericClass.class.getTypeParameters()[0];

        Type context = parameterizedContext;
        Class<?> contextRawType = GenericClass.class;
        Type toResolve = typeVariable;

        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        Map<TypeVariable<?>, Type> visited = new HashMap<>();
        Type result = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);

        // Because mapping exists, it should resolve to String.class
        assertEquals(String.class, result);
    }

    @Test
    @Timeout(8000)
    void testResolve_ParameterizedType_resolvesTypeArguments() throws Exception {
        class GenericClass<T> {}

        TypeVariable<?> tVar = GenericClass.class.getTypeParameters()[0];

        ParameterizedType parameterizedType = new ParameterizedType() {

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { tVar };
            }

            @Override
            public Type getRawType() {
                return Map.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Type context = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class, Integer.class };
            }

            @Override
            public Type getRawType() {
                return Map.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        Class<?> contextRawType = Map.class;
        Type toResolve = parameterizedType;

        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        Type result = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, new HashMap<TypeVariable<?>, Type>());

        assertNotNull(result);
        assertTrue(result instanceof ParameterizedType);
        ParameterizedType resolved = (ParameterizedType) result;
        assertEquals(Map.class, resolved.getRawType());
        assertEquals(2, resolved.getActualTypeArguments().length);
        assertEquals(String.class, resolved.getActualTypeArguments()[0]);
        assertEquals(Integer.class, resolved.getActualTypeArguments()[1]);
    }

    @Test
    @Timeout(8000)
    void testResolve_GenericArrayType_resolvesComponentType() throws Exception {
        GenericArrayType genericArrayType = new GenericArrayType() {

            @Override
            public Type getGenericComponentType() {
                return String.class;
            }
        };

        Type context = String[].class;
        Class<?> contextRawType = String[].class;
        Type toResolve = genericArrayType;

        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        Type result = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, new HashMap<TypeVariable<?>, Type>());

        assertNotNull(result);
        assertTrue(result instanceof GenericArrayType || result instanceof Class<?>);
    }

    @Test
    @Timeout(8000)
    void testResolve_Class_returnsClass() throws Exception {
        Type context = String.class;
        Class<?> contextRawType = String.class;
        Type toResolve = Integer.class;

        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        Type result = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, new HashMap<TypeVariable<?>, Type>());

        assertEquals(Integer.class, result);
    }

    @Test
    @Timeout(8000)
    void testResolve_WildcardType_resolvesBounds() throws Exception {
        WildcardType wildcardType = new WildcardType() {

            @Override
            public Type[] getUpperBounds() {
                return new Type[] { Number.class };
            }

            @Override
            public Type[] getLowerBounds() {
                return new Type[] {};
            }
        };

        Type context = Number.class;
        Class<?> contextRawType = Number.class;
        Type toResolve = wildcardType;

        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        Type result = (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, new HashMap<TypeVariable<?>, Type>());

        assertNotNull(result);
        assertTrue(result instanceof WildcardType);
    }
}