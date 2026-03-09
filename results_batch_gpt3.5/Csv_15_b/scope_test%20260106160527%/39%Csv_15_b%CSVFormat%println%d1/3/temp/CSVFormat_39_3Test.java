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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVFormatPrintlnTest {

    private CSVFormat csvFormatTrailing;
    private CSVFormat csvFormatNoTrailing;
    private Appendable out;

    @BeforeEach
    void setUp() throws Exception {
        // Create CSVFormat instance with trailingDelimiter = true, recordSeparator = "\n"
        csvFormatTrailing = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("\n");
        // Create CSVFormat instance with trailingDelimiter = false, recordSeparator = null
        csvFormatNoTrailing = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator(null);
        out = mock(Appendable.class);

        // Use reflection to set the private final field 'trailingDelimiter' and 'recordSeparator'
        setFinalField(csvFormatTrailing, "trailingDelimiter", true);
        setFinalField(csvFormatTrailing, "recordSeparator", "\n");

        setFinalField(csvFormatNoTrailing, "trailingDelimiter", false);
        setFinalField(csvFormatNoTrailing, "recordSeparator", null);
    }

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testPrintln_withTrailingDelimiterAndRecordSeparator() throws IOException {
        csvFormatTrailing.println(out);

        InOrder inOrder = inOrder(out);
        // First append delimiter
        inOrder.verify(out).append(String.valueOf(csvFormatTrailing.getDelimiter()));
        // Then append recordSeparator
        inOrder.verify(out).append(csvFormatTrailing.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintln_withTrailingDelimiterOnly() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator(null);

        setFinalField(format, "trailingDelimiter", true);
        setFinalField(format, "recordSeparator", null);

        format.println(out);

        verify(out).append(String.valueOf(format.getDelimiter()));
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintln_withRecordSeparatorOnly() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator("\r\n");

        setFinalField(format, "trailingDelimiter", false);
        setFinalField(format, "recordSeparator", "\r\n");

        format.println(out);

        verify(out).append(format.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintln_withNoTrailingDelimiterNoRecordSeparator() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator(null);

        setFinalField(format, "trailingDelimiter", false);
        setFinalField(format, "recordSeparator", null);

        format.println(out);

        verify(out, never()).append(any(CharSequence.class));
        verifyNoMoreInteractions(out);
    }
}