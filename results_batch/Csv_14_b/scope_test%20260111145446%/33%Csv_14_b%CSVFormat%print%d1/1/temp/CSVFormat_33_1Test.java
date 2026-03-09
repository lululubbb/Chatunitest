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

import org.junit.jupiter.api.Test;

public class CSVFormat_33_1Test {

    @Test
    @Timeout(8000)
    public void testPrintWithNonNullValue() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = "testValue";
        StringWriter out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, newRecord);

        // Then
        assertEquals("testValue", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNullValue() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = null;
        StringWriter out = new StringWriter();
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
        Object value = new StringBuilder("charSequenceValue");
        StringWriter out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, newRecord);

        // Then
        assertEquals("charSequenceValue", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintTrimmedValue() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = "  trimmedValue  ";
        StringWriter out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, newRecord);

        // Then
        assertEquals("trimmedValue", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintWithOffsetAndLength() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = "offsetLengthValue";
        StringWriter out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, 3, 6, newRecord);

        // Then
        assertEquals("offset", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNegativeOffset() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = "negativeOffset";
        StringWriter out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, -5, 5, newRecord);

        // Then
        assertEquals("negat", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNegativeLength() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object value = "negativeLength";
        StringWriter out = new StringWriter();
        boolean newRecord = true;

        // When
        csvFormat.print(value, out, 0, -5, newRecord);

        // Then
        assertEquals("negative", out.toString());
    }
}