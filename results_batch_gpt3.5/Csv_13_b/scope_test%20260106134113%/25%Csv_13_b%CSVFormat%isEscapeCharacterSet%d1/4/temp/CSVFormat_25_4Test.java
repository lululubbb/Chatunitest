package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_25_4Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        // DEFAULT has escapeCharacter == null
        assertFalse(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsSet() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsSetToNullCharacter() throws Exception {
        // Find the QuoteMode inner class
        Class<?> quoteModeClass = null;
        for (Class<?> innerClass : CSVFormat.class.getDeclaredClasses()) {
            if ("QuoteMode".equals(innerClass.getSimpleName())) {
                quoteModeClass = innerClass;
                break;
            }
        }
        assertNotNull(quoteModeClass, "QuoteMode class not found");

        // Get the private constructor with correct parameter types
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteModeClass,
                Character.class, Character.class,
                boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Prepare parameters for constructor invocation
        char delimiter = CSVFormat.DEFAULT.getDelimiter();
        Character quoteChar = CSVFormat.DEFAULT.getQuoteCharacter();
        Object quoteMode = CSVFormat.DEFAULT.getQuoteMode();
        Character commentMarker = CSVFormat.DEFAULT.getCommentMarker();
        Character escapeCharacter = '\0'; // null character explicitly set
        boolean ignoreSurroundingSpaces = CSVFormat.DEFAULT.getIgnoreSurroundingSpaces();
        boolean ignoreEmptyLines = CSVFormat.DEFAULT.getIgnoreEmptyLines();
        String recordSeparator = CSVFormat.DEFAULT.getRecordSeparator();
        String nullString = CSVFormat.DEFAULT.getNullString();
        Object[] headerComments = null; // null for headerComments
        String[] header = CSVFormat.DEFAULT.getHeader();
        boolean skipHeaderRecord = CSVFormat.DEFAULT.getSkipHeaderRecord();
        boolean allowMissingColumnNames = CSVFormat.DEFAULT.getAllowMissingColumnNames();
        boolean ignoreHeaderCase = CSVFormat.DEFAULT.getIgnoreHeaderCase();

        CSVFormat format = constructor.newInstance(
                delimiter,
                quoteChar,
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
                ignoreHeaderCase
        );

        assertTrue(format.isEscapeCharacterSet());
    }
}