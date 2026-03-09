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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintlnTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        appendable = mock(Appendable.class);
    }

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                      Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                      Object[] headerComments, String[] header, boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
                                      boolean trailingDelimiter) {
        try {
            Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                    boolean.class, boolean.class, String.class, String.class, Object[].class,
                    String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            ctor.setAccessible(true);

            // Defensive null for arrays to avoid null pointer in varargs parameter in reflection
            Object[] safeHeaderComments = headerComments != null ? headerComments : new Object[0];
            String[] safeHeader = header != null ? header : new String[0];

            return ctor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                    safeHeaderComments, safeHeader, skipHeaderRecord, allowMissingColumnNames,
                    ignoreHeaderCase, trim, trailingDelimiter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testPrintln_withTrailingDelimiterAndRecordSeparator() throws IOException {
        csvFormat = createCSVFormat(',', '"', null, null, null, false, false, "\n",
                null, null, null, false, false, false, false, true);

        csvFormat.println(appendable);

        verify(appendable).append(',');
        verify(appendable).append("\n");
    }

    @Test
    @Timeout(8000)
    void testPrintln_withTrailingDelimiterNoRecordSeparator() throws IOException {
        csvFormat = createCSVFormat(',', '"', null, null, null, false, false, null,
                null, null, null, false, false, false, false, true);

        csvFormat.println(appendable);

        verify(appendable).append(',');
        verify(appendable, never()).append(isA(String.class));
    }

    @Test
    @Timeout(8000)
    void testPrintln_noTrailingDelimiterWithRecordSeparator() throws IOException {
        csvFormat = createCSVFormat(',', '"', null, null, null, false, false, "\r\n",
                null, null, null, false, false, false, false, false);

        csvFormat.println(appendable);

        verify(appendable, never()).append(',');
        verify(appendable).append("\r\n");
    }

    @Test
    @Timeout(8000)
    void testPrintln_noTrailingDelimiterNoRecordSeparator() throws IOException {
        csvFormat = createCSVFormat(',', '"', null, null, null, false, false, null,
                null, null, null, false, false, false, false, false);

        csvFormat.println(appendable);

        verify(appendable, never()).append(',');
        verify(appendable, never()).append(anyString());
    }
}