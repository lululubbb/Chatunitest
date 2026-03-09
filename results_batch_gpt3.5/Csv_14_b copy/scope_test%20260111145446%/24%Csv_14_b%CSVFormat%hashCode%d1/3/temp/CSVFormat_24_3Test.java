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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;

public class CSVFormat_24_3Test {

    @Test
    @Timeout(8000)
    public void testHashCode() {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        Object quoteMode = null; // Change QuoteMode to Object
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\r\n";
        String nullString = "NULL";
        Object[] headerComments = {"Header comment 1", "Header comment 2"};
        String[] header = {"Header1", "Header2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = true;
        boolean trim = false;
        boolean trailingDelimiter = true;

        try {
            CSVFormat csvFormat = (CSVFormat) CSVFormat.class.getDeclaredConstructors()[0].newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                    skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);

            // When
            int hashCode = csvFormat.hashCode();

            // Then
            assertEquals(31 * 1 + delimiter, hashCode);
            assertEquals(31 * hashCode + (quoteMode == null ? 0 : quoteMode.hashCode()), hashCode);
            assertEquals(31 * hashCode + (quoteChar == null ? 0 : quoteChar.hashCode()), hashCode);
            assertEquals(31 * hashCode + (commentStart == null ? 0 : commentStart.hashCode()), hashCode);
            assertEquals(31 * hashCode + (escape == null ? 0 : escape.hashCode()), hashCode);
            assertEquals(31 * hashCode + (nullString == null ? 0 : nullString.hashCode()), hashCode);
            assertEquals(31 * hashCode + (ignoreSurroundingSpaces ? 1231 : 1237), hashCode);
            assertEquals(31 * hashCode + (ignoreHeaderCase ? 1231 : 1237), hashCode);
            assertEquals(31 * hashCode + (ignoreEmptyLines ? 1231 : 1237), hashCode);
            assertEquals(31 * hashCode + (skipHeaderRecord ? 1231 : 1237), hashCode);
            assertEquals(31 * hashCode + (recordSeparator == null ? 0 : recordSeparator.hashCode()), hashCode);
            assertEquals(31 * hashCode + Arrays.hashCode(header), hashCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}