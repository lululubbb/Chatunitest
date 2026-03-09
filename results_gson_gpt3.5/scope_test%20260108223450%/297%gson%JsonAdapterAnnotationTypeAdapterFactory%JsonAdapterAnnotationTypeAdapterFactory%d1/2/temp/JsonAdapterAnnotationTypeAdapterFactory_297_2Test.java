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
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

public class JsonAdapterAnnotationTypeAdapterFactory_297_2Test {

    @Mock
    private ConstructorConstructor mockConstructorConstructor;

    @Mock
    private Gson mockGson;

    private JsonAdapterAnnotationTypeAdapterFactory factory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor);
    }

    // Helper classes for testing
    @JsonAdapter(TestTypeAdapter.class)
    static class AnnotatedClass {}

    static class TestTypeAdapter extends TypeAdapter<AnnotatedClass> {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, AnnotatedClass value) { }
        @Override
        public AnnotatedClass read(com.google.gson.stream.JsonReader in) { return null; }
    }

    @Test
    @Timeout(8000)
    public void testCreate_withJsonAdapterAnnotation_returnsTypeAdapter() throws Exception {
        TypeToken<AnnotatedClass> typeToken = TypeToken.get(AnnotatedClass.class);

        // Mock ObjectConstructor<TestTypeAdapter> that returns a real TestTypeAdapter instance
        TestTypeAdapter realTestTypeAdapter = new TestTypeAdapter();
        ObjectConstructor<TestTypeAdapter> realObjectConstructor = () -> realTestTypeAdapter;

        when(mockConstructorConstructor.get(TypeToken.get(TestTypeAdapter.class)))
                .thenReturn(realObjectConstructor);

        // Use reflection to get private getTypeAdapter method
        Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
                "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        getTypeAdapterMethod.setAccessible(true);

        JsonAdapter annotation = AnnotatedClass.class.getAnnotation(JsonAdapter.class);
        assertNotNull(annotation);

        Object result = getTypeAdapterMethod.invoke(factory, mockConstructorConstructor, mockGson, typeToken, annotation);
        assertNotNull(result);
        assertTrue(result instanceof TypeAdapter<?>);
    }

    @Test
    @Timeout(8000)
    public void testCreate_withoutJsonAdapterAnnotation_returnsNull() {
        TypeToken<String> typeToken = TypeToken.get(String.class);
        TypeAdapter<?> adapter = factory.create(mockGson, typeToken);
        assertNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void testCreate_withMockedConstructorConstructor_andJsonAdapter() throws Exception {
        // Setup a dummy JsonAdapter annotation on a dummy class
        @JsonAdapter(TestTypeAdapter.class)
        class Dummy {}

        TypeToken<Dummy> dummyType = TypeToken.get(Dummy.class);
        JsonAdapter annotation = Dummy.class.getAnnotation(JsonAdapter.class);

        // Mock ObjectConstructor<TestTypeAdapter> that returns a real TestTypeAdapter instance
        TestTypeAdapter realTestTypeAdapter = new TestTypeAdapter();
        ObjectConstructor<TestTypeAdapter> realObjectConstructor = () -> realTestTypeAdapter;

        when(mockConstructorConstructor.get(TypeToken.get(TestTypeAdapter.class)))
                .thenReturn(realObjectConstructor);

        // Use reflection to invoke private getTypeAdapter method
        Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
                "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
        getTypeAdapterMethod.setAccessible(true);

        Object adapter = getTypeAdapterMethod.invoke(factory, mockConstructorConstructor, mockGson, dummyType, annotation);

        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter<?>);
    }
}