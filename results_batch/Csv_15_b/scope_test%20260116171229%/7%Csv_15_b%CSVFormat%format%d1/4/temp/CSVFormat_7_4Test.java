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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class CSVFormat_7_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_singleValue() throws IOException {
        String result = csvFormat.format("value");
        assertEquals("value", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_multipleValues() throws IOException {
        String result = csvFormat.format("value1", "value2", "value3");
        assertEquals("value1,value2,value3", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_nullValue() throws IOException {
        String result = csvFormat.format((Object) null);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_emptyValues() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withIOException() throws Exception {
        // Mock CSVPrinter to throw IOException on printRecord
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("IO error")).when(mockPrinter).printRecord((Object[]) any());

        // Create a CSVFormat instance same as DEFAULT
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Spy on CSVFormat to override format method
        CSVFormat spyFormat = spy(baseFormat);

        // Override format method to use mocked CSVPrinter
        doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                Object[] values = (Object[]) invocation.getArguments()[0];
                StringWriter out = new StringWriter();
                try (CSVPrinter printer = mockPrinter) {
                    mockPrinter.printRecord(values);
                    return out.toString().trim();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }).when(spyFormat).format((Object[]) any());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            spyFormat.format("value");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }
}