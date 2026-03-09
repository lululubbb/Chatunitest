package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

class PostConstructAdapterFactory_181_2Test {

    private PostConstructAdapterFactory factory;
    private Gson mockGson;

    @BeforeEach
    void setUp() {
        factory = new PostConstructAdapterFactory();
        mockGson = mock(Gson.class);
    }

    static class ClassWithPostConstruct {
        boolean postConstructCalled = false;

        @PostConstruct
        private void init() {
            postConstructCalled = true;
        }
    }

    static class ClassWithoutPostConstruct {
        private void someMethod() {
        }
    }

    @Test
    @Timeout(8000)
    void create_shouldReturnPostConstructAdapter_whenPostConstructMethodPresent() throws Exception {
        TypeToken<ClassWithPostConstruct> typeToken = TypeToken.get(ClassWithPostConstruct.class);

        // Create a real delegate adapter to avoid null fields
        @SuppressWarnings("unchecked")
        TypeAdapter<ClassWithPostConstruct> delegateAdapter = (TypeAdapter<ClassWithPostConstruct>) mock(TypeAdapter.class);
        when(mockGson.getDelegateAdapter(ArgumentMatchers.eq(factory), ArgumentMatchers.eq(typeToken)))
                .thenReturn(delegateAdapter);

        TypeAdapter<ClassWithPostConstruct> adapter = factory.create(mockGson, typeToken);

        assertNotNull(adapter);
        assertEquals(PostConstructAdapterFactory.PostConstructAdapter.class, adapter.getClass());

        // Verify that the PostConstruct method is set in the adapter via reflection
        Field postConstructMethodField = null;
        Class<?> clazz = adapter.getClass();
        while (clazz != null) {
            try {
                postConstructMethodField = clazz.getDeclaredField("postConstruct");
                break;
            } catch (NoSuchFieldException e) {
                try {
                    postConstructMethodField = clazz.getDeclaredField("postConstructMethod");
                    break;
                } catch (NoSuchFieldException ex) {
                    clazz = clazz.getSuperclass();
                }
            }
        }
        assertNotNull(postConstructMethodField, "Field 'postConstructMethod' or 'postConstruct' not found in adapter class hierarchy");
        postConstructMethodField.setAccessible(true);
        Method postConstructMethod = (Method) postConstructMethodField.get(adapter);

        assertNotNull(postConstructMethod);
        assertTrue(postConstructMethod.isAnnotationPresent(PostConstruct.class));
        assertEquals("init", postConstructMethod.getName());
    }

    @Test
    @Timeout(8000)
    void create_shouldReturnNull_whenNoPostConstructMethodPresent() {
        TypeToken<ClassWithoutPostConstruct> typeToken = TypeToken.get(ClassWithoutPostConstruct.class);

        TypeAdapter<ClassWithoutPostConstruct> adapter = factory.create(mockGson, typeToken);

        assertNull(adapter);
        verifyNoInteractions(mockGson);
    }

    @Test
    @Timeout(8000)
    void create_shouldCheckSuperclassesForPostConstructMethod() throws Exception {
        class SuperClass {
            @PostConstruct
            private void superInit() {
            }
        }
        class SubClass extends SuperClass {
        }

        TypeToken<SubClass> typeToken = TypeToken.get(SubClass.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<SubClass> delegateAdapter = (TypeAdapter<SubClass>) mock(TypeAdapter.class);
        when(mockGson.getDelegateAdapter(ArgumentMatchers.eq(factory), ArgumentMatchers.eq(typeToken)))
                .thenReturn(delegateAdapter);

        TypeAdapter<SubClass> adapter = factory.create(mockGson, typeToken);

        assertNotNull(adapter);
        assertEquals(PostConstructAdapterFactory.PostConstructAdapter.class, adapter.getClass());

        // Verify that the PostConstruct method is set in the adapter via reflection
        Field postConstructMethodField = null;
        Class<?> clazz = adapter.getClass();
        while (clazz != null) {
            try {
                postConstructMethodField = clazz.getDeclaredField("postConstruct");
                break;
            } catch (NoSuchFieldException e) {
                try {
                    postConstructMethodField = clazz.getDeclaredField("postConstructMethod");
                    break;
                } catch (NoSuchFieldException ex) {
                    clazz = clazz.getSuperclass();
                }
            }
        }
        assertNotNull(postConstructMethodField, "Field 'postConstructMethod' or 'postConstruct' not found in adapter class hierarchy");
        postConstructMethodField.setAccessible(true);
        Method postConstructMethod = (Method) postConstructMethodField.get(adapter);

        assertNotNull(postConstructMethod);
        assertTrue(postConstructMethod.isAnnotationPresent(PostConstruct.class));
        assertEquals("superInit", postConstructMethod.getName());
    }

    @Test
    @Timeout(8000)
    void create_shouldReturnNull_whenTypeIsObject() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);

        TypeAdapter<Object> adapter = factory.create(mockGson, typeToken);

        assertNull(adapter);
        verifyNoInteractions(mockGson);
    }
}