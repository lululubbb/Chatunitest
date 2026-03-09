package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.$Gson$Preconditions;
import java.lang.reflect.Type;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.Streams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

public class TreeTypeAdapter_491_1Test {

    private Gson gson;
    private TypeToken<String> typeToken;
    private TypeAdapterFactory skipPast;

    @BeforeEach
    public void setUp() {
        gson = mock(Gson.class);
        typeToken = TypeToken.get(String.class);
        skipPast = mock(TypeAdapterFactory.class);
    }

    @Test
    @Timeout(8000)
    public void read_WhenDeserializerIsNull_DelegatesRead() throws Exception {
        JsonDeserializer<String> deserializer = null;
        JsonSerializer<String> serializer = null;
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);

        @SuppressWarnings("unchecked")
        TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
        // Inject delegate adapter via reflection
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(adapter, delegateAdapter);

        JsonReader in = mock(JsonReader.class);
        when(delegateAdapter.read(in)).thenReturn("delegatedValue");

        String result = adapter.read(in);

        verify(delegateAdapter).read(in);
        assertEquals("delegatedValue", result);
    }

    @Test
    @Timeout(8000)
    public void read_WhenDeserializerNotNull_AndValueIsJsonNullAndNullSafe_ReturnsNull() throws Exception {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        JsonSerializer<String> serializer = null;
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);

        JsonReader in = mock(JsonReader.class);

        // Mock Streams.parse to return JsonElement.isJsonNull() = true
        try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
            JsonElement jsonElement = mock(JsonElement.class);
            when(jsonElement.isJsonNull()).thenReturn(true);
            streamsMockedStatic.when(() -> Streams.parse(in)).thenReturn(jsonElement);

            String result = adapter.read(in);

            assertNull(result);
            verify(jsonElement).isJsonNull();
            verifyNoInteractions(deserializer); // deserialize should not be called
        }
    }

    @Test
    @Timeout(8000)
    public void read_WhenDeserializerNotNull_AndValueIsNotJsonNull_Deserializes() throws Exception {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        JsonSerializer<String> serializer = null;
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);

        JsonReader in = mock(JsonReader.class);

        try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
            JsonElement jsonElement = mock(JsonElement.class);
            when(jsonElement.isJsonNull()).thenReturn(false);
            streamsMockedStatic.when(() -> Streams.parse(in)).thenReturn(jsonElement);

            // Access context field to verify deserializer call
            Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
            contextField.setAccessible(true);
            JsonDeserializationContext context = (JsonDeserializationContext) contextField.get(adapter);

            when(deserializer.deserialize(eq(jsonElement), eq(typeToken.getType()), eq(context))).thenReturn("deserializedValue");

            String result = adapter.read(in);

            assertEquals("deserializedValue", result);
            verify(jsonElement).isJsonNull();
            verify(deserializer).deserialize(jsonElement, typeToken.getType(), context);
        }
    }

    @Test
    @Timeout(8000)
    public void read_WhenDeserializerNotNull_AndNullSafeFalse_DeserializesEvenIfJsonNull() throws Exception {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        JsonSerializer<String> serializer = null;
        // nullSafe = false
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, false);

        JsonReader in = mock(JsonReader.class);

        try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
            JsonElement jsonElement = mock(JsonElement.class);
            // Remove when(jsonElement.isJsonNull()) since it is not called when nullSafe=false
            streamsMockedStatic.when(() -> Streams.parse(in)).thenReturn(jsonElement);

            Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
            contextField.setAccessible(true);
            JsonDeserializationContext context = (JsonDeserializationContext) contextField.get(adapter);

            when(deserializer.deserialize(eq(jsonElement), eq(typeToken.getType()), eq(context))).thenReturn("deserializedValue");

            String result = adapter.read(in);

            assertEquals("deserializedValue", result);
            // Verify no call to isJsonNull()
            verify(jsonElement, never()).isJsonNull();
            verify(deserializer).deserialize(jsonElement, typeToken.getType(), context);
        }
    }
}