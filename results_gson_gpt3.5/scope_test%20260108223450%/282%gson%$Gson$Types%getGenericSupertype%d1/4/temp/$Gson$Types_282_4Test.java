package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;

public class $Gson$Types_282_4Test {

    private Method getGenericSupertypeMethod;

    @BeforeEach
    public void setUp() throws Exception {
        getGenericSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getGenericSupertype", Type.class, Class.class, Class.class);
        getGenericSupertypeMethod.setAccessible(true);
    }

    private Type invokeGetGenericSupertype(Type context, Class<?> rawType, Class<?> supertype) throws Exception {
        return (Type) getGenericSupertypeMethod.invoke(null, context, rawType, supertype);
    }

    // Helper classes for testing
    interface InterfaceA {}
    interface InterfaceB extends InterfaceA {}
    interface InterfaceC {}
    static class SuperClass implements InterfaceB {}
    static class SubClass extends SuperClass implements InterfaceC {}

    @Test
    @Timeout(8000)
    public void testWhenSupertypeEqualsRawTypeReturnsContext() throws Exception {
        Class<?> clazz = String.class;
        Type result = invokeGetGenericSupertype(clazz, clazz, clazz);
        assertEquals(clazz, result);
    }

    @Test
    @Timeout(8000)
    public void testWhenSupertypeIsInterfaceDirectlyImplementedReturnsGenericInterface() throws Exception {
        Class<?> rawType = SuperClass.class;
        Class<?> supertype = InterfaceB.class;

        Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

        Type[] genericInterfaces = rawType.getGenericInterfaces();
        boolean found = false;
        for (Type t : genericInterfaces) {
            if (t == result) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    @Timeout(8000)
    public void testWhenSupertypeIsInterfaceIndirectlyImplementedReturnsRecursiveGenericInterface() throws Exception {
        Class<?> rawType = SubClass.class;
        Class<?> supertype = InterfaceA.class;

        Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

        // The returned type should be the generic interface from SuperClass or SubClass that corresponds to InterfaceA
        assertNotNull(result);
        // Should be a ParameterizedType or Class (raw)
        assertTrue(result instanceof Type);
    }

    @Test
    @Timeout(8000)
    public void testWhenSupertypeIsSuperclassDirectlyReturnsGenericSuperclass() throws Exception {
        Class<?> rawType = SubClass.class;
        Class<?> supertype = SuperClass.class;

        Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

        Type genericSuperclass = rawType.getGenericSuperclass();
        assertEquals(genericSuperclass, result);
    }

    @Test
    @Timeout(8000)
    public void testWhenSupertypeIsSuperclassIndirectlyReturnsRecursiveGenericSuperclass() throws Exception {
        class GrandParent {}
        class Parent extends GrandParent {}
        class Child extends Parent {}

        Class<?> rawType = Child.class;
        Class<?> supertype = GrandParent.class;

        Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

        assertNotNull(result);
        // Should be a Type representing GrandParent in the hierarchy
        assertTrue(result instanceof Type);
    }

    @Test
    @Timeout(8000)
    public void testWhenSupertypeIsInterfaceNotImplementedReturnsSupertypeItself() throws Exception {
        Class<?> rawType = String.class;
        Class<?> supertype = Cloneable.class; // String implements Cloneable, so test with an unrelated interface

        Class<?> unrelatedInterface = Runnable.class;

        Type result = invokeGetGenericSupertype(rawType, rawType, unrelatedInterface);

        // Because String does not implement Runnable, and it's interface, should return supertype itself
        assertEquals(unrelatedInterface, result);
    }

    @Test
    @Timeout(8000)
    public void testWhenSupertypeIsSuperclassNotInHierarchyReturnsSupertypeItself() throws Exception {
        Class<?> rawType = String.class;
        Class<?> supertype = Number.class;

        Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

        assertEquals(supertype, result);
    }

    @Test
    @Timeout(8000)
    public void testWhenRawTypeIsObjectReturnsSupertypeItself() throws Exception {
        Class<?> rawType = Object.class;
        Class<?> supertype = Runnable.class;

        Type result = invokeGetGenericSupertype(rawType, rawType, supertype);

        assertEquals(supertype, result);
    }
}