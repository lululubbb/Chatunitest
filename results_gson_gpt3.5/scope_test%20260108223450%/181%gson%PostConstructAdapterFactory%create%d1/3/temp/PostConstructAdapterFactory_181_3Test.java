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

class PostConstructAdapterFactory_181_3Test {

    PostConstructAdapterFactory factory;
    Gson gson;

    @BeforeEach
    void setUp() {
        factory = new PostConstructAdapterFactory();
        gson = mock(Gson.class);
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
        TypeToken<TestClassWithPostConstruct> typeToken = TypeToken.get(TestClassWithPostConstruct.class);

        @SuppressWarnings("unchecked")
        TypeAdapter<TestClassWithPostConstruct> delegateAdapter = mock(TypeAdapter.class);
        when(gson.getDelegateAdapter(eq(factory), eq(typeToken))).thenReturn(delegateAdapter);

        TypeAdapter<TestClassWithPostConstruct> adapter = factory.create(gson, typeToken);

        assertNotNull(adapter);
        assertEquals(PostConstructAdapterFactory.PostConstructAdapter.class, adapter.getClass());

        // use reflection to verify the PostConstruct method is set inside PostConstructAdapter
        Field[] fields = adapter.getClass().getDeclaredFields();
        Field postConstructMethodField = null;
        for (Field f : fields) {
            if (f.getType() == Method.class) {
                postConstructMethodField = f;
                break;
            }
        }
        assertNotNull(postConstructMethodField, "postConstructMethod field not found");
        postConstructMethodField.setAccessible(true);
        Method postConstructMethod = (Method) postConstructMethodField.get(adapter);
        assertEquals("init", postConstructMethod.getName());
    }

    @Test
    @Timeout(8000)
    void create_returnsNull_whenNoPostConstructMethodPresent() {
        TypeToken<TestClassWithoutPostConstruct> typeToken = TypeToken.get(TestClassWithoutPostConstruct.class);

        TypeAdapter<TestClassWithoutPostConstruct> adapter = factory.create(gson, typeToken);

        assertNull(adapter);
    }

    @Test
    @Timeout(8000)
    void create_findsPostConstructMethodInSuperclass() throws Exception {
        class SuperClass {
            @PostConstruct
            private void postConstructMethod() {
            }
        }
        class SubClass extends SuperClass {
        }

        TypeToken<SubClass> typeToken = TypeToken.get(SubClass.class);

        @SuppressWarnings("unchecked")
        TypeAdapter<SubClass> delegateAdapter = mock(TypeAdapter.class);
        when(gson.getDelegateAdapter(eq(factory), eq(typeToken))).thenReturn(delegateAdapter);

        TypeAdapter<SubClass> adapter = factory.create(gson, typeToken);

        assertNotNull(adapter);
        assertEquals(PostConstructAdapterFactory.PostConstructAdapter.class, adapter.getClass());
    }
}