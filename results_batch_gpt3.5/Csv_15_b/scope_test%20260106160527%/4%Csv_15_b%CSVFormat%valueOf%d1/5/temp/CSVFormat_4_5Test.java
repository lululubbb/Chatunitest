package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Predefined;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_4_5Test {

    @Test
    @Timeout(8000)
    public void testValueOf_withValidFormat() {
        for (Predefined predefined : Predefined.values()) {
            String name = predefined.name();
            CSVFormat format = CSVFormat.valueOf(name);
            assertNotNull(format, "Format should not be null for predefined: " + name);
            assertSame(predefined.getFormat(), format, "Returned format should match predefined.getFormat()");
        }
    }

    @Test
    @Timeout(8000)
    public void testValueOf_withInvalidFormat_shouldThrowIllegalArgumentException() {
        String invalidFormat = "NON_EXISTENT_FORMAT";
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(invalidFormat));
    }

    @Test
    @Timeout(8000)
    public void testValueOf_withNull_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }

    @Test
    @Timeout(8000)
    public void testValueOf_reflectionInvoke() throws Exception {
        Method valueOfMethod = CSVFormat.class.getDeclaredMethod("valueOf", String.class);
        valueOfMethod.setAccessible(true);

        // Valid input
        Object result = valueOfMethod.invoke(null, "DEFAULT");
        assertTrue(result instanceof CSVFormat);
        assertSame(CSVFormat.valueOf("DEFAULT"), result);

        // Invalid input should throw InvocationTargetException with cause IllegalArgumentException
        Executable execInvalid = () -> {
            try {
                valueOfMethod.invoke(null, "INVALID_FORMAT");
                fail("Expected InvocationTargetException not thrown");
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else {
                    throw new RuntimeException(cause);
                }
            }
        };
        IllegalArgumentException thrownInvalid = assertThrows(IllegalArgumentException.class, execInvalid);
        assertNotNull(thrownInvalid);

        // Null input should throw InvocationTargetException with cause NullPointerException
        Executable execNull = () -> {
            try {
                valueOfMethod.invoke(null, (Object) null);
                fail("Expected InvocationTargetException not thrown");
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else {
                    throw new RuntimeException(cause);
                }
            }
        };
        NullPointerException thrownNull = assertThrows(NullPointerException.class, execNull);
        assertNotNull(thrownNull);
    }
}