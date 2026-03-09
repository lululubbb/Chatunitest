package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class $Gson$Types_292_3Test {

    private static Method checkNotPrimitiveMethod;

    @BeforeAll
    static void setUp() throws Exception {
        checkNotPrimitiveMethod = $Gson$Types.class.getDeclaredMethod("checkNotPrimitive", Type.class);
        checkNotPrimitiveMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testCheckNotPrimitive_withNonPrimitiveClass() throws Exception {
        // Using String.class which is not primitive
        assertDoesNotThrow(() -> checkNotPrimitiveMethod.invoke(null, String.class));
    }

    @Test
    @Timeout(8000)
    void testCheckNotPrimitive_withPrimitiveClass() throws Exception {
        // Using int.class which is primitive
        Throwable thrown = assertThrows(Exception.class, () -> checkNotPrimitiveMethod.invoke(null, int.class));
        // InvocationTargetException wraps the actual exception, unwrap it
        Throwable cause = thrown.getCause();
        // checkArgument throws IllegalArgumentException on failure
        org.junit.jupiter.api.Assertions.assertTrue(cause instanceof IllegalArgumentException);
    }

    @Test
    @Timeout(8000)
    void testCheckNotPrimitive_withNonClassType() throws Exception {
        // Using an instance of Type that is not Class, e.g. an anonymous Type implementation
        Type anonymousType = new Type() {};
        assertDoesNotThrow(() -> checkNotPrimitiveMethod.invoke(null, anonymousType));
    }

    @Test
    @Timeout(8000)
    void testCheckNotPrimitive_withNullType() throws Exception {
        // The method does not explicitly check for null, but checkArgument likely requires non-null
        // Passing null should NOT throw exception because checkArgument only checks primitive condition
        // and !(null instanceof Class<?>) is true, so no exception is thrown.
        assertDoesNotThrow(() -> checkNotPrimitiveMethod.invoke(null, new Object[] { null }));
    }
}