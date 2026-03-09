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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_20_6Test {

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator() {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);

        // When
        String recordSeparator = csvFormat.getRecordSeparator();

        // Then
        assertEquals("\r\n", recordSeparator);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                      Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                      Object[] headerComments, String[] header, boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
                                      boolean trailingDelimiter) {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                    Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                    boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                    ignoreEmptyLines, recordSeparator, nullString, headerComments, header, skipHeaderRecord,
                    allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}