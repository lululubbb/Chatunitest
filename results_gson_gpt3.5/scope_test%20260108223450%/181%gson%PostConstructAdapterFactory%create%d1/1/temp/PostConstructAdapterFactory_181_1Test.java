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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

class PostConstructAdapterFactory_181_1Test {

    private PostConstructAdapterFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PostConstructAdapterFactory();
    }

    static class TestClassWithPostConstruct {
        @PostConstruct
        private void init() {
        }
    }

    static class TestClassWithoutPostConstruct {
        private void init() {
        }
    }

    @Test
    @Timeout(8000)
    void create_returnsPostConstructAdapter_whenPostConstructMethodPresent() throws Exception {
        Gson gson = mock(Gson.class);
        TypeToken<TestClassWithPostConstruct> typeToken = TypeToken.get(TestClassWithPostConstruct.class);

        Method postConstructMethod = TestClassWithPostConstruct.class.getDeclaredMethod("init");
        postConstructMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        TypeAdapter<TestClassWithPostConstruct> delegateAdapter = mock(TypeAdapter.class);

        when(gson.getDelegateAdapter(factory, typeToken)).thenReturn(delegateAdapter);

        TypeAdapter<TestClassWithPostConstruct> adapter = factory.create(gson, typeToken);

        assertNotNull(adapter);
        assertEquals("com.google.gson.typeadapters.PostConstructAdapterFactory$PostConstructAdapter", adapter.getClass().getName());

        // Use reflection to verify the PostConstruct method is correctly set in adapter
        Method methodInAdapter = getPostConstructMethod(adapter);
        assertEquals(postConstructMethod, methodInAdapter);
    }

    @Test
    @Timeout(8000)
    void create_returnsNull_whenNoPostConstructMethodPresent() {
        Gson gson = mock(Gson.class);
        TypeToken<TestClassWithoutPostConstruct> typeToken = TypeToken.get(TestClassWithoutPostConstruct.class);

        TypeAdapter<TestClassWithoutPostConstruct> adapter = factory.create(gson, typeToken);

        assertNull(adapter);
    }

    @Test
    @Timeout(8000)
    void create_findsPostConstructInSuperclass() throws Exception {
        class Parent {
            @PostConstruct
            private void parentInit() {
            }
        }
        class Child extends Parent {
        }

        Gson gson = mock(Gson.class);
        TypeToken<Child> typeToken = TypeToken.get(Child.class);

        Method postConstructMethod = Parent.class.getDeclaredMethod("parentInit");
        postConstructMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        TypeAdapter<Child> delegateAdapter = mock(TypeAdapter.class);

        when(gson.getDelegateAdapter(factory, typeToken)).thenReturn(delegateAdapter);

        TypeAdapter<Child> adapter = factory.create(gson, typeToken);

        assertNotNull(adapter);
        assertEquals("com.google.gson.typeadapters.PostConstructAdapterFactory$PostConstructAdapter", adapter.getClass().getName());

        Method methodInAdapter = getPostConstructMethod(adapter);
        assertEquals(postConstructMethod, methodInAdapter);
    }

    @Test
    @Timeout(8000)
    void create_returnsNull_whenTypeIsObject() {
        Gson gson = mock(Gson.class);
        TypeToken<Object> typeToken = TypeToken.get(Object.class);

        TypeAdapter<Object> adapter = factory.create(gson, typeToken);

        assertNull(adapter);
    }

    private Method getPostConstructMethod(Object adapter) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = adapter.getClass();
        while (clazz != null) {
            try {
                Field delegateField = clazz.getDeclaredField("delegate");
                delegateField.setAccessible(true);
                Object delegate = delegateField.get(adapter);

                Field postConstructField = clazz.getDeclaredField("postConstruct");
                postConstructField.setAccessible(true);
                Object value = postConstructField.get(adapter);
                if (value instanceof Method) {
                    return (Method) value;
                }
                throw new IllegalStateException("Field 'postConstruct' is not a Method");
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field 'postConstruct' not found in adapter class hierarchy");
    }
}