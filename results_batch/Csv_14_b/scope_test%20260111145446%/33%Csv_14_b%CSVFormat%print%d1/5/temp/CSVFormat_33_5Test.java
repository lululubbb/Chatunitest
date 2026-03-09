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
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_33_5Test {

    @Test
    @Timeout(8000)
    public void testPrint() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = "Test";
        Appendable out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, newRecord);

        // Then
        assertEquals("Test", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNullValue() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = null;
        Appendable out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, newRecord);

        // Then
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintWithCharSequenceValue() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = new StringBuilder("Hello");
        Appendable out = new StringWriter();
        boolean newRecord = false;

        // When
        csvFormat.print(value, out, newRecord);

        // Then
        assertEquals("Hello", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintWithTrim() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = "   Trimmed   ";
        Appendable out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, newRecord);

        // Then
        assertEquals("Trimmed", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CharSequence value = "Escaped, Value";
        Appendable out = new StringWriter();

        // When
        assertDoesNotThrow(() -> {
            csvFormat.printAndEscape(value, 0, value.length(), out);
        });
    }

    @Test
    @Timeout(8000)
    public void testPrintAndQuote() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = "Quoted Value";
        Appendable out = new StringWriter();
        boolean newRecord = true;

        // When
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(csvFormat, value, (CharSequence) value, 0, value.toString().length(), out, newRecord);

        // Then
        assertEquals("\"Quoted Value\"", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Appendable out = new StringWriter();
        Object[] values = {"Record", "Values"};

        // When
        csvFormat.printRecord(out, values);

        // Then
        assertEquals("Record,Values", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintln() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Appendable out = new StringWriter();

        // When
        csvFormat.println(out);

        // Then
        assertEquals(System.lineSeparator(), out.toString());
    }

}