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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_28_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        // Use DEFAULT as base instance
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsSet() throws Exception {
        // Create a CSVFormat instance with quoteCharacter set to a non-null value
        CSVFormat formatWithQuote = csvFormat.withQuote('"');
        // Use reflection to read the private field quoteCharacter and assert it's not null
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharValue = (Character) quoteCharacterField.get(formatWithQuote);
        assertNotNull(quoteCharValue);

        // Call isQuoteCharacterSet and assert true
        assertTrue(formatWithQuote.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        // Create a CSVFormat instance with quoteCharacter set to null
        CSVFormat formatWithNullQuote = csvFormat.withQuote((Character) null);
        // Use reflection to read the private field quoteCharacter and assert it's null
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharValue = (Character) quoteCharacterField.get(formatWithNullQuote);
        assertNull(quoteCharValue);

        // Call isQuoteCharacterSet and assert false
        assertFalse(formatWithNullQuote.isQuoteCharacterSet());
    }
}