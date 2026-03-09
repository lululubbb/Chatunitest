package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.ReflectionAccessFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReflectiveTypeAdapterFactory_74_4Test {

    private ReflectiveTypeAdapterFactory factory;
    private Excluder excluderMock;
    private FieldNamingStrategy fieldNamingStrategyMock;
    private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactoryMock;
    private List<ReflectionAccessFilter> reflectionFilters;

    private Field testField;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        excluderMock = mock(Excluder.class);
        fieldNamingStrategyMock = mock(FieldNamingStrategy.class);
        jsonAdapterFactoryMock = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
        reflectionFilters = Collections.emptyList();

        factory = new ReflectiveTypeAdapterFactory(constructorConstructorMock(),
                fieldNamingStrategyMock,
                excluderMock,
                jsonAdapterFactoryMock,
                reflectionFilters);

        testField = SampleClass.class.getDeclaredField("field");
    }

    private ConstructorConstructor constructorConstructorMock() {
        return mock(ConstructorConstructor.class);
    }

    private boolean invokeIncludeField(Field f, boolean serialize) throws Exception {
        var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("includeField", Field.class, boolean.class);
        method.setAccessible(true);
        return (boolean) method.invoke(factory, f, serialize);
    }

    @Test
    @Timeout(8000)
    void includeField_returnsTrue_whenExcluderReturnsFalseForBoth() throws Exception {
        when(excluderMock.excludeClass(testField.getType(), true)).thenReturn(false);
        when(excluderMock.excludeField(testField, true)).thenReturn(false);

        boolean result = invokeIncludeField(testField, true);

        assertTrue(result);
        verify(excluderMock).excludeClass(testField.getType(), true);
        verify(excluderMock).excludeField(testField, true);
    }

    @Test
    @Timeout(8000)
    void includeField_returnsFalse_whenExcluderExcludeClassIsTrue() throws Exception {
        when(excluderMock.excludeClass(testField.getType(), false)).thenReturn(true);

        boolean result = invokeIncludeField(testField, false);

        assertFalse(result);
        verify(excluderMock).excludeClass(testField.getType(), false);
        verify(excluderMock, never()).excludeField(any(), anyBoolean());
    }

    @Test
    @Timeout(8000)
    void includeField_returnsFalse_whenExcluderExcludeFieldIsTrue() throws Exception {
        when(excluderMock.excludeClass(testField.getType(), true)).thenReturn(false);
        when(excluderMock.excludeField(testField, true)).thenReturn(true);

        boolean result = invokeIncludeField(testField, true);

        assertFalse(result);
        verify(excluderMock).excludeClass(testField.getType(), true);
        verify(excluderMock).excludeField(testField, true);
    }

    static class SampleClass {
        public int field;
    }
}