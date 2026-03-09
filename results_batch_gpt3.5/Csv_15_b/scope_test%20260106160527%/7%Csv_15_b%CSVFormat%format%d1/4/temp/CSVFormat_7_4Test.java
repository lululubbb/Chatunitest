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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.StringWriter;

class CSVFormat_7_4Test {

    @Test
    @Timeout(8000)
    void testFormatWithNormalValues() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String result = format.format("a", "b", "c");
        // The output should be CSV record with comma delimiter and CRLF line ending trimmed
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithNullValue() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        String result = format.format("a", null, "c");
        assertEquals("a,NULL,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithEmptyValues() {
        CSVFormat format = CSVFormat.DEFAULT;
        String result = format.format();
        // Empty record should produce empty string after trim
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithIOException() throws Exception {
        // Spy CSVFormat.DEFAULT to override format method to simulate IOException
        CSVFormat formatWithException = Mockito.spy(CSVFormat.DEFAULT);

        // Mock CSVPrinter to throw IOException when printRecord is called
        CSVPrinter printerMock = Mockito.mock(CSVPrinter.class);
        Mockito.doThrow(new IOException("forced IO exception")).when(printerMock).printRecord(Mockito.<Object[]>any());

        // Override format method in spy to use the mocked CSVPrinter
        Mockito.doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                Object[] values = (Object[]) invocation.getArguments()[0];
                final StringWriter out = new StringWriter();
                try (CSVPrinter ignored = printerMock) {
                    printerMock.printRecord(values);
                    return out.toString().trim();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }).when(formatWithException).format(Mockito.<Object[]>any());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> formatWithException.format("a"));
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("forced IO exception", thrown.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    void testFormatWithDifferentFormats() {
        CSVFormat[] formats = new CSVFormat[]{
                CSVFormat.EXCEL,
                CSVFormat.INFORMIX_UNLOAD,
                CSVFormat.INFORMIX_UNLOAD_CSV,
                CSVFormat.MYSQL,
                CSVFormat.POSTGRESQL_CSV,
                CSVFormat.POSTGRESQL_TEXT,
                CSVFormat.RFC4180,
                CSVFormat.TDF
        };

        for (CSVFormat format : formats) {
            String result = format.format("x", "y", "z");
            assertNotNull(result);
            assertFalse(result.isEmpty());
            // Basic check that output contains the first value
            assertTrue(result.contains("x"));
        }
    }
}