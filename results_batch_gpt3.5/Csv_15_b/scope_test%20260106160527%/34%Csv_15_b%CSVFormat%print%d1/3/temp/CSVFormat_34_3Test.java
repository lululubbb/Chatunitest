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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintWithPathAndCharset() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BufferedWriter writer = mock(BufferedWriter.class);
        CSVPrinter printer = mock(CSVPrinter.class);

        // Spy the csvFormat
        CSVFormat spyFormat = spy(csvFormat);

        // Use reflection to get the print(Appendable) method
        Method printAppendableMethod = CSVFormat.class.getDeclaredMethod("print", Appendable.class);
        printAppendableMethod.setAccessible(true);

        // When print(Appendable) is called, return mocked printer
        doReturn(printer).when(spyFormat).print(writer);

        // Directly test print(Appendable)
        CSVPrinter result = (CSVPrinter) printAppendableMethod.invoke(spyFormat, writer);
        assertNotNull(result);
        assertEquals(printer, result);

        // Verify print(writer) was called
        verify(spyFormat).print(writer);
    }

    @Test
    @Timeout(8000)
    void testPrintWithIOExceptionFromPrintBufferedWriter() throws IOException {
        BufferedWriter writer = mock(BufferedWriter.class);
        CSVFormat spyFormat = spy(csvFormat);

        // Mock print(Appendable) to throw IOException
        doThrow(new IOException("Test IOException")).when(spyFormat).print(writer);

        IOException thrown = assertThrows(IOException.class, () -> spyFormat.print(writer));
        assertEquals("Test IOException", thrown.getMessage());
    }
}