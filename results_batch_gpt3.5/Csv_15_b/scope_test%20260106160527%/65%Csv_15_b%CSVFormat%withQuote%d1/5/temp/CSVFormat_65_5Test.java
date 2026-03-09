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

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormatWithQuoteTest {

    @Test
    @Timeout(8000)
    void testWithQuote_char() {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;

        // Act
        CSVFormat updated = original.withQuote('"');

        // Assert
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf('"'), updated.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_char_nullQuote() {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;

        // Act
        CSVFormat updated = original.withQuote('\0');

        // Assert
        assertNotNull(updated);
        assertNotSame(original, updated);
        // '\0' is a valid char, so quoteCharacter should be '\0'
        assertEquals(Character.valueOf('\0'), updated.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_Character_null() throws Exception {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke public withQuote(Character)
        java.lang.reflect.Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);

        // Act
        CSVFormat updated = (CSVFormat) withQuoteCharMethod.invoke(original, new Object[] { null });

        // Assert
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_Character_valid() throws Exception {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;

        // Use reflection to invoke public withQuote(Character)
        java.lang.reflect.Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);

        Character quoteChar = '\'';

        // Act
        CSVFormat updated = (CSVFormat) withQuoteCharMethod.invoke(original, quoteChar);

        // Assert
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(quoteChar, updated.getQuoteCharacter());
    }
}