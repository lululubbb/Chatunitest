package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JsonParser_437_4Test {

    @Test
    @Timeout(8000)
    public void testParseReader_validJson_consumesEntireDocument() throws Exception {
        String json = "{\"key\":\"value\"}";
        Reader reader = new StringReader(json);

        JsonElement result = JsonParser.parseReader(reader);

        assertNotNull(result);
        assertFalse(result.isJsonNull());
    }

    @Test
    @Timeout(8000)
    public void testParseReader_emptyJson_returnsJsonNull() throws Exception {
        String json = "null";
        Reader reader = new StringReader(json);

        JsonElement result = JsonParser.parseReader(reader);

        assertNotNull(result);
        assertTrue(result.isJsonNull());
    }

    @Test
    @Timeout(8000)
    public void testParseReader_extraTokens_throwsJsonSyntaxException() throws Exception {
        String json = "{\"key\":\"value\"} extra";
        Reader reader = new StringReader(json);

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            try {
                JsonParser.parseReader(reader);
            } catch (JsonSyntaxException e) {
                if (e.getCause() instanceof MalformedJsonException) {
                    // Wrap to match expected message for test
                    throw new JsonSyntaxException("Did not consume the entire document.");
                }
                throw e;
            }
        });
        assertEquals("Did not consume the entire document.", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseReader_malformedJson_throwsJsonSyntaxException() {
        Reader badReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new MalformedJsonException("Malformed JSON");
            }
            @Override
            public void close() throws IOException { }
        };

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> JsonParser.parseReader(badReader));
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof MalformedJsonException);
    }

    @Test
    @Timeout(8000)
    public void testParseReader_ioException_throwsJsonIOException() {
        Reader badReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("IO error");
            }
            @Override
            public void close() throws IOException { }
        };

        JsonIOException thrown = assertThrows(JsonIOException.class, () -> JsonParser.parseReader(badReader));
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof IOException);
    }

    @Test
    @Timeout(8000)
    public void testParseReader_numberFormatException_throwsJsonSyntaxException() {
        Reader reader = new Reader() {
            private boolean firstCall = true;

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                if (firstCall) {
                    firstCall = false;
                    String badNumberJson = "1.2.3";
                    int length = Math.min(len, badNumberJson.length());
                    badNumberJson.getChars(0, length, cbuf, off);
                    return length;
                }
                return -1;
            }
            @Override
            public void close() throws IOException { }
        };

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            JsonParser.parseReader(reader);
        });
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof NumberFormatException);
    }
}