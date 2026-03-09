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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class CSVFormat_37_2Test {

    private Appendable out;

    @BeforeEach
    public void setUp() {
        out = mock(Appendable.class);
    }

    private CSVFormat setTrailingDelimiterAndRecordSeparator(CSVFormat format, boolean trailingDelimiter, String recordSeparator) throws Exception {
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(format);

        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharacter = (Character) quoteCharacterField.get(format);

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        Object quoteMode = quoteModeField.get(format);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(format);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(format);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(format);

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(format);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(format);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(format);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(format);

        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = trimField.getBoolean(format);

        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        return ctor.newInstance(
                delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testPrintln_WithTrailingDelimiterAndRecordSeparator() throws Exception {
        CSVFormat format = setTrailingDelimiterAndRecordSeparator(CSVFormat.DEFAULT, true, "\n");
        format.println(out);
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(format.getDelimiter());
        inOrder.verify(out).append(format.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintln_WithTrailingDelimiterOnly() throws Exception {
        CSVFormat format = setTrailingDelimiterAndRecordSeparator(CSVFormat.DEFAULT, true, null);
        format.println(out);
        verify(out).append(format.getDelimiter());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintln_WithRecordSeparatorOnly() throws Exception {
        CSVFormat format = setTrailingDelimiterAndRecordSeparator(CSVFormat.DEFAULT, false, "\r\n");
        format.println(out);
        verify(out).append(format.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintln_WithNeitherTrailingDelimiterNorRecordSeparator() throws Exception {
        CSVFormat format = setTrailingDelimiterAndRecordSeparator(CSVFormat.DEFAULT, false, null);
        format.println(out);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintln_AppendableThrowsIOException() throws Exception {
        CSVFormat format = setTrailingDelimiterAndRecordSeparator(CSVFormat.DEFAULT, true, "\n");
        doThrow(new IOException("append failed")).when(out).append(any(CharSequence.class));
        IOException thrown = assertThrows(IOException.class, () -> format.println(out));
        assertEquals("append failed", thrown.getMessage());
    }
}