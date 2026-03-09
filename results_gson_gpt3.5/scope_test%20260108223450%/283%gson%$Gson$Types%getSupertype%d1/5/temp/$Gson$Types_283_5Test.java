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
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class $Gson$Types_283_5Test {

    private Method getSupertypeMethod;
    private Method getGenericSupertypeMethod;
    private Method resolveMethod;

    @BeforeEach
    void setUp() throws Exception {
        getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
        getSupertypeMethod.setAccessible(true);

        getGenericSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
        getGenericSupertypeMethod.setAccessible(true);

        resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class);
        resolveMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testGetSupertype_withWildcardType_usesUpperBound() throws Throwable {
        WildcardType wildcardType = mock(WildcardType.class);
        Type upperBound = String.class;
        when(wildcardType.getUpperBounds()).thenReturn(new Type[]{upperBound});

        Class<?> contextRawType = String.class;
        Class<?> supertype = Object.class;

        try (MockedStatic<$Gson$Types> mockedStatic = Mockito.mockStatic($Gson$Types.class)) {
            // Mock getGenericSupertype via reflection call
            mockedStatic.when(() -> $Gson$Types.getGenericSupertype(upperBound, contextRawType, supertype))
                    .thenReturn(Object.class);

            // Mock resolve via reflection call
            mockedStatic.when(() -> $Gson$Types.resolve(upperBound, contextRawType, Object.class))
                    .thenReturn(Object.class);

            // call method under test
            Object result = getSupertypeMethod.invoke(null, wildcardType, contextRawType, supertype);

            assertEquals(Object.class, result);

            // Verify calls to static methods
            mockedStatic.verify(() -> $Gson$Types.getGenericSupertype(upperBound, contextRawType, supertype));
            mockedStatic.verify(() -> $Gson$Types.resolve(upperBound, contextRawType, Object.class));
        }
    }

    @Test
    @Timeout(8000)
    void testGetSupertype_withNonWildcardType_success() throws Throwable {
        Type context = String.class;
        Class<?> contextRawType = String.class;
        Class<?> supertype = Object.class;

        try (MockedStatic<$Gson$Types> mockedStatic = Mockito.mockStatic($Gson$Types.class)) {
            mockedStatic.when(() -> $Gson$Types.getGenericSupertype(context, contextRawType, supertype))
                    .thenReturn(Object.class);

            mockedStatic.when(() -> $Gson$Types.resolve(context, contextRawType, Object.class))
                    .thenReturn(Object.class);

            Object result = getSupertypeMethod.invoke(null, context, contextRawType, supertype);

            assertEquals(Object.class, result);

            mockedStatic.verify(() -> $Gson$Types.getGenericSupertype(context, contextRawType, supertype));
            mockedStatic.verify(() -> $Gson$Types.resolve(context, contextRawType, Object.class));
        }
    }

    @Test
    @Timeout(8000)
    void testGetSupertype_checkArgumentFails_throwsIllegalArgumentException() throws Throwable {
        Type context = String.class;
        Class<?> contextRawType = String.class;
        Class<?> supertype = Integer.class; // Integer is not assignable from String

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            try {
                getSupertypeMethod.invoke(null, context, contextRawType, supertype);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // unwrap cause
                throw e.getCause();
            }
        });
    }
}