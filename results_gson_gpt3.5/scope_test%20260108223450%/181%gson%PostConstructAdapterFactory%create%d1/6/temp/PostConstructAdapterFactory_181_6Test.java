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
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

class PostConstructAdapterFactory_181_6Test {

    private PostConstructAdapterFactory factory;
    private Gson gson;

    @BeforeEach
    void setUp() {
        factory = new PostConstructAdapterFactory();
        gson = mock(Gson.class);
    }

    static class TestClassWithPostConstruct {
        boolean postConstructCalled = false;

        @PostConstruct
        private void init() {
            postConstructCalled = true;
        }
    }

    static class TestClassWithoutPostConstruct {
        // no PostConstruct method
    }

    @Test
    @Timeout(8000)
    void create_returnsPostConstructAdapter_whenPostConstructMethodPresent() throws NoSuchMethodException {
        TypeToken<TestClassWithPostConstruct> typeToken = TypeToken.get(TestClassWithPostConstruct.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<TestClassWithPostConstruct> delegateAdapter = mock(TypeAdapter.class);
        when(gson.getDelegateAdapter(factory, typeToken)).thenReturn(delegateAdapter);

        TypeAdapter<TestClassWithPostConstruct> adapter = factory.create(gson, typeToken);

        assertNotNull(adapter);

        Method initMethod = TestClassWithPostConstruct.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);

        // Use reflection to get the class of PostConstructAdapter by creating one via reflection
        Class<?> postConstructAdapterClass = null;
        try {
            // Find PostConstructAdapter class inside PostConstructAdapterFactory
            Class<?>[] declaredClasses = PostConstructAdapterFactory.class.getDeclaredClasses();
            for (Class<?> c : declaredClasses) {
                if ("PostConstructAdapter".equals(c.getSimpleName())) {
                    postConstructAdapterClass = c;
                    break;
                }
            }
            assertNotNull(postConstructAdapterClass);

            // Create an instance of PostConstructAdapter via reflection
            var constructor = postConstructAdapterClass.getDeclaredConstructor(TypeAdapter.class, Method.class);
            constructor.setAccessible(true);
            Object expectedAdapter = constructor.newInstance(delegateAdapter, initMethod);

            assertEquals(expectedAdapter.getClass(), adapter.getClass());
        } catch (Exception e) {
            fail("Failed to create PostConstructAdapter instance via reflection: " + e.getMessage());
        }

        // Use reflection to get the private method 'init' from TestClassWithPostConstruct
        try {
            TestClassWithPostConstruct instance = new TestClassWithPostConstruct();
            assertFalse(instance.postConstructCalled);
            initMethod.invoke(instance);
            assertTrue(instance.postConstructCalled);
        } catch (Exception e) {
            fail("Reflection invocation of PostConstruct method failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void create_returnsNull_whenNoPostConstructMethodPresent() {
        TypeToken<TestClassWithoutPostConstruct> typeToken = TypeToken.get(TestClassWithoutPostConstruct.class);

        TypeAdapter<TestClassWithoutPostConstruct> adapter = factory.create(gson, typeToken);

        assertNull(adapter);
        verify(gson, never()).getDelegateAdapter(any(), any());
    }

    @Test
    @Timeout(8000)
    void create_findsPostConstructMethodInSuperclass() throws NoSuchMethodException {
        class SuperClass {
            @PostConstruct
            private void superInit() {
            }
        }
        class SubClass extends SuperClass {
        }
        TypeToken<SubClass> typeToken = TypeToken.get(SubClass.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<SubClass> delegateAdapter = mock(TypeAdapter.class);
        when(gson.getDelegateAdapter(factory, typeToken)).thenReturn(delegateAdapter);

        TypeAdapter<SubClass> adapter = factory.create(gson, typeToken);

        assertNotNull(adapter);

        Method superInitMethod = SuperClass.class.getDeclaredMethod("superInit");
        superInitMethod.setAccessible(true);

        // Find PostConstructAdapter class inside PostConstructAdapterFactory
        Class<?> postConstructAdapterClass = null;
        try {
            Class<?>[] declaredClasses = PostConstructAdapterFactory.class.getDeclaredClasses();
            for (Class<?> c : declaredClasses) {
                if ("PostConstructAdapter".equals(c.getSimpleName())) {
                    postConstructAdapterClass = c;
                    break;
                }
            }
            assertNotNull(postConstructAdapterClass);

            // Create an instance of PostConstructAdapter via reflection
            var constructor = postConstructAdapterClass.getDeclaredConstructor(TypeAdapter.class, Method.class);
            constructor.setAccessible(true);
            Object expectedAdapter = constructor.newInstance(delegateAdapter, superInitMethod);

            assertEquals(expectedAdapter.getClass(), adapter.getClass());
        } catch (Exception e) {
            fail("Failed to create PostConstructAdapter instance via reflection: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void create_ignoresMethodsWithoutPostConstructAnnotation() {
        class ClassWithNoAnnotation {
            private void someMethod() {
            }
        }
        TypeToken<ClassWithNoAnnotation> typeToken = TypeToken.get(ClassWithNoAnnotation.class);

        TypeAdapter<ClassWithNoAnnotation> adapter = factory.create(gson, typeToken);

        assertNull(adapter);
        verify(gson, never()).getDelegateAdapter(any(), any());
    }
}