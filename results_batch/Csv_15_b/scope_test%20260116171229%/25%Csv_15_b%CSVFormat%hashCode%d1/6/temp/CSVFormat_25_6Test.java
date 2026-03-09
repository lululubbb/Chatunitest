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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsNullOrDefaults() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ',', // delimiter
                null, // quoteMode
                null, // quoteCharacter
                null, // commentMarker
                null, // escapeCharacter
                null, // nullString
                false, // ignoreSurroundingSpaces
                false, // ignoreHeaderCase
                false, // ignoreEmptyLines
                false, // skipHeaderRecord
                null, // recordSeparator
                null // header
        );
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsNonNullAndMixedBooleans() throws Exception {
        String[] header = new String[] {"a", "b"};
        CSVFormat format = createCSVFormatWithFields(
                ';',
                QuoteMode.ALL_NON_NULL,
                'Q',
                '#',
                '\\',
                "NULL",
                true,
                false,
                true,
                false,
                "\n",
                header
        );
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_HeaderArrayEmpty() throws Exception {
        CSVFormat format = createCSVFormatWithFields(
                ',',
                QuoteMode.MINIMAL,
                '\"',
                '!',
                '\\',
                "null",
                false,
                true,
                false,
                true,
                "\r\n",
                new String[0]
        );
        int expected = computeExpectedHashCode(format);
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
        // Create a new instance via reflection using the constructor
        // since CSVFormat fields are final, modifying them via reflection won't work reliably.
        // The constructor signature is:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush)
        Class<CSVFormat> clazz = CSVFormat.class;
        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        // For fields not set in test, use defaults from CSVFormat.DEFAULT
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // For headerComments, pass null
        Object[] headerComments = null;
        // For allowMissingColumnNames, trim, trailingDelimiter, autoFlush use defaultFormat's getters
        boolean allowMissingColumnNames = defaultFormat.getAllowMissingColumnNames();
        boolean trim = defaultFormat.getTrim();
        boolean trailingDelimiter = defaultFormat.getTrailingDelimiter();
        boolean autoFlush = defaultFormat.getAutoFlush();

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
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
    }

    private int computeExpectedHashCode(CSVFormat format) {
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