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

public class JsonReaderPeekNumberTest {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    void setUp() throws IOException {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    private int invokePeekNumber() throws Exception {
        Method peekNumberMethod = JsonReader.class.getDeclaredMethod("peekNumber");
        peekNumberMethod.setAccessible(true);
        try {
            return (int) peekNumberMethod.invoke(jsonReader);
        } catch (InvocationTargetException e) {
            // Unwrap IOException thrown by peekNumber
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }

    private void setBufferAndPosLimit(String content, int pos, int limit) throws IOException {
        // Fill the buffer with content chars, pad with zeros if needed
        char[] buffer = new char[1024];
        int len = Math.min(content.length(), buffer.length);
        content.getChars(0, len, buffer, 0);
        // Fill rest with zero chars
        for (int i = len; i < buffer.length; i++) {
            buffer[i] = 0;
        }
        setField("buffer", buffer);
        setField("pos", pos);
        setField("limit", limit);
    }

    private void setField(String fieldName, Object value) {
        try {
            var field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(jsonReader, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPeeked(int value) {
        setField("peeked", value);
    }

    private int getPeeked() {
        try {
            var field = JsonReader.class.getDeclaredField("peeked");
            field.setAccessible(true);
            return (int) field.get(jsonReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private long getPeekedLong() {
        try {
            var field = JsonReader.class.getDeclaredField("peekedLong");
            field.setAccessible(true);
            return (long) field.get(jsonReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getPos() {
        try {
            var field = JsonReader.class.getDeclaredField("pos");
            field.setAccessible(true);
            return (int) field.get(jsonReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getPeekedNumberLength() {
        try {
            var field = JsonReader.class.getDeclaredField("peekedNumberLength");
            field.setAccessible(true);
            return (int) field.get(jsonReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPos(int pos) {
        setField("pos", pos);
    }

    private void setLimit(int limit) {
        setField("limit", limit);
    }

    private void setLenient(boolean lenient) {
        setField("lenient", lenient);
    }

    private void setFillBufferReturn(boolean returnValue) {
        try {
            Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
            fillBufferMethod.setAccessible(true);
            // We cannot mock private method directly, so we wrap Reader to simulate behavior
            // Instead, we rely on buffer and limit setup to avoid fillBuffer call
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void peekNumber_validPositiveLong() throws Exception {
        String number = "123456789";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(15, result); // PEEKED_LONG = 15
        assertEquals(123456789L, getPeekedLong());
        assertEquals(number.length(), getPos());
    }

    @Test
    @Timeout(8000)
    void peekNumber_validNegativeLong() throws Exception {
        String number = "-987654321";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(15, result);
        assertEquals(-987654321L, getPeekedLong());
        assertEquals(number.length(), getPos());
    }

    @Test
    @Timeout(8000)
    void peekNumber_zero() throws Exception {
        String number = "0";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        // Leading zero is not allowed if followed by digits, but here single zero is allowed
        assertEquals(15, result);
        assertEquals(0L, getPeekedLong());
        assertEquals(number.length(), getPos());
    }

    @Test
    @Timeout(8000)
    void peekNumber_leadingZeroFollowedByDigit_returnsNone() throws Exception {
        String number = "01";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(0, result); // PEEKED_NONE = 0
    }

    @Test
    @Timeout(8000)
    void peekNumber_validFractionNumber() throws Exception {
        String number = "123.456";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result); // PEEKED_NUMBER = 16
        assertEquals(number.length(), getPeekedNumberLength());
    }

    @Test
    @Timeout(8000)
    void peekNumber_validExponentNumber() throws Exception {
        String number = "1e10";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result);
        assertEquals(number.length(), getPeekedNumberLength());
    }

    @Test
    @Timeout(8000)
    void peekNumber_validNegativeExponentNumber() throws Exception {
        String number = "-2.5e-3";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result);
        assertEquals(number.length(), getPeekedNumberLength());
    }

    @Test
    @Timeout(8000)
    void peekNumber_invalidSignPosition_returnsNone() throws Exception {
        String number = "1-23";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void peekNumber_invalidPlusSign_returnsNone() throws Exception {
        String number = "+123";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void peekNumber_invalidCharacter_returnsNone() throws Exception {
        String number = "123a456";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void peekNumber_emptyBuffer_returnsNone() throws Exception {
        setBufferAndPosLimit("", 0, 0);
        int result = invokePeekNumber();
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void peekNumber_bufferEndsBeforeNumberCompletes_returnsNone() throws Exception {
        String number = "1234567890";
        setBufferAndPosLimit(number, 0, 5); // limit less than number length
        // We simulate fillBuffer false by not mocking fillBuffer, so it returns false implicitly
        int result = invokePeekNumber();
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void peekNumber_numberTooLong_returnsNone() throws Exception {
        // number length equal to buffer length to trigger too long condition
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            sb.append('1');
        }
        setBufferAndPosLimit(sb.toString(), 0, sb.length());
        int result = invokePeekNumber();
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void peekNumber_minValueLongNegative() throws Exception {
        // Long.MIN_VALUE = -9223372036854775808
        String number = "-9223372036854775808";
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result); // Because of special check, it's PEEKED_NUMBER not PEEKED_LONG
        assertEquals(number.length(), getPeekedNumberLength());
    }

    @Test
    @Timeout(8000)
    void peekNumber_minValueLongNegativeButNegativeFlagFalse_returnsLong() throws Exception {
        // Trick to test value == Long.MIN_VALUE and negative == false, should not store as long
        // We simulate by setting buffer to Long.MIN_VALUE without '-' sign (not valid number)
        String number = "9223372036854775808"; // Long.MIN_VALUE absolute value + 1
        setBufferAndPosLimit(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result);
        assertEquals(number.length(), getPeekedNumberLength());
    }
}