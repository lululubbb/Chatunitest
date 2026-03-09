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

public class CSVFormat_13_2Test {

    @Test
    @Timeout(8000)
    void testGetHeaderComments_WhenHeaderCommentsIsNull() throws Exception {
        CSVFormat format = createCSVFormatWithHeaderComments(null);

        String[] result = format.getHeaderComments();
        assertNull(result, "Expected null when headerComments field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderComments_WhenHeaderCommentsIsNonNull() throws Exception {
        String[] comments = new String[] {"comment1", "comment2"};
        CSVFormat format = createCSVFormatWithHeaderComments(comments);

        String[] result = format.getHeaderComments();
        assertNotNull(result, "Expected non-null array when headerComments field is set");
        assertArrayEquals(comments, result, "Returned array should equal the set headerComments");
        assertNotSame(comments, result, "Returned array should be a clone, not the same instance");
    }

    private CSVFormat createCSVFormatWithHeaderComments(String[] headerComments) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        char delimiter = ',';
        Character quoteChar = '"';
        QuoteMode quoteMode = null;
        Character commentStart = null;
        Character escape = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        Object[] headerCommentsObj = headerComments;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = false;
        boolean trim = false;
        boolean trailingDelimiter = false;
        boolean autoFlush = false;

        return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerCommentsObj, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);
    }
}