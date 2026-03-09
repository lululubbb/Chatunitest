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

public class CSVFormat_29_3Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_whenQuoteCharacterIsSet() {
        // Create a new instance with quoteCharacter set to '"'
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"');

        boolean result = format.isQuoteCharacterSet();
        assertTrue(result, "Expected isQuoteCharacterSet to return true when quoteCharacter is set");
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        // Create a new CSVFormat instance with quoteCharacter set to null using reflection

        // Start from DEFAULT
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to create a new instance with quoteCharacter = null
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        // Extract current values from DEFAULT via getters
        char delimiter = format.getDelimiter();
        QuoteMode quoteMode = format.getQuoteMode();
        Character commentMarker = format.getCommentMarker();
        Character escapeCharacter = format.getEscapeCharacter();
        boolean ignoreSurroundingSpaces = format.getIgnoreSurroundingSpaces();
        boolean ignoreEmptyLines = format.getIgnoreEmptyLines();
        String recordSeparator = format.getRecordSeparator();
        String nullString = format.getNullString();
        Object[] headerComments = format.getHeaderComments();
        String[] header = format.getHeader();
        boolean skipHeaderRecord = format.getSkipHeaderRecord();
        boolean allowMissingColumnNames = format.getAllowMissingColumnNames();
        boolean ignoreHeaderCase = format.getIgnoreHeaderCase();
        boolean trim = format.getTrim();
        boolean trailingDelimiter = format.getTrailingDelimiter();
        boolean autoFlush = format.getAutoFlush();

        CSVFormat newFormat = constructor.newInstance(
                delimiter,
                null, // quoteCharacter set to null here
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase,
                trim,
                trailingDelimiter,
                autoFlush
        );

        boolean result = newFormat.isQuoteCharacterSet();
        assertFalse(result, "Expected isQuoteCharacterSet to return false when quoteCharacter is null");
    }
}