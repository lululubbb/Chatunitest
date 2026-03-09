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

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonReader_225_1Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testConsumeNonExecutePrefix_noSecurityToken_bufferHasLessThan5Chars() throws Exception {
        // Setup buffer and pos so that pos + 5 > limit and fillBuffer returns false
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 3);
        setField(jsonReader, "buffer", new char[1024]);

        JsonReader spyReader = Mockito.spy(jsonReader);

        // Mock fillBuffer private method via reflection
        doReturn(false).when(spyReader).invokePrivateBooleanMethod("fillBuffer", int.class, 5);

        // Mock nextNonWhitespace private method via reflection
        doNothing().when(spyReader).invokePrivateVoidMethod("nextNonWhitespace", boolean.class, true);

        // Set pos to 1 after nextNonWhitespace
        setField(spyReader, "pos", 1);

        invokeConsumeNonExecutePrefix(spyReader);

        // pos should be decremented by 1 after nextNonWhitespace(true)
        assertEquals(0, getIntField(spyReader, "pos"));
    }

    @Test
    @Timeout(8000)
    public void testConsumeNonExecutePrefix_noSecurityToken_bufferHas5CharsButNotMatching() throws Exception {
        // Setup buffer with chars not matching the security token
        char[] buf = new char[1024];
        buf[0] = 'a';
        buf[1] = 'b';
        buf[2] = 'c';
        buf[3] = 'd';
        buf[4] = 'e';

        setField(jsonReader, "buffer", buf);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 10);

        JsonReader spyReader = Mockito.spy(jsonReader);

        // Mock fillBuffer private method via reflection
        doReturn(true).when(spyReader).invokePrivateBooleanMethod("fillBuffer", int.class, 5);

        // Mock nextNonWhitespace private method via reflection
        doNothing().when(spyReader).invokePrivateVoidMethod("nextNonWhitespace", boolean.class, true);

        // Set pos to 1 after nextNonWhitespace
        setField(spyReader, "pos", 1);

        invokeConsumeNonExecutePrefix(spyReader);

        // pos should be decremented by 1 after nextNonWhitespace(true)
        assertEquals(0, getIntField(spyReader, "pos"));
    }

    @Test
    @Timeout(8000)
    public void testConsumeNonExecutePrefix_withSecurityToken() throws Exception {
        // Setup buffer with the security token sequence at pos
        char[] buf = new char[1024];
        buf[0] = ')';
        buf[1] = ']';
        buf[2] = '}';
        buf[3] = '\'';
        buf[4] = '\n';

        setField(jsonReader, "buffer", buf);
        setField(jsonReader, "pos", 0);
        setField(jsonReader, "limit", 10);

        JsonReader spyReader = Mockito.spy(jsonReader);

        // Mock fillBuffer private method via reflection
        doReturn(true).when(spyReader).invokePrivateBooleanMethod("fillBuffer", int.class, 5);

        // Mock nextNonWhitespace private method via reflection
        doNothing().when(spyReader).invokePrivateVoidMethod("nextNonWhitespace", boolean.class, true);

        // Set pos to 1 after nextNonWhitespace
        setField(spyReader, "pos", 1);

        invokeConsumeNonExecutePrefix(spyReader);

        // pos should be incremented by 5 after detecting security token
        assertEquals(5, getIntField(spyReader, "pos"));
    }

    private void invokeConsumeNonExecutePrefix(JsonReader reader) throws Exception {
        Method method = JsonReader.class.getDeclaredMethod("consumeNonExecutePrefix");
        method.setAccessible(true);
        method.invoke(reader);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private int getIntField(Object target, String fieldName) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getInt(target);
    }

    /**
     * Helper methods for spying on private methods with Mockito and reflection.
     * These methods are used to mock private methods since direct Mockito spying on private methods is not allowed.
     */
    private boolean invokePrivateBooleanMethod(String methodName, Class<?> paramType, Object param) throws Exception {
        Method method = JsonReader.class.getDeclaredMethod(methodName, paramType);
        method.setAccessible(true);
        return (boolean) method.invoke(this.jsonReader, param);
    }

    private void invokePrivateVoidMethod(String methodName, Class<?> paramType, Object param) throws Exception {
        Method method = JsonReader.class.getDeclaredMethod(methodName, paramType);
        method.setAccessible(true);
        method.invoke(this.jsonReader, param);
    }

    // Extend Mockito spy to add custom stubbing of private methods
    private static class JsonReaderSpy extends JsonReader {
        public JsonReaderSpy(Reader in) {
            super(in);
        }

        // This is a placeholder for Mockito to mock private methods via reflection
        public boolean invokePrivateBooleanMethod(String methodName, Class<?> paramType, Object param) throws Exception {
            Method method = JsonReader.class.getDeclaredMethod(methodName, paramType);
            method.setAccessible(true);
            return (boolean) method.invoke(this, param);
        }

        public void invokePrivateVoidMethod(String methodName, Class<?> paramType, Object param) throws Exception {
            Method method = JsonReader.class.getDeclaredMethod(methodName, paramType);
            method.setAccessible(true);
            method.invoke(this, param);
        }
    }

    private JsonReader spyWithPrivateMethods(JsonReader original) {
        JsonReaderSpy spy = new JsonReaderSpy(this.mockReader);
        try {
            setField(spy, "buffer", getField(original, "buffer"));
            setField(spy, "pos", getIntField(original, "pos"));
            setField(spy, "limit", getIntField(original, "limit"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Mockito.spy(spy);
    }

    private Object getField(Object target, String fieldName) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}