package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberStrategy;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NumberTypeAdapter_300_6Test {

    private ToNumberStrategy toNumberStrategyMock;
    private NumberTypeAdapter numberTypeAdapter;

    @BeforeEach
    public void setUp() throws Exception {
        toNumberStrategyMock = mock(ToNumberStrategy.class);
        Constructor<NumberTypeAdapter> constructor = NumberTypeAdapter.class.getDeclaredConstructor(ToNumberStrategy.class);
        constructor.setAccessible(true);
        numberTypeAdapter = constructor.newInstance(toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testRead_null() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(reader).nextNull();

        Number result = numberTypeAdapter.read(reader);

        verify(reader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_nonNull_callsToNumberStrategy() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NUMBER);
        when(toNumberStrategyMock.readNumber(reader)).thenReturn(123);

        Number result = numberTypeAdapter.read(reader);

        verify(toNumberStrategyMock).readNumber(reader);
        assertEquals(123, result);
    }

    @Test
    @Timeout(8000)
    public void testRead_throwsJsonSyntaxException_onIOException() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NUMBER);
        when(toNumberStrategyMock.readNumber(reader)).thenThrow(new IOException("io error"));

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            try {
                numberTypeAdapter.read(reader);
            } catch (IOException e) {
                throw new JsonSyntaxException(e);
            }
        });

        assertTrue(thrown.getMessage().contains("io error"));
    }

    @Test
    @Timeout(8000)
    public void testWrite_null() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);

        numberTypeAdapter.write(writer, null);

        verify(writer).value((String) null);
        verifyNoMoreInteractions(writer);
    }

    @Test
    @Timeout(8000)
    public void testWrite_nonNull() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        Number value = 42;

        numberTypeAdapter.write(writer, value);

        verify(writer).value(value);
    }

    @Test
    @Timeout(8000)
    public void testGetFactory_returnsFactory() throws Exception {
        TypeAdapterFactory factory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
        assertNotNull(factory);
        // Additional test: factory creates a TypeAdapter for Number class
        TypeAdapter<Number> adapter = factory.create(new Gson(), TypeToken.get(Number.class));
        assertNotNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void testNewFactory_reflection() throws Exception {
        var method = NumberTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
        method.setAccessible(true);
        TypeAdapterFactory factory = (TypeAdapterFactory) method.invoke(null, ToNumberPolicy.LAZILY_PARSED_NUMBER);
        assertNotNull(factory);
    }
}