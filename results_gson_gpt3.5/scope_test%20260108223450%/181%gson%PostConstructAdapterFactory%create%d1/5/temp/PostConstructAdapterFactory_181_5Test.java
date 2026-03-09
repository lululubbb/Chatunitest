package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public class PostConstructAdapterFactory_181_5Test {

    private PostConstructAdapterFactory factory;
    private Gson mockGson;

    static class TestClassWithPostConstruct {
        @PostConstruct
        private void init() {
        }
    }

    static class TestClassWithoutPostConstruct {
        private void someMethod() {
        }
    }

    @BeforeEach
    public void setUp() {
        factory = new PostConstructAdapterFactory();
        mockGson = mock(Gson.class);
    }

    @Test
    @Timeout(8000)
    public void create_shouldReturnPostConstructAdapter_whenPostConstructMethodPresent() throws Exception {
        TypeToken<TestClassWithPostConstruct> typeToken = TypeToken.get(TestClassWithPostConstruct.class);

        // Prepare a real delegate adapter to be returned by gson.getDelegateAdapter
        TypeAdapter<TestClassWithPostConstruct> realDelegateAdapter = new TypeAdapter<TestClassWithPostConstruct>() {
            @Override
            public void write(com.google.gson.stream.JsonWriter out, TestClassWithPostConstruct value) {
            }

            @Override
            public TestClassWithPostConstruct read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };

        when(mockGson.getDelegateAdapter(ArgumentMatchers.eq(factory), ArgumentMatchers.eq(typeToken))).thenReturn(realDelegateAdapter);

        TypeAdapter<TestClassWithPostConstruct> adapter = factory.create(mockGson, typeToken);

        assertNotNull(adapter, "Adapter should not be null when @PostConstruct method is present");

        // Verify that the adapter is an instance of PostConstructAdapterFactory.PostConstructAdapter
        assertEquals("com.google.gson.typeadapters.PostConstructAdapterFactory$PostConstructAdapter", adapter.getClass().getName());

        Field field = null;
        Class<?> clazz = adapter.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField("postConstructMethod");
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        assertNotNull(field, "Field 'postConstructMethod' should be present");
        field.setAccessible(true);
        Method methodValue = (Method) field.get(adapter);
        assertNotNull(methodValue);
        assertEquals("init", methodValue.getName());
    }

    @Test
    @Timeout(8000)
    public void create_shouldReturnNull_whenNoPostConstructMethodPresent() {
        TypeToken<TestClassWithoutPostConstruct> typeToken = TypeToken.get(TestClassWithoutPostConstruct.class);

        TypeAdapter<TestClassWithoutPostConstruct> adapter = factory.create(mockGson, typeToken);

        assertNull(adapter, "Adapter should be null when no @PostConstruct method is present");
    }

    @Test
    @Timeout(8000)
    public void create_shouldScanSuperclassesForPostConstruct() throws Exception {
        class SuperClass {
            @PostConstruct
            private void superInit() {
            }
        }
        class SubClass extends SuperClass {
        }

        TypeToken<SubClass> typeToken = TypeToken.get(SubClass.class);

        // Prepare a real delegate adapter to be returned by gson.getDelegateAdapter
        TypeAdapter<SubClass> realDelegateAdapter = new TypeAdapter<SubClass>() {
            @Override
            public void write(com.google.gson.stream.JsonWriter out, SubClass value) {
            }

            @Override
            public SubClass read(com.google.gson.stream.JsonReader in) {
                return null;
            }
        };

        when(mockGson.getDelegateAdapter(ArgumentMatchers.eq(factory), ArgumentMatchers.eq(typeToken))).thenReturn(realDelegateAdapter);

        TypeAdapter<SubClass> adapter = factory.create(mockGson, typeToken);

        assertNotNull(adapter, "Adapter should not be null when superclass has @PostConstruct method");

        Field field = null;
        Class<?> clazz = adapter.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField("postConstructMethod");
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        assertNotNull(field, "Field 'postConstructMethod' should be present");
        field.setAccessible(true);
        Method methodValue = (Method) field.get(adapter);
        assertNotNull(methodValue);
        assertEquals("superInit", methodValue.getName());
    }

    @Test
    @Timeout(8000)
    public void create_shouldReturnNull_whenTypeIsObject() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);

        TypeAdapter<Object> adapter = factory.create(mockGson, typeToken);

        assertNull(adapter, "Adapter should be null when raw type is Object");
    }
}