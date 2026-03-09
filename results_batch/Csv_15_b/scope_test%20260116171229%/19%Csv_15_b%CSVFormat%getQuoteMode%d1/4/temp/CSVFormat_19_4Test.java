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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Constructor;

class CSVFormat_19_4Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test getQuoteMode returns correct QuoteMode")
    void testGetQuoteMode() {
        // Using predefined CSVFormat.DEFAULT which has quoteMode set in constructor
        CSVFormat formatDefault = CSVFormat.DEFAULT;
        assertNotNull(formatDefault.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, formatDefault.getQuoteMode());

        // Create a CSVFormat with a specific QuoteMode using withQuoteMode method
        CSVFormat formatAllNonNull = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL_NON_NULL);
        assertNotNull(formatAllNonNull.getQuoteMode());
        assertEquals(QuoteMode.ALL_NON_NULL, formatAllNonNull.getQuoteMode());

        // Since withQuoteMode(null) is not allowed (throws NPE), we test by reflection to simulate null quoteMode
        CSVFormat formatNullQuoteMode = null;
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                    boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                    boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            formatNullQuoteMode = constructor.newInstance(
                    CSVFormat.DEFAULT.getDelimiter(),
                    CSVFormat.DEFAULT.getQuoteCharacter(),
                    null, // quoteMode null explicitly
                    CSVFormat.DEFAULT.getCommentMarker(),
                    CSVFormat.DEFAULT.getEscapeCharacter(),
                    CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                    CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                    CSVFormat.DEFAULT.getRecordSeparator(),
                    CSVFormat.DEFAULT.getNullString(),
                    null,
                    CSVFormat.DEFAULT.getHeader(),
                    CSVFormat.DEFAULT.getSkipHeaderRecord(),
                    CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                    CSVFormat.DEFAULT.getIgnoreHeaderCase(),
                    CSVFormat.DEFAULT.getTrim(),
                    CSVFormat.DEFAULT.getTrailingDelimiter(),
                    CSVFormat.DEFAULT.getAutoFlush()
            );
        } catch (Exception e) {
            fail("Reflection instantiation failed: " + e.getMessage());
        }
        assertNotNull(formatNullQuoteMode);
        assertNull(formatNullQuoteMode.getQuoteMode());
    }
}