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
    void testHashCode_defaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_customValues() throws Exception {
        // Create CSVFormat instance with custom values using reflection since constructor is private
        CSVFormat format = createCSVFormatInstance(
                ';', // delimiter
                '\"', // quoteCharacter
                QuoteMode.ALL, // quoteMode
                '#', // commentMarker
                '\\', // escapeCharacter
                true, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                "\n", // recordSeparator
                "NULL", // nullString
                new String[]{"header1", "header2"}, // header
                true, // ignoreHeaderCase
                true, // skipHeaderRecord
                false, // allowMissingColumnNames
                true, // trailingDelimiter
                true // trim
        );
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullFields() throws Exception {
        CSVFormat format = createCSVFormatInstance(
                ',', // delimiter
                null, // quoteCharacter
                null, // quoteMode
                null, // commentMarker
                null, // escapeCharacter
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                null, // recordSeparator
                null, // nullString
                null, // header
                false, // ignoreHeaderCase
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false, // trailingDelimiter
                false // trim
        );
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    // Helper method to compute expected hash code the same way as CSVFormat.hashCode()
    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;
        result = prime * result + format.getDelimiter();
        result = prime * result + (format.getQuoteMode() == null ? 0 : format.getQuoteMode().hashCode());
        result = prime * result + (format.getQuoteCharacter() == null ? 0 : format.getQuoteCharacter().hashCode());
        result = prime * result + (format.getCommentMarker() == null ? 0 : format.getCommentMarker().hashCode());
        result = prime * result + (format.getEscapeCharacter() == null ? 0 : format.getEscapeCharacter().hashCode());
        result = prime * result + (format.getNullString() == null ? 0 : format.getNullString().hashCode());
        result = prime * result + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreHeaderCase() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        result = prime * result + (format.getSkipHeaderRecord() ? 1231 : 1237);
        result = prime * result + (format.getRecordSeparator() == null ? 0 : format.getRecordSeparator().hashCode());
        result = prime * result + Arrays.hashCode(format.getHeader());
        return result;
    }

    // Helper method to create CSVFormat instance with reflection because constructor is private
    private CSVFormat createCSVFormatInstance(
            char delimiter,
            Character quoteCharacter,
            QuoteMode quoteMode,
            Character commentMarker,
            Character escapeCharacter,
            boolean ignoreSurroundingSpaces,
            boolean ignoreEmptyLines,
            String recordSeparator,
            String nullString,
            String[] header,
            boolean ignoreHeaderCase,
            boolean skipHeaderRecord,
            boolean allowMissingColumnNames,
            boolean trailingDelimiter,
            boolean trim
    ) throws Exception {
        // Constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
        // boolean trailingDelimiter, boolean autoFlush)

        Class<CSVFormat> clazz = CSVFormat.class;
        var ctor = clazz.getDeclaredConstructors();
        // Find the constructor with 17 params (the private one)
        java.lang.reflect.Constructor<CSVFormat> constructor = null;
        for (var c : ctor) {
            if (c.getParameterCount() == 17) {
                constructor = (java.lang.reflect.Constructor<CSVFormat>) c;
                break;
            }
        }
        if (constructor == null) {
            throw new NoSuchMethodException("CSVFormat constructor with 17 params not found");
        }
        constructor.setAccessible(true);

        Object[] headerComments = null; // pass null for headerComments
        boolean autoFlush = false; // default false for autoFlush

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
}