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
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

class GsonTypesTest {

    @Test
    @Timeout(8000)
    void indexOf_foundElement_returnsCorrectIndex() throws Exception {
        Object[] array = {"a", "b", "c"};
        Object toFind = "b";

        Method indexOfMethod = getIndexOfMethod();
        int result = (int) indexOfMethod.invoke(null, (Object) array, toFind);

        assertEquals(1, result);
    }

    @Test
    @Timeout(8000)
    void indexOf_firstElement_returnsZero() throws Exception {
        Object[] array = {"x", "y", "z"};
        Object toFind = "x";

        Method indexOfMethod = getIndexOfMethod();
        int result = (int) indexOfMethod.invoke(null, (Object) array, toFind);

        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void indexOf_lastElement_returnsLastIndex() throws Exception {
        Object[] array = {"x", "y", "z"};
        Object toFind = "z";

        Method indexOfMethod = getIndexOfMethod();
        int result = (int) indexOfMethod.invoke(null, (Object) array, toFind);

        assertEquals(2, result);
    }

    @Test
    @Timeout(8000)
    void indexOf_elementNotFound_throwsNoSuchElementException() throws Exception {
        Object[] array = {"a", "b", "c"};
        Object toFind = "d";

        Method indexOfMethod = getIndexOfMethod();

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            indexOfMethod.invoke(null, (Object) array, toFind);
        });
        assertTrue(thrown.getCause() instanceof NoSuchElementException);
    }

    @Test
    @Timeout(8000)
    void indexOf_nullElement_throwsNullPointerException() throws Exception {
        Object[] array = {"a", null, "c"};
        Object toFind = null;

        Method indexOfMethod = getIndexOfMethod();

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            indexOfMethod.invoke(null, (Object) array, toFind);
        });
        assertTrue(thrown.getCause() instanceof NullPointerException);
    }

    private Method getIndexOfMethod() throws NoSuchMethodException, ClassNotFoundException {
        Class<?> gsonTypesClass = Class.forName("com.google.gson.internal.$Gson$Types");
        Method method = gsonTypesClass.getDeclaredMethod("indexOf", Object[].class, Object.class);
        method.setAccessible(true);
        return method;
    }
}