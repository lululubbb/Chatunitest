package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_541_6Test {

    private JsonPrimitive jsonPrimitive;

    @BeforeEach
    public void setUp() {
        // no default constructor, will create per test
    }

    private void setValue(JsonPrimitive instance, Object value) throws Exception {
        Field valueField = JsonPrimitive.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(instance, value);
    }

    private byte invokeGetAsByte(JsonPrimitive instance) throws Exception {
        Method method = JsonPrimitive.class.getDeclaredMethod("getAsByte");
        method.setAccessible(true);
        return (byte) method.invoke(instance);
    }

    @Test
    @Timeout(8000)
    public void testGetAsByte_withNumberValue() throws Exception {
        // Arrange: create JsonPrimitive with Number value
        jsonPrimitive = new JsonPrimitive( (Number) 123 );
        // Act
        byte result = invokeGetAsByte(jsonPrimitive);
        // Assert
        assertEquals((byte)123, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAsByte_withLazilyParsedNumberValue() throws Exception {
        // Arrange: LazilyParsedNumber is a subclass of Number
        jsonPrimitive = new JsonPrimitive( new LazilyParsedNumber("127") );
        // Act
        byte result = invokeGetAsByte(jsonPrimitive);
        // Assert
        assertEquals((byte)127, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAsByte_withStringValue() throws Exception {
        // Arrange: create JsonPrimitive with String that parses to byte
        jsonPrimitive = new JsonPrimitive("42");
        // Act
        byte result = invokeGetAsByte(jsonPrimitive);
        // Assert
        assertEquals((byte)42, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAsByte_withStringValue_negative() throws Exception {
        // Arrange: negative byte value string
        jsonPrimitive = new JsonPrimitive("-128");
        // Act
        byte result = invokeGetAsByte(jsonPrimitive);
        // Assert
        assertEquals((byte)-128, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAsByte_withStringValue_nonNumeric_throwsNumberFormatException() throws Exception {
        // Arrange: create JsonPrimitive with non-numeric string
        jsonPrimitive = new JsonPrimitive("notANumber");
        // Act & Assert
        NumberFormatException thrown = assertThrows(NumberFormatException.class, () -> {
            try {
                invokeGetAsByte(jsonPrimitive);
            } catch (InvocationTargetException e) {
                // unwrap the cause and rethrow if it's NumberFormatException
                Throwable cause = e.getCause();
                if (cause instanceof NumberFormatException) {
                    throw (NumberFormatException) cause;
                }
                // else rethrow original
                throw e;
            }
        });
        assertEquals("For input string: \"notANumber\"", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGetAsByte_withNumberValue_outOfByteRange() throws Exception {
        // Arrange: Number larger than byte max value
        jsonPrimitive = new JsonPrimitive(300); // 300 > 127
        // Act
        byte result = invokeGetAsByte(jsonPrimitive);
        // Assert: byteValue truncates, expect (byte)300 == 44
        assertEquals((byte)300, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAsByte_withNumberValue_negativeOutOfByteRange() throws Exception {
        // Arrange: Number less than byte min value
        jsonPrimitive = new JsonPrimitive(-130); // -130 < -128
        // Act
        byte result = invokeGetAsByte(jsonPrimitive);
        // Assert: byteValue truncates, expect (byte)-130 == 126
        assertEquals((byte)-130, result);
    }

}