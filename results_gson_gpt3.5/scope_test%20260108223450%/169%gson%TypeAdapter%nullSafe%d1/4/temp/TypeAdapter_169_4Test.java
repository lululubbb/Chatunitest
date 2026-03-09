package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;

class TypeAdapterNullSafeTest {

    private TypeAdapter<String> originalAdapter;
    private TypeAdapter<String> nullSafeAdapter;
    private JsonWriter mockWriter;
    private JsonReader mockReader;

    @BeforeEach
    void setUp() {
        originalAdapter = new TypeAdapter<String>() {
            @Override
            public void write(JsonWriter out, String value) throws IOException {
                out.value("written:" + value);
            }

            @Override
            public String read(JsonReader in) throws IOException {
                return "read:" + in.nextString();
            }
        };
        nullSafeAdapter = originalAdapter.nullSafe();
        mockWriter = mock(JsonWriter.class);
        mockReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    void write_withNonNullValue_callsOriginalWrite() throws IOException {
        String value = "testValue";

        nullSafeAdapter.write(mockWriter, value);

        InOrder inOrder = inOrder(mockWriter);
        inOrder.verify(mockWriter, never()).nullValue();
        inOrder.verify(mockWriter).value("written:" + value);
    }

    @Test
    @Timeout(8000)
    void write_withNullValue_callsNullValue() throws IOException {
        nullSafeAdapter.write(mockWriter, null);

        verify(mockWriter).nullValue();
        verify(mockWriter, never()).value(anyString());
    }

    @Test
    @Timeout(8000)
    void read_withNullToken_returnsNull() throws IOException {
        when(mockReader.peek()).thenReturn(JsonToken.NULL);

        String result = nullSafeAdapter.read(mockReader);

        verify(mockReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_withNonNullToken_callsOriginalRead() throws IOException {
        when(mockReader.peek()).thenReturn(JsonToken.STRING);
        when(mockReader.nextString()).thenReturn("input");

        String result = nullSafeAdapter.read(mockReader);

        assertEquals("read:input", result);
    }
}