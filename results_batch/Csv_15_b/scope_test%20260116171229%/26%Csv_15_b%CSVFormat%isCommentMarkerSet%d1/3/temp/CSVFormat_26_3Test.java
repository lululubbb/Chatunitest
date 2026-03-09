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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_26_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_WhenCommentMarkerIsNull() throws Exception {
        CSVFormat format = createCSVFormatWithCommentMarker(null);
        assertFalse(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_WhenCommentMarkerIsSet() throws Exception {
        CSVFormat format = createCSVFormatWithCommentMarker('#');
        assertTrue(format.isCommentMarkerSet());
    }

    private CSVFormat createCSVFormatWithCommentMarker(Character commentMarker) throws Exception {
        Class<?> clazz = CSVFormat.class;

        Constructor<?> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        QuoteMode quoteModeAllNonNull = QuoteMode.ALL_NON_NULL;

        // Adjust arguments to match constructor parameter types exactly
        return (CSVFormat) constructor.newInstance(
                ',', // delimiter
                '"', // quoteChar
                quoteModeAllNonNull,
                commentMarker, // commentMarker
                null, // escape
                false, // ignoreSurroundingSpaces
                true,  // ignoreEmptyLines
                "\r\n", // recordSeparator
                null,   // nullString
                null,   // headerComments (Object[])
                null,   // header (String[])
                false,  // skipHeaderRecord
                false,  // allowMissingColumnNames
                false,  // ignoreHeaderCase
                false,  // trim
                false,  // trailingDelimiter
                false   // autoFlush
        );
    }
}