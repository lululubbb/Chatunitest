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

import java.lang.reflect.Field;

public class CSVFormat_28_4Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet() {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Test when quoteCharacter is not null
        csvFormat = csvFormat.withQuote('"');
        assertTrue(csvFormat.isQuoteCharacterSet());

        // Test when quoteCharacter is null
        csvFormat = csvFormat.withQuote(null);
        assertFalse(csvFormat.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWithReflection() {
        try {
            CSVFormat csvFormat = CSVFormat.DEFAULT;

            Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
            quoteCharacterField.setAccessible(true);

            // Test when quoteCharacter is not null
            quoteCharacterField.set(csvFormat, '"');
            assertTrue(csvFormat.isQuoteCharacterSet());

            // Test when quoteCharacter is null
            quoteCharacterField.set(csvFormat, null);
            assertFalse(csvFormat.isQuoteCharacterSet());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Exception occurred while using reflection: " + e.getMessage());
        }
    }
}