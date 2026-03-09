package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CSVFormat_46_4Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        QuoteMode quoteMode = null;
        Character commentStart = null;
        Character escape = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        Object[] headerComments = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = false;
        boolean trim = false;
        boolean trailingDelimiter = false;

        CSVFormat csvFormat = createCSVFormatInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);

        char newCommentMarker = '#';

        // When
        CSVFormat result = csvFormat.withCommentMarker(newCommentMarker);

        // Then
        assertEquals(newCommentMarker, result.getCommentMarker());
    }

    // Helper method to create a CSVFormat instance for testing private methods
    private CSVFormat createCSVFormatInstance(char delimiter, char quoteChar, QuoteMode quoteMode, Character commentStart,
                                              Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                              String recordSeparator, String nullString, Object[] headerComments, String[] header,
                                              boolean skipHeaderRecord, boolean allowMissingColumnNames, boolean ignoreHeaderCase,
                                              boolean trim, boolean trailingDelimiter) {
        try {
            return CSVFormat.class.getDeclaredConstructor(char.class, char.class, QuoteMode.class, Character.class, Character.class,
                    boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                    boolean.class, boolean.class, boolean.class, boolean.class, boolean.class)
                    .newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                            recordSeparator, nullString, headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                            ignoreHeaderCase, trim, trailingDelimiter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}