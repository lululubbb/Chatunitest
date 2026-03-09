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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormat_25_1Test {

    @Test
    @Timeout(8000)
    void testHashCode_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = calculateExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsNullOrFalse() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                (char) 0,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                null,
                null
        );
        int expected = calculateExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsSet() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ';',
                QuoteMode.ALL_NON_NULL,
                Character.valueOf('Q'),
                Character.valueOf('#'),
                Character.valueOf('\\'),
                "NULL",
                true,
                true,
                true,
                true,
                "\n",
                new String[]{"header1", "header2"}
        );
        int expected = calculateExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_HeaderEmptyArray() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ',',
                QuoteMode.MINIMAL,
                Character.valueOf('"'),
                Character.valueOf('!'),
                Character.valueOf('\\'),
                "null",
                true,
                false,
                true,
                false,
                "\r\n",
                new String[0]
        );
        int expected = calculateExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    private CSVFormat createCSVFormatWithFields(char delimiter,
                                                QuoteMode quoteMode,
                                                Character quoteCharacter,
                                                Character commentMarker,
                                                Character escapeCharacter,
                                                String nullString,
                                                boolean ignoreSurroundingSpaces,
                                                boolean ignoreHeaderCase,
                                                boolean ignoreEmptyLines,
                                                boolean skipHeaderRecord,
                                                String recordSeparator,
                                                String[] header) throws Exception {
        // Create a new instance via the public constructor using reflection
        // since CSVFormat is final and fields are final, we cannot simply modify DEFAULT instance
        // We find the constructor with all fields needed for hashCode
        // Constructor parameters:
        // char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush

        // We can pass null or defaults for unused parameters

        Class<CSVFormat> clazz = CSVFormat.class;
        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class
        );
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                null,
                header,
                skipHeaderRecord,
                false, // allowMissingColumnNames
                ignoreHeaderCase,
                false, // trim
                false, // trailingDelimiter
                false  // autoFlush
        );

        return format;
    }

    private int calculateExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;

        result = prime * result + format.getDelimiter();
        result = prime * result + ((format.getQuoteMode() == null) ? 0 : format.getQuoteMode().hashCode());
        result = prime * result + ((format.getQuoteCharacter() == null) ? 0 : format.getQuoteCharacter().hashCode());
        result = prime * result + ((format.getCommentMarker() == null) ? 0 : format.getCommentMarker().hashCode());
        result = prime * result + ((format.getEscapeCharacter() == null) ? 0 : format.getEscapeCharacter().hashCode());
        result = prime * result + ((format.getNullString() == null) ? 0 : format.getNullString().hashCode());
        result = prime * result + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreHeaderCase() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        result = prime * result + (format.getSkipHeaderRecord() ? 1231 : 1237);
        result = prime * result + ((format.getRecordSeparator() == null) ? 0 : format.getRecordSeparator().hashCode());
        result = prime * result + Arrays.hashCode(format.getHeader());

        return result;
    }
}