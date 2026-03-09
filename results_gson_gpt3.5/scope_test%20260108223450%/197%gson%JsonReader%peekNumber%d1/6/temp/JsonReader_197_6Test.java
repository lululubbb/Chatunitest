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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReaderPeekNumberTest {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    private void setBuffer(String content, int pos, int limit) throws Exception {
        Field bufferField = JsonReader.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        char[] buffer = new char[1024];
        System.arraycopy(content.toCharArray(), 0, buffer, 0, content.length());
        bufferField.set(jsonReader, buffer);

        Field posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        posField.setInt(jsonReader, pos);

        Field limitField = JsonReader.class.getDeclaredField("limit");
        limitField.setAccessible(true);
        limitField.setInt(jsonReader, limit);
    }

    private int invokePeekNumber() throws Exception {
        Method peekNumber = JsonReader.class.getDeclaredMethod("peekNumber");
        peekNumber.setAccessible(true);
        return (int) peekNumber.invoke(jsonReader);
    }

    private int getPeekedField() throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        return peekedField.getInt(jsonReader);
    }

    private long getPeekedLongField() throws Exception {
        Field peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
        peekedLongField.setAccessible(true);
        return peekedLongField.getLong(jsonReader);
    }

    private int getPeekedNumberLength() throws Exception {
        Field peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
        peekedNumberLengthField.setAccessible(true);
        return peekedNumberLengthField.getInt(jsonReader);
    }

    private int getPosField() throws Exception {
        Field posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        return posField.getInt(jsonReader);
    }

    private void setPosField(int value) throws Exception {
        Field posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        posField.setInt(jsonReader, value);
    }

    private void mockFillBufferReturn(boolean value) throws Exception {
        Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        fillBufferMethod.setAccessible(true);
        // Cannot mock private method directly, so we spy the JsonReader instance
        JsonReader spyReader = spy(jsonReader);
        doReturn(value).when(spyReader).fillBuffer(anyInt());
        // Replace jsonReader with spyReader for this test
        jsonReader = spyReader;
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_simplePositiveInteger() throws Exception {
        String number = "12345";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(15, result); // PEEKED_LONG = 15
        assertEquals(12345L, getPeekedLongField());
        assertEquals(number.length(), getPosField());
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_simpleNegativeInteger() throws Exception {
        String number = "-98765";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(15, result); // PEEKED_LONG = 15
        assertEquals(-98765L, getPeekedLongField());
        assertEquals(number.length(), getPosField());
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_leadingZero() throws Exception {
        String number = "0123";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(0, result); // PEEKED_NONE = 0
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_decimalNumber() throws Exception {
        String number = "123.456";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result); // PEEKED_NUMBER = 16
        assertEquals(number.length(), getPeekedNumberLength());
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_exponentNumber() throws Exception {
        String number = "1e10";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result); // PEEKED_NUMBER = 16
        assertEquals(number.length(), getPeekedNumberLength());
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_negativeExponentNumber() throws Exception {
        String number = "-2E-5";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result); // PEEKED_NUMBER = 16
        assertEquals(number.length(), getPeekedNumberLength());
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_invalidSignPosition() throws Exception {
        // '+' not after 'e' or 'E'
        String number = "+123";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(0, result); // PEEKED_NONE = 0
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_invalidMultipleSigns() throws Exception {
        // '-' after digit but not after 'e'
        String number = "12-3";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_numberTooLong() throws Exception {
        // Fill buffer with 1024 '1's to simulate too long number
        char[] chars = new char[1024];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = '1';
        }
        String number = new String(chars);
        setBuffer(number, 0, number.length());

        // Spy to mock fillBuffer to return false after buffer full
        JsonReader spyReader = spy(jsonReader);
        doReturn(false).when(spyReader).fillBuffer(anyInt());
        Field bufferField = JsonReader.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        bufferField.set(spyReader, chars);
        Field posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        posField.setInt(spyReader, 0);
        Field limitField = JsonReader.class.getDeclaredField("limit");
        limitField.setAccessible(true);
        limitField.setInt(spyReader, chars.length);

        jsonReader = spyReader;

        int result = invokePeekNumber();
        assertEquals(0, result); // PEEKED_NONE = 0
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_zeroNegativeZero() throws Exception {
        // "-0" should return PEEKED_NUMBER, not PEEKED_LONG
        String number = "-0";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(16, result); // PEEKED_NUMBER = 16
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_longMinValue() throws Exception {
        // Long.MIN_VALUE = -9223372036854775808
        // We test number string equals Long.MIN_VALUE
        String number = "-9223372036854775808";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(15, result); // PEEKED_LONG = 15
        assertEquals(Long.MIN_VALUE, getPeekedLongField());
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_longMinValuePositive() throws Exception {
        // Same digits as Long.MIN_VALUE but positive should fail
        String number = "9223372036854775808";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        // Should be PEEKED_NUMBER because it doesn't fit in long
        assertEquals(16, result);
        assertEquals(number.length(), getPeekedNumberLength());
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_nonLiteralCharacterStops() throws Exception {
        String number = "123a";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        // Stops at 'a', so should return PEEKED_LONG (digits before 'a')
        assertEquals(15, result);
        assertEquals(123L, getPeekedLongField());
        assertEquals(3, getPosField());
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_literalCharacterReturnsNone() throws Exception {
        // '!' is a literal character, should return PEEKED_NONE
        String number = "123!";
        setBuffer(number, 0, number.length());
        int result = invokePeekNumber();
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    public void testPeekNumber_emptyBufferReturnsNone() throws Exception {
        setBuffer("", 0, 0);
        int result = invokePeekNumber();
        assertEquals(0, result);
    }
}