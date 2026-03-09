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

import java.lang.reflect.Field;

class CSVFormat_13_6Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsCloneWhenNotNull() throws Exception {
        // Create a new CSVFormat instance with headerComments set
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("comment1", "comment2");

        // Use reflection to access the private headerComments field
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Get the original headerComments array via reflection
        String[] originalComments = (String[]) headerCommentsField.get(format);

        String[] result = format.getHeaderComments();

        assertNotNull(result);
        assertArrayEquals(originalComments, result);
        // Ensure returned array is a clone, not the original reference
        assertNotSame(originalComments, result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsNullWhenNull() throws Exception {
        // Create a new CSVFormat instance without headerComments (null)
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access the private headerComments field
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);

        // Create a new CSVFormat instance with headerComments forcibly set to null
        // Since CSVFormat is immutable, directly setting the field on DEFAULT instance won't work as expected.
        // Instead, create a new instance via reflection copying all fields but with headerComments null.

        // Get all fields to copy
        Field[] fields = CSVFormat.class.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }

        // Use constructor to create a new instance with headerComments = null
        // Find the constructor with all fields
        // The private constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush)

        // Get the private constructor
        var constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        // Get existing field values from DEFAULT
        char delimiter = (char) CSVFormat.DEFAULT.getDelimiter();
        Character quoteChar = CSVFormat.DEFAULT.getQuoteCharacter();
        QuoteMode quoteMode = CSVFormat.DEFAULT.getQuoteMode();
        Character commentStart = CSVFormat.DEFAULT.getCommentMarker();
        Character escape = CSVFormat.DEFAULT.getEscapeCharacter();
        boolean ignoreSurroundingSpaces = CSVFormat.DEFAULT.getIgnoreSurroundingSpaces();
        boolean ignoreEmptyLines = CSVFormat.DEFAULT.getIgnoreEmptyLines();
        String recordSeparator = CSVFormat.DEFAULT.getRecordSeparator();
        String nullString = CSVFormat.DEFAULT.getNullString();

        // headerComments null
        Object[] headerComments = null;

        String[] header = CSVFormat.DEFAULT.getHeader();
        boolean skipHeaderRecord = CSVFormat.DEFAULT.getSkipHeaderRecord();
        boolean allowMissingColumnNames = CSVFormat.DEFAULT.getAllowMissingColumnNames();
        boolean ignoreHeaderCase = CSVFormat.DEFAULT.getIgnoreHeaderCase();
        boolean trim = CSVFormat.DEFAULT.getTrim();
        boolean trailingDelimiter = CSVFormat.DEFAULT.getTrailingDelimiter();
        boolean autoFlush = CSVFormat.DEFAULT.getAutoFlush();

        // Create new instance with headerComments = null
        CSVFormat formatWithNullHeaderComments = constructor.newInstance(
                delimiter, quoteChar, quoteMode,
                commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase, trim,
                trailingDelimiter, autoFlush);

        String[] result = formatWithNullHeaderComments.getHeaderComments();

        assertNull(result);
    }
}