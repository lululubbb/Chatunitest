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

class CSVFormatPrintlnTest {

    private Appendable out;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
    }

    private CSVFormat withRecordSeparatorAndTrailingDelimiter(CSVFormat format, String recordSeparator, boolean trailingDelimiter) {
        try {
            // Extract all needed fields from the existing format instance
            char delimiter = format.getDelimiter();
            Character quoteChar = format.getQuoteCharacter();
            QuoteMode quoteMode = format.getQuoteMode();
            Character commentStart = format.getCommentMarker();
            Character escape = format.getEscapeCharacter();
            boolean ignoreSurroundingSpaces = format.getIgnoreSurroundingSpaces();
            boolean ignoreEmptyLines = format.getIgnoreEmptyLines();
            String nullString = format.getNullString();

            Object[] headerComments = null;
            try {
                Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
                headerCommentsField.setAccessible(true);
                headerComments = (Object[]) headerCommentsField.get(format);
            } catch (Exception ignored) {}

            String[] header = format.getHeader();

            boolean skipHeaderRecord = format.getSkipHeaderRecord();
            boolean allowMissingColumnNames = format.getAllowMissingColumnNames();
            boolean ignoreHeaderCase = format.getIgnoreHeaderCase();
            boolean trim = format.getTrim();
            boolean autoFlush = format.getAutoFlush();

            // Find the constructor with the exact parameter types
            Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class,
                    String.class, Object[].class, String[].class, boolean.class, boolean.class,
                    boolean.class, boolean.class, boolean.class, boolean.class);
            ctor.setAccessible(true);

            // Create new instance with updated recordSeparator and trailingDelimiter
            return ctor.newInstance(delimiter, quoteChar, quoteMode,
                    commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                    recordSeparator, nullString, headerComments, header,
                    skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase,
                    trim, trailingDelimiter, autoFlush);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterAndRecordSeparator_appendsDelimiterAndRecordSeparator() throws IOException {
        // Arrange
        CSVFormat format = withRecordSeparatorAndTrailingDelimiter(CSVFormat.DEFAULT, "\n", true);

        // Act
        format.println(out);

        // Assert
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(format.getDelimiter());
        inOrder.verify(out).append("\n");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterNoRecordSeparator_appendsOnlyDelimiter() throws IOException {
        CSVFormat format = withRecordSeparatorAndTrailingDelimiter(CSVFormat.DEFAULT, null, true);

        format.println(out);

        verify(out).append(format.getDelimiter());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_noTrailingDelimiterWithRecordSeparator_appendsOnlyRecordSeparator() throws IOException {
        CSVFormat format = withRecordSeparatorAndTrailingDelimiter(CSVFormat.DEFAULT, "\r\n", false);

        format.println(out);

        verify(out).append("\r\n");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_noTrailingDelimiterNoRecordSeparator_appendsNothing() throws IOException {
        CSVFormat format = withRecordSeparatorAndTrailingDelimiter(CSVFormat.DEFAULT, null, false);

        format.println(out);

        verify(out, never()).append(any());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_throwsIOException_propagatesException() throws IOException {
        CSVFormat format = withRecordSeparatorAndTrailingDelimiter(CSVFormat.DEFAULT, "\n", true);
        doThrow(new IOException("append failed")).when(out).append(any(CharSequence.class));

        IOException thrown = assertThrows(IOException.class, () -> format.println(out));
        assertEquals("append failed", thrown.getMessage());
    }
}