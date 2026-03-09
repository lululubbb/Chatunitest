package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_217_3Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    private boolean invokeSkipTo(String toFind) throws Exception {
        Method skipTo = JsonReader.class.getDeclaredMethod("skipTo", String.class);
        skipTo.setAccessible(true);
        try {
            return (boolean) skipTo.invoke(jsonReader, toFind);
        } catch (InvocationTargetException e) {
            // unwrap IOException
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }

    @Test
    @Timeout(8000)
    void testSkipTo_foundAtStart() throws Exception {
        // Setup buffer to contain the string at pos=0
        String target = "abc";
        char[] buf = new char[1024];
        buf[0] = 'a';
        buf[1] = 'b';
        buf[2] = 'c';
        // fill rest with something else
        for (int i = 3; i < buf.length; i++) {
            buf[i] = 'x';
        }

        setField("buffer", buf);
        setField("pos", 0);
        setField("limit", 10);
        setField("lineNumber", 0);
        setField("lineStart", 0);

        boolean result = invokeSkipTo(target);

        assertTrue(result);
        assertEquals(0, getFieldInt("pos")); // pos should not move inside skipTo (pos is incremented in loop)
        // Actually pos is incremented in loop, but pos field is updated after loop iteration, but because return true happens immediately, pos is not incremented after match.
    }

    @Test
    @Timeout(8000)
    void testSkipTo_foundAfterNewLine() throws Exception {
        // Buffer contains '\n' at pos=0, then target at pos=1
        String target = "def";
        char[] buf = new char[1024];
        buf[0] = '\n';
        buf[1] = 'd';
        buf[2] = 'e';
        buf[3] = 'f';
        for (int i = 4; i < buf.length; i++) {
            buf[i] = 'x';
        }

        setField("buffer", buf);
        setField("pos", 0);
        setField("limit", 10);
        setField("lineNumber", 5);
        setField("lineStart", 0);

        boolean result = invokeSkipTo(target);

        assertTrue(result);
        // lineNumber should be incremented once because of '\n'
        assertEquals(6, getFieldInt("lineNumber"));
        // lineStart should be pos+1 at '\n' position 0
        assertEquals(1, getFieldInt("lineStart"));
    }

    @Test
    @Timeout(8000)
    void testSkipTo_notFound() throws Exception {
        // Buffer does not contain target
        String target = "xyz";
        char[] buf = new char[1024];
        buf[0] = 'a';
        buf[1] = 'b';
        buf[2] = 'c';
        for (int i = 3; i < buf.length; i++) {
            buf[i] = 'x';
        }

        setField("buffer", buf);
        setField("pos", 0);
        setField("limit", 3);
        setField("lineNumber", 0);
        setField("lineStart", 0);

        // Override fillBuffer to return false to simulate no more data
        setFillBufferReturns(false);

        boolean result = invokeSkipTo(target);

        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testSkipTo_fillBufferThrowsIOException() throws Exception {
        String target = "abc";
        char[] buf = new char[1024];
        buf[0] = 'a';
        buf[1] = 'b';
        buf[2] = 'c';

        setField("buffer", buf);
        setField("pos", 0);
        setField("limit", 2); // less than target length to force fillBuffer call
        setField("lineNumber", 0);
        setField("lineStart", 0);

        // Override fillBuffer to throw IOException
        setFillBufferThrows(new IOException("fillBuffer exception"));

        IOException thrown = assertThrows(IOException.class, () -> invokeSkipTo(target));
        assertEquals("fillBuffer exception", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testSkipTo_multipleLines() throws Exception {
        String target = "xyz";
        char[] buf = new char[1024];
        // buffer: \n a \n x y z ...
        buf[0] = '\n';
        buf[1] = 'a';
        buf[2] = '\n';
        buf[3] = 'x';
        buf[4] = 'y';
        buf[5] = 'z';
        for (int i = 6; i < buf.length; i++) {
            buf[i] = 'x';
        }

        setField("buffer", buf);
        setField("pos", 0);
        setField("limit", 10);
        setField("lineNumber", 1);
        setField("lineStart", 0);

        boolean result = invokeSkipTo(target);

        assertTrue(result);
        // lineNumber should be incremented twice for two '\n'
        assertEquals(3, getFieldInt("lineNumber"));
        // lineStart should be pos+1 of last '\n' at pos=2, so 3
        assertEquals(3, getFieldInt("lineStart"));
    }

    // Helper to set private field
    private void setField(String fieldName, Object value) throws Exception {
        var field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(jsonReader, value);
    }

    private int getFieldInt(String fieldName) throws Exception {
        var field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getInt(jsonReader);
    }

    // Override fillBuffer(boolean) to return false
    private void setFillBufferReturns(boolean ret) throws Exception {
        Method fillBuffer = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        fillBuffer.setAccessible(true);

        // Use a spy to override fillBuffer method
        JsonReader spyReader = spy(jsonReader);
        doReturn(ret).when(spyReader).fillBuffer(anyInt());

        // Replace jsonReader with spy
        jsonReader = spyReader;
    }

    // Override fillBuffer(boolean) to throw IOException
    private void setFillBufferThrows(IOException ex) throws Exception {
        Method fillBuffer = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        fillBuffer.setAccessible(true);

        JsonReader spyReader = spy(jsonReader);
        doThrow(ex).when(spyReader).fillBuffer(anyInt());

        jsonReader = spyReader;
    }
}