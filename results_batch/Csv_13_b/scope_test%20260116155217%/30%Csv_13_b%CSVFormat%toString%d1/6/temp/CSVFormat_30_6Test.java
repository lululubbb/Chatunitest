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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.util.Arrays;

class CSVFormat_30_6Test {

    @Test
    @Timeout(8000)
    void testToString_AllFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                ';',
                '\"',
                QuoteMode.MINIMAL,
                '#',
                '\\',
                true,
                true,
                "\r\n",
                "NULL",
                new String[]{"comment1", "comment2"},
                new String[]{"header1", "header2"},
                true,
                true,
                true
        );

        String toStringResult = format.toString();

        assertTrue(toStringResult.contains("Delimiter=<;>"));
        assertTrue(toStringResult.contains("Escape=<\\>"));
        assertTrue(toStringResult.contains("QuoteChar=<\">"));
        assertTrue(toStringResult.contains("CommentStart=<#>"));
        assertTrue(toStringResult.contains("NullString=<NULL>"));
        assertTrue(toStringResult.contains("RecordSeparator=<\r\n>"));
        assertTrue(toStringResult.contains("EmptyLines:ignored"));
        assertTrue(toStringResult.contains(" SurroundingSpaces:ignored"));
        assertTrue(toStringResult.contains(" IgnoreHeaderCase:ignored"));
        assertTrue(toStringResult.contains("SkipHeaderRecord:true"));
        assertTrue(toStringResult.contains("HeaderComments:[comment1, comment2]"));
        assertTrue(toStringResult.contains("Header:[header1, header2]"));
    }

    @Test
    @Timeout(8000)
    void testToString_NoOptionalFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                null,
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false
        );

        String toStringResult = format.toString();

        assertEquals("Delimiter=<,> SkipHeaderRecord:false", toStringResult);
    }

    @Test
    @Timeout(8000)
    void testToString_SomeFieldsSet() throws Exception {
        CSVFormat format = createCSVFormat(
                ',',
                '\"',
                QuoteMode.MINIMAL,
                null,
                null,
                true,
                true,
                "\n",
                null,
                null,
                new String[]{"headerA"},
                false,
                false,
                false
        );

        String toStringResult = format.toString();

        assertTrue(toStringResult.contains("Delimiter=<,>"));
        assertTrue(toStringResult.contains("QuoteChar=<\">"));
        assertTrue(toStringResult.contains("RecordSeparator=<\n>"));
        assertTrue(toStringResult.contains("EmptyLines:ignored"));
        assertTrue(toStringResult.contains(" SurroundingSpaces:ignored"));
        assertTrue(toStringResult.contains("Header:[headerA]"));
        assertTrue(toStringResult.contains("SkipHeaderRecord:false"));
    }

    private CSVFormat createCSVFormat(
            char delimiter,
            Character quoteCharacter,
            QuoteMode quoteMode,
            Character commentMarker,
            Character escapeCharacter,
            boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines,
            String recordSeparator,
            String nullString,
            String[] headerComments,
            String[] header,
            boolean skipHeaderRecord,
            boolean allowMissingColumnNames,
            boolean ignoreHeaderCase) throws Exception {

        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class
        );
        constructor.setAccessible(true);

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
                ignoreHeaderCase
        );
    }
}