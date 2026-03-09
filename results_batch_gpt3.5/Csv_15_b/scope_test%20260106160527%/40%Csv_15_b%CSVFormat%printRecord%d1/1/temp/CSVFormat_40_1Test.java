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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatPrintRecordTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void printRecord_WithMultipleValues_AppendsAllValuesAndNewline() throws IOException {
        Appendable out = new StringBuilder();
        Object[] values = {"value1", 123, null, "value4"};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        // The exact output depends on print method implementation, but should contain all values and end with record separator
        for (Object val : values) {
            if (val != null) {
                assertTrue(result.contains(val.toString()));
            }
        }
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void printRecord_WithEmptyValues_AppendsOnlyNewline() throws IOException {
        Appendable out = new StringBuilder();
        Object[] values = {};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void printRecord_WithSingleValue_AppendsValueAndNewline() throws IOException {
        Appendable out = new StringBuilder();
        Object[] values = {"singleValue"};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        assertTrue(result.startsWith("singleValue"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void printRecord_WithNullValue_AppendsNullStringAndNewline() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat formatWithNullString = csvFormat.withNullString("NULL");
        Object[] values = {null};

        formatWithNullString.printRecord(out, values);

        String result = out.toString();
        assertTrue(result.startsWith("NULL"));
        assertTrue(result.endsWith(formatWithNullString.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void printRecord_WithAppendableThrowingIOException_PropagatesException() {
        Appendable out = mock(Appendable.class);
        Object[] values = {"value"};

        try {
            doThrow(new IOException("Test IOException")).when(out).append(any(CharSequence.class));
            assertThrows(IOException.class, () -> csvFormat.printRecord(out, values));
        } catch (IOException e) {
            fail("Setup of mock threw IOException");
        }
    }

    @Test
    @Timeout(8000)
    void printRecord_UsesPrintMethodWithCorrectNewRecordFlag() throws Exception {
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        Appendable out = new StringBuilder();
        Object[] values = {"v1", "v2", "v3"};

        // Use reflection to call the public printRecord method to avoid infinite recursion in spy
        Method printRecordMethod = CSVFormat.class.getMethod("printRecord", Appendable.class, Object[].class);
        printRecordMethod.invoke(spyFormat, out, (Object) values);

        for (int i = 0; i < values.length; i++) {
            verify(spyFormat).print(eq(values[i]), eq(out), eq(i == 0));
        }
        verify(spyFormat).println(eq(out));
    }
}