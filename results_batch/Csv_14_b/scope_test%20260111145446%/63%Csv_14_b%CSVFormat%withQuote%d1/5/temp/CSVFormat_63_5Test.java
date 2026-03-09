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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_63_5Test {

    @Test
    @Timeout(8000)
    public void testWithQuote() {
        // Given
        char quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteUsingReflection() throws Exception {
        // Given
        char quoteChar = '"';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat csvFormatSpy = spy(csvFormat);

        // When
        CSVFormat result = (CSVFormat) invokePrivateMethod(csvFormatSpy, "withQuote", Character.class, quoteChar);

        // Then
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
    }

    private Object invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument)
            throws Exception {
        Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return method.invoke(object, argument);
    }
}