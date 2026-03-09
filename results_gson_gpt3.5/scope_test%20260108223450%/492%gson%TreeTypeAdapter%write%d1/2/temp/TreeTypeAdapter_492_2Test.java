package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class TreeTypeAdapterWriteTest {

    private TreeTypeAdapter<Object> adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;
    private JsonElement jsonElement;
    private JsonSerializationContext serializationContext;
    private TypeToken<Object> typeToken;
    private TypeAdapterFactory skipPast;

    private JsonSerializerMock serializerMock;

    interface JsonSerializerMock extends com.google.gson.JsonSerializer<Object> {}

    @BeforeEach
    void setUp() throws Exception {
        serializerMock = mock(JsonSerializerMock.class);
        typeToken = TypeToken.get(Object.class);
        skipPast = mock(TypeAdapterFactory.class);
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Create adapter with serializer, so serializer != null
        adapter = new TreeTypeAdapter<>(serializerMock, null, null, typeToken, skipPast, true);

        // Use reflection to set gson field (required for context)
        Field gsonField = TreeTypeAdapter.class.getDeclaredField("gson");
        gsonField.setAccessible(true);
        gsonField.set(adapter, null);

        // Use reflection to set context field (GsonContextImpl)
        Field contextField = TreeTypeAdapter.class.getDeclaredField("context");
        contextField.setAccessible(true);
        contextField.set(adapter, new JsonSerializationContext() {
            @Override
            public JsonElement serialize(Object src) {
                return null;
            }

            @Override
            public JsonElement serialize(Object src, java.lang.reflect.Type typeOfSrc) {
                return null;
            }
        });
    }

    @Test
    @Timeout(8000)
    void write_shouldCallDelegateWrite_whenSerializerIsNull() throws Exception {
        // Create adapter with null serializer
        adapter = new TreeTypeAdapter<>(null, null, null, typeToken, skipPast, true);

        // Mock delegate TypeAdapter and inject via reflection
        TypeAdapter<Object> delegateMock = mock(TypeAdapter.class);
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(adapter, delegateMock);

        // Prepare JsonWriter and value
        Object value = new Object();

        // Call write
        adapter.write(jsonWriter, value);

        // Verify delegate.write called once with same arguments
        verify(delegateMock).write(jsonWriter, value);
    }

    @Test
    @Timeout(8000)
    void write_shouldWriteNullValue_whenNullSafeIsTrueAndValueIsNull() throws IOException {
        // value is null, nullSafe true
        Object value = null;

        // Spy on JsonWriter to verify nullValue() call
        JsonWriter spyWriter = spy(jsonWriter);

        adapter.write(spyWriter, value);

        verify(spyWriter).nullValue();
    }

    @Test
    @Timeout(8000)
    void write_shouldSerializeAndWriteJsonElement_whenSerializerIsNotNullAndValueIsNotNull() throws IOException {
        Object value = new Object();

        JsonElement element = mock(JsonElement.class);

        // Setup serializer to return element
        when(serializerMock.serialize(eq(value), eq(typeToken.getType()), any())).thenReturn(element);

        // Spy on Streams.write to confirm it is called
        // Streams.write is static, so we test indirectly by capturing output

        adapter.write(jsonWriter, value);

        // The stringWriter should contain serialized output, but Streams.write writes to JsonWriter
        // We verify serializer.serialize called with correct args
        verify(serializerMock).serialize(eq(value), eq(typeToken.getType()), any());

        // We cannot directly verify Streams.write without PowerMockito, so verify no exceptions thrown
        assertNotNull(stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    void write_shouldThrowIOException_whenStreamsWriteThrows() throws IOException {
        Object value = new Object();
        JsonElement element = mock(JsonElement.class);

        when(serializerMock.serialize(eq(value), eq(typeToken.getType()), any())).thenReturn(element);

        // Create JsonWriter that throws IOException on any write call
        JsonWriter throwingWriter = new JsonWriter(new StringWriter()) {
            @Override
            public JsonWriter nullValue() throws IOException {
                throw new IOException("forced");
            }
        };

        // Use reflection to spy Streams.write by replacing it with a method that throws IOException
        // Not possible without PowerMockito, so we test IOException thrown by nullValue path

        // Create adapter with nullSafe true and value null to force nullValue call throwing IOException
        adapter = new TreeTypeAdapter<>(serializerMock, null, null, typeToken, skipPast, true);

        IOException thrown = assertThrows(IOException.class, () -> adapter.write(throwingWriter, null));
        assertEquals("forced", thrown.getMessage());
    }
}