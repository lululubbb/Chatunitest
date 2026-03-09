package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.Streams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class TreeTypeAdapterWriteTest {

    private JsonSerializer<String> serializerMock;
    private TypeAdapter<String> delegateMock;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;
    private TreeTypeAdapter<String> treeTypeAdapter;
    private TypeToken<String> typeToken;
    private TypeAdapterFactory skipPastMock;

    @BeforeEach
    public void setUp() throws Exception {
        serializerMock = mock(JsonSerializer.class);
        delegateMock = mock(TypeAdapter.class);
        skipPastMock = mock(TypeAdapterFactory.class);
        typeToken = TypeToken.get(String.class);
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        // Create instance of TreeTypeAdapter with nullSafe = true
        treeTypeAdapter = new TreeTypeAdapter<>(serializerMock, null, null, typeToken, skipPastMock, true);

        // Inject delegate mock using reflection to avoid null pointer in delegate()
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(treeTypeAdapter, delegateMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withNullSerializer_delegatesWrite() throws Exception {
        // Create instance with serializer = null, nullSafe = false
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, null, typeToken, skipPastMock, false);

        // Inject delegate mock
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(adapter, delegateMock);

        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);

        String value = "testValue";

        adapter.write(jw, value);

        verify(delegateMock, times(1)).write(jw, value);
        assertEquals("", sw.toString());
    }

    @Test
    @Timeout(8000)
    public void testWrite_nullSafeTrue_valueNull_writesNullValue() throws IOException {
        // serializer is non-null (serializerMock)
        // nullSafe = true (set in setUp)
        // value = null

        treeTypeAdapter.write(jsonWriter, null);

        String output = stringWriter.toString();
        // JsonWriter.nullValue() writes "null"
        assertEquals("null", output);
    }

    @Test
    @Timeout(8000)
    public void testWrite_serializerNonNull_valueNonNull_serializesAndWrites() throws IOException {
        String value = "abc";
        JsonElement elementMock = mock(JsonElement.class);

        when(serializerMock.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(elementMock);

        treeTypeAdapter.write(jsonWriter, value);

        verify(serializerMock, times(1)).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
    }

    @Test
    @Timeout(8000)
    public void testWrite_nullSafeFalse_valueNull_serializesAndWrites() throws IOException {
        // Create instance with nullSafe = false
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializerMock, null, null, typeToken, skipPastMock, false);

        String value = null;
        JsonElement elementMock = JsonNull.INSTANCE;

        when(serializerMock.serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(elementMock);

        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);

        adapter.write(jw, value);

        verify(serializerMock, times(1)).serialize(eq(value), eq(typeToken.getType()), any(JsonSerializationContext.class));
        // Because serializer returned JsonNull, Streams.write should write "null"
        assertEquals("null", sw.toString());
    }

    @Test
    @Timeout(8000)
    public void testDelegateMethod_returnsDelegate() throws Exception {
        // Use reflection to invoke private delegate() method
        Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
        delegateMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        TypeAdapter<String> delegateReturned = (TypeAdapter<String>) delegateMethod.invoke(treeTypeAdapter);

        assertNotNull(delegateReturned);
    }
}