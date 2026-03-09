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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_24_5Test {

    @Test
    @Timeout(8000)
    public void testHashCode() {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        Character commentStart = null;
        Character escape = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = true;
        boolean ignoreHeaderCase = false;
        boolean trim = false;
        boolean trailingDelimiter = false;
        QuoteMode quoteMode = QuoteMode.ALL_NON_NULL;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        int hashCode = csvFormat.hashCode();

        // Then
        int expectedHashCode = 31 * 1 + delimiter;
        expectedHashCode = 31 * expectedHashCode + (quoteMode == null ? 0 : quoteMode.hashCode());
        expectedHashCode = 31 * expectedHashCode + (quoteChar == null ? 0 : quoteChar.hashCode());
        expectedHashCode = 31 * expectedHashCode + (commentStart == null ? 0 : commentStart.hashCode());
        expectedHashCode = 31 * expectedHashCode + (escape == null ? 0 : escape.hashCode());
        expectedHashCode = 31 * expectedHashCode + (nullString == null ? 0 : nullString.hashCode());
        expectedHashCode = 31 * expectedHashCode + (ignoreSurroundingSpaces ? 1231 : 1237);
        expectedHashCode = 31 * expectedHashCode + (ignoreHeaderCase ? 1231 : 1237);
        expectedHashCode = 31 * expectedHashCode + (ignoreEmptyLines ? 1231 : 1237);
        expectedHashCode = 31 * expectedHashCode + (skipHeaderRecord ? 1231 : 1237);
        expectedHashCode = 31 * expectedHashCode + (recordSeparator == null ? 0 : recordSeparator.hashCode());
        expectedHashCode = 31 * expectedHashCode + 0; // Arrays.hashCode(null) is not applicable here

        assertEquals(expectedHashCode, hashCode);
    }

    // Add more test cases if needed for optimal coverage

}