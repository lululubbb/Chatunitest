package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

public class JsonAdapterAnnotationTypeAdapterFactory_297_6Test {

    private ConstructorConstructor constructorConstructor;
    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        constructorConstructor = mock(ConstructorConstructor.class);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
        gson = mock(Gson.class);
    }

    @Test
    @Timeout(8000)
    public void testConstructorSetsField() throws Exception {
        ConstructorConstructor cc = new ConstructorConstructor(Collections.emptyMap(), false, Collections.emptyList());
        JsonAdapterAnnotationTypeAdapterFactory f = new JsonAdapterAnnotationTypeAdapterFactory(cc);
        // Use reflection to check private final field constructorConstructor
        var field = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
        field.setAccessible(true);
        assertSame(cc, field.get(f));
    }

    @Test
    @Timeout(8000)
    public void testGetTypeAdapter_withJsonSerializer() throws Exception {
        // Setup a dummy @JsonAdapter annotation with a JsonSerializer class
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
                return true;
            }
        };

        TypeToken<String> typeToken = TypeToken.get(String.class);

        DummyJsonSerializer serializer = new DummyJsonSerializer();
        // Mock constructorConstructor.get(TypeToken) to return a ObjectConstructor wrapping serializer
        when(constructorConstructor.get(TypeToken.get(DummyJsonSerializer.class)))
                .thenReturn(() -> serializer);

        Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod("getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        method.setAccessible(true);

        TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, typeToken, annotation);

        assertNotNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void testGetTypeAdapter_withTypeAdapter() throws Exception {
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
                return false;
            }
        };

        TypeToken<String> typeToken = TypeToken.get(String.class);
        DummyTypeAdapter dummyAdapter = new DummyTypeAdapter();

        when(constructorConstructor.get(TypeToken.get(DummyTypeAdapter.class)))
                .thenReturn(() -> dummyAdapter);

        Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod("getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        method.setAccessible(true);

        TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, typeToken, annotation);

        assertSame(dummyAdapter, adapter);
    }

    @Test
    @Timeout(8000)
    public void testGetTypeAdapter_withTypeAdapterFactory() throws Exception {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }

            @Override
            public Class<?> value() {
                return DummyTypeAdapterFactory.class;
            }

            @Override
            public boolean nullSafe() {
                return true;
            }
        };

        TypeToken<String> typeToken = TypeToken.get(String.class);
        DummyTypeAdapterFactory factoryInstance = new DummyTypeAdapterFactory();

        when(constructorConstructor.get(TypeToken.get(DummyTypeAdapterFactory.class)))
                .thenReturn(() -> factoryInstance);

        Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod("getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        method.setAccessible(true);

        TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, typeToken, annotation);

        assertNotNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void testGetTypeAdapter_withInvalidClass() throws Exception {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }

            @Override
            public Class<?> value() {
                return String.class; // String is not JsonSerializer, TypeAdapter, or TypeAdapterFactory
            }

            @Override
            public boolean nullSafe() {
                return true;
            }
        };

        TypeToken<String> typeToken = TypeToken.get(String.class);

        Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod("getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(factory, constructorConstructor, gson, typeToken, annotation);
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    // Dummy classes for testing
    public static class DummyJsonSerializer implements JsonSerializer<String> {
        @Override
        public JsonElement serialize(String src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    public static class DummyTypeAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) {
        }

        @Override
        public String read(JsonReader in) {
            return null;
        }
    }

    public static class DummyTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            return (TypeAdapter<T>) new DummyTypeAdapter();
        }
    }

    // Helper class to simulate TypeAdapterRuntimeTypeWrapper behavior for testing
    public static class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T> {
        private final Gson context;
        private final JsonSerializer<T> serializer;
        private final TypeToken<T> type;

        private TypeAdapterRuntimeTypeWrapper(Gson context, JsonSerializer<T> serializer, TypeToken<T> type) {
            this.context = context;
            this.serializer = serializer;
            this.type = type;
        }

        public static <T> TypeAdapter<T> create(Gson context, JsonSerializer<T> serializer, TypeToken<T> type) {
            return new TypeAdapterRuntimeTypeWrapper<>(context, serializer, type);
        }

        @Override
        public void write(JsonWriter out, T value) {
        }

        @Override
        public T read(JsonReader in) {
            return null;
        }
    }
}