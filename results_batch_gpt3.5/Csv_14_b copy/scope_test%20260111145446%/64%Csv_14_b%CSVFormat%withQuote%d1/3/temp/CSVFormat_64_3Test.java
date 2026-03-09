package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class CSVFormat_64_3Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_validCharacter() {
        // Given
        Character quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.newFormat(',').withIgnoreEmptyLines(true).withRecordSeparator("\r\n");

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_invalidCharacter() {
        // Given
        Character quoteChar = '\n';
        CSVFormat csvFormat = CSVFormat.newFormat(',').withIgnoreEmptyLines(true).withRecordSeparator("\r\n");

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withQuote(quoteChar));

        // Then
        assertEquals("The quoteChar cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_usingReflection() throws Exception {
        // Given
        Character quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.newFormat(',').withIgnoreEmptyLines(true).withRecordSeparator("\r\n");

        // When
        CSVFormat result = invokePrivateMethod(csvFormat, "withQuote", Character.class, quoteChar);

        // Then
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
    }

    private <T> T invokePrivateMethod(Object obj, String methodName, Class<?> parameterType, Object arg) throws Exception {
        Method method = obj.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(obj, arg);
    }
}