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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_19_2Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        QuoteMode quoteMode = csvFormat.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_CustomQuoteMode() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL_NON_NULL);
        QuoteMode quoteMode = csvFormat.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_ReflectionAccess() throws Exception {
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormatCopy = constructor.newInstance(
                csvFormat.getDelimiter(),
                csvFormat.getQuoteCharacter(),
                csvFormat.getQuoteMode(),
                csvFormat.getCommentMarker(),
                csvFormat.getEscapeCharacter(),
                csvFormat.getIgnoreSurroundingSpaces(),
                csvFormat.getIgnoreEmptyLines(),
                csvFormat.getRecordSeparator(),
                csvFormat.getNullString(),
                null,
                csvFormat.getHeader(),
                csvFormat.getSkipHeaderRecord(),
                csvFormat.getAllowMissingColumnNames(),
                csvFormat.getIgnoreHeaderCase(),
                csvFormat.getTrim(),
                csvFormat.getTrailingDelimiter(),
                csvFormat.getAutoFlush()
        );

        // Set quoteMode field to null via reflection
        quoteModeField.set(csvFormatCopy, null);
        QuoteMode quoteMode = csvFormatCopy.getQuoteMode();
        assertNull(quoteMode);

        // Set quoteMode field to ALL_NON_NULL via reflection
        quoteModeField.set(csvFormatCopy, QuoteMode.ALL_NON_NULL);
        quoteMode = csvFormatCopy.getQuoteMode();
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }
}