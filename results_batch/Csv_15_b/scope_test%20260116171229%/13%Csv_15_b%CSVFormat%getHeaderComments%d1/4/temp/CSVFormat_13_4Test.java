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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_13_4Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        // headerComments is private final, default is null in DEFAULT
        String[] comments = format.getHeaderComments();
        assertNull(comments, "Expected null when headerComments is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        // Use reflection to create CSVFormat instance with headerComments set
        String[] originalComments = new String[]{"comment1", "comment2"};
        CSVFormat format = createCSVFormatWithHeaderComments(originalComments);

        String[] returnedComments = format.getHeaderComments();

        assertNotNull(returnedComments, "Returned comments should not be null");
        assertArrayEquals(originalComments, returnedComments, "Returned comments should equal original");
        assertNotSame(originalComments, returnedComments, "Returned array should be a clone, not the same instance");

        // Modify returned array to verify original is not affected
        returnedComments[0] = "modified";
        String[] secondCall = format.getHeaderComments();
        assertEquals("comment1", secondCall[0], "Original headerComments should not be affected by modifications");
    }

    private CSVFormat createCSVFormatWithHeaderComments(String[] headerComments) throws Exception {
        // Constructor signature changed: headerComments is Object[] and header is String[]
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        char delimiter = CSVFormat.DEFAULT.getDelimiter();
        Character quoteChar = CSVFormat.DEFAULT.getQuoteCharacter();
        QuoteMode quoteMode = CSVFormat.DEFAULT.getQuoteMode();
        Character commentStart = CSVFormat.DEFAULT.getCommentMarker();
        Character escape = CSVFormat.DEFAULT.getEscapeCharacter();
        boolean ignoreSurroundingSpaces = CSVFormat.DEFAULT.getIgnoreSurroundingSpaces();
        boolean ignoreEmptyLines = CSVFormat.DEFAULT.getIgnoreEmptyLines();
        String recordSeparator = CSVFormat.DEFAULT.getRecordSeparator();
        String nullString = CSVFormat.DEFAULT.getNullString();
        Object[] headerCommentsObj = headerComments;
        String[] header = CSVFormat.DEFAULT.getHeader();
        boolean skipHeaderRecord = CSVFormat.DEFAULT.getSkipHeaderRecord();
        boolean allowMissingColumnNames = CSVFormat.DEFAULT.getAllowMissingColumnNames();
        boolean ignoreHeaderCase = CSVFormat.DEFAULT.getIgnoreHeaderCase();
        boolean trim = CSVFormat.DEFAULT.getTrim();
        boolean trailingDelimiter = CSVFormat.DEFAULT.getTrailingDelimiter();
        boolean autoFlush = CSVFormat.DEFAULT.getAutoFlush();

        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerCommentsObj, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }
}