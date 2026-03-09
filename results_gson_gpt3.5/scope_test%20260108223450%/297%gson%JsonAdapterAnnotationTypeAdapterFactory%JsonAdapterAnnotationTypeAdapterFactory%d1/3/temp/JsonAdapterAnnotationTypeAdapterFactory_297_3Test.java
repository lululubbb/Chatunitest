package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class JsonAdapterAnnotationTypeAdapterFactory_297_3Test {

    @Mock
    private ConstructorConstructor constructorConstructor;

    @Mock
    private Gson gson;

    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    }

    @Test
    @Timeout(8000)
    public void testConstructorStoresConstructorConstructor() {
        assertNotNull(factory);
    }

    @Test
    @Timeout(8000)
    public void testGetTypeAdapter_withJsonAdapterAnnotationReturningTypeAdapter() throws Exception {
        // Prepare a dummy JsonAdapter annotation with a TypeAdapter class
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }

            @Override
            public Class<?> value() {
                return DummyTypeAdapter.class;
            }

            @Override
            public boolean nullSafe() {
                return true;
            }
        };

        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Mock ConstructorConstructor to return an instance of DummyTypeAdapter when requested
        when(constructorConstructor.get(TypeToken.get(DummyTypeAdapter.class))).thenReturn(() -> new DummyTypeAdapter());

        // Use reflection to invoke private getTypeAdapter method
        Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
                "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        method.setAccessible(true);

        TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, typeToken, annotation);

        assertNotNull(adapter);
        // The returned adapter may be wrapped in a nullSafe adapter, so check underlying delegate
        if (adapter.getClass() != DummyTypeAdapter.class) {
            // Try to get delegate field if exists (nullSafe wrapping)
            try {
                java.lang.reflect.Field delegateField = adapter.getClass().getDeclaredField("delegate");
                delegateField.setAccessible(true);
                Object delegate = delegateField.get(adapter);
                assertTrue(delegate instanceof DummyTypeAdapter);
            } catch (NoSuchFieldException e) {
                // Try "delegateAdapter" field as alternative (Gson uses delegateAdapter in some wrappers)
                try {
                    java.lang.reflect.Field delegateAdapterField = adapter.getClass().getDeclaredField("delegateAdapter");
                    delegateAdapterField.setAccessible(true);
                    Object delegate = delegateAdapterField.get(adapter);
                    assertTrue(delegate instanceof DummyTypeAdapter);
                } catch (NoSuchFieldException ex) {
                    // Try "typeAdapter" field as alternative (some Gson wrappers use this)
                    try {
                        java.lang.reflect.Field typeAdapterField = adapter.getClass().getDeclaredField("typeAdapter");
                        typeAdapterField.setAccessible(true);
                        Object delegate = typeAdapterField.get(adapter);
                        assertTrue(delegate instanceof DummyTypeAdapter);
                    } catch (NoSuchFieldException exc) {
                        fail("Returned adapter is not DummyTypeAdapter and no delegate field found");
                    }
                }
            }
        } else {
            assertTrue(adapter instanceof DummyTypeAdapter);
        }
    }

    @Test
    @Timeout(8000)
    public void testGetTypeAdapter_withJsonAdapterAnnotationReturningJsonSerializer() throws Exception {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }

            @Override
            public Class<?> value() {
                return DummyJsonSerializer.class;
            }

            @Override
            public boolean nullSafe() {
                return false;
            }
        };

        TypeToken<String> typeToken = TypeToken.get(String.class);

        when(constructorConstructor.get(TypeToken.get(DummyJsonSerializer.class))).thenReturn(() -> new DummyJsonSerializer());

        Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
                "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        method.setAccessible(true);

        TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, typeToken, annotation);

        assertNotNull(adapter);
        // The returned adapter should NOT be the JsonSerializer class itself
        assertNotSame(DummyJsonSerializer.class, adapter.getClass());
    }

    @Test
    @Timeout(8000)
    public void testGetTypeAdapter_withJsonAdapterAnnotationReturningJsonDeserializer() throws Exception {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }

            @Override
            public Class<?> value() {
                return DummyJsonDeserializer.class;
            }

            @Override
            public boolean nullSafe() {
                return false;
            }
        };

        TypeToken<String> typeToken = TypeToken.get(String.class);

        when(constructorConstructor.get(TypeToken.get(DummyJsonDeserializer.class))).thenReturn(() -> new DummyJsonDeserializer());

        Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
                "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        method.setAccessible(true);

        TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, typeToken, annotation);

        assertNotNull(adapter);
        // The returned adapter should NOT be the JsonDeserializer class itself
        assertNotSame(DummyJsonDeserializer.class, adapter.getClass());
    }

    @Test
    @Timeout(8000)
    public void testGetTypeAdapter_withJsonAdapterAnnotationReturningInvalidClass() throws Exception {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }

            @Override
            public Class<?> value() {
                return String.class; // invalid adapter class
            }

            @Override
            public boolean nullSafe() {
                return false;
            }
        };

        TypeToken<String> typeToken = TypeToken.get(String.class);

        // No constructorConstructor.get() mocking because it should throw

        Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
                "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        method.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(factory, constructorConstructor, gson, typeToken, annotation);
        });
        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalArgumentException);
    }

    // Dummy classes for testing

    public static class DummyTypeAdapter extends TypeAdapter<String> {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, String value) {
        }

        @Override
        public String read(com.google.gson.stream.JsonReader in) {
            return null;
        }
    }

    public static class DummyJsonSerializer implements JsonSerializer<String> {
        @Override
        public com.google.gson.JsonElement serialize(String src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
            return null;
        }
    }

    public static class DummyJsonDeserializer implements JsonDeserializer<String> {
        @Override
        public String deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) {
            return null;
        }
    }
}