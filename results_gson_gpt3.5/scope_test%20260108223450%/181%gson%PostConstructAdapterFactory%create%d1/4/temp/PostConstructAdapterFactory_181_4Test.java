package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

public class PostConstructAdapterFactory_181_4Test {

    private PostConstructAdapterFactory factory;
    private Gson mockGson;

    @BeforeEach
    public void setUp() {
        factory = new PostConstructAdapterFactory();
        mockGson = mock(Gson.class);
    }

    static class TestClassWithPostConstruct {
        boolean postConstructCalled = false;

        @PostConstruct
        private void init() {
            postConstructCalled = true;
        }
    }

    static class TestClassWithoutPostConstruct {
        // no @PostConstruct methods
    }

    @Test
    @Timeout(8000)
    public void create_WithPostConstructMethod_ReturnsPostConstructAdapter() throws Exception {
        TypeToken<TestClassWithPostConstruct> typeToken = TypeToken.get(TestClassWithPostConstruct.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<TestClassWithPostConstruct> delegateAdapter = mock(TypeAdapter.class);
        when(mockGson.getDelegateAdapter(ArgumentMatchers.any(), ArgumentMatchers.eq(typeToken))).thenReturn(delegateAdapter);

        TypeAdapter<TestClassWithPostConstruct> adapter = factory.create(mockGson, typeToken);

        assertNotNull(adapter);
        assertEquals("PostConstructAdapter", adapter.getClass().getSimpleName());

        // Use reflection to get the private field 'postConstructMethod' from PostConstructAdapter
        Method postConstructMethod = TestClassWithPostConstruct.class.getDeclaredMethod("init");
        postConstructMethod.setAccessible(true);

        // Verify that the PostConstruct annotation is present on the method
        assertTrue(postConstructMethod.isAnnotationPresent(PostConstruct.class));
    }

    @Test
    @Timeout(8000)
    public void create_WithoutPostConstructMethod_ReturnsNull() {
        TypeToken<TestClassWithoutPostConstruct> typeToken = TypeToken.get(TestClassWithoutPostConstruct.class);

        TypeAdapter<TestClassWithoutPostConstruct> adapter = factory.create(mockGson, typeToken);

        assertNull(adapter);
        verify(mockGson, never()).getDelegateAdapter(any(), any());
    }

    @Test
    @Timeout(8000)
    public void create_InheritedPostConstructMethod_ReturnsPostConstructAdapter() {
        class SuperClass {
            @PostConstruct
            private void init() {}
        }
        class SubClass extends SuperClass {}

        TypeToken<SubClass> typeToken = TypeToken.get(SubClass.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<SubClass> delegateAdapter = mock(TypeAdapter.class);
        when(mockGson.getDelegateAdapter(ArgumentMatchers.any(), ArgumentMatchers.eq(typeToken))).thenReturn(delegateAdapter);

        TypeAdapter<SubClass> adapter = factory.create(mockGson, typeToken);

        assertNotNull(adapter);
        assertEquals("PostConstructAdapter", adapter.getClass().getSimpleName());
    }

    @Test
    @Timeout(8000)
    public void create_PostConstructMethodInSuperclassWithObjectSuperclass_ReturnsPostConstructAdapter() {
        class Base {
            @PostConstruct
            private void baseInit() {}
        }
        class Derived extends Base {}

        TypeToken<Derived> typeToken = TypeToken.get(Derived.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<Derived> delegateAdapter = mock(TypeAdapter.class);
        when(mockGson.getDelegateAdapter(ArgumentMatchers.any(), ArgumentMatchers.eq(typeToken))).thenReturn(delegateAdapter);

        TypeAdapter<Derived> adapter = factory.create(mockGson, typeToken);

        assertNotNull(adapter);
        assertEquals("PostConstructAdapter", adapter.getClass().getSimpleName());
    }
}