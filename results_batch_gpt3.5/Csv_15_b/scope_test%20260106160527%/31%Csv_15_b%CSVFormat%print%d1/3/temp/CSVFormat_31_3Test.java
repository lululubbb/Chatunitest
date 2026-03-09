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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_31_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        // Use reflection to access the private 'out' field of CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object internalOut = outField.get(printer);

        // Unwrap BufferedWriter if present (BufferedWriter is in java.io package)
        Class<?> bufferedWriterClass = null;
        try {
            bufferedWriterClass = Class.forName("java.io.BufferedWriter");
        } catch (ClassNotFoundException e) {
            // Ignore if not found
        }
        if (bufferedWriterClass != null && bufferedWriterClass.isInstance(internalOut)) {
            Field outFieldInBufferedWriter = bufferedWriterClass.getDeclaredField("out");
            outFieldInBufferedWriter.setAccessible(true);
            internalOut = outFieldInBufferedWriter.get(internalOut);
        }

        // Unwrap FilterWriter if present (BufferedWriter extends FilterWriter)
        Class<?> filterWriterClass = null;
        try {
            filterWriterClass = Class.forName("java.io.FilterWriter");
        } catch (ClassNotFoundException e) {
            // Ignore if not found
        }
        if (filterWriterClass != null && filterWriterClass.isInstance(internalOut)) {
            Field outFieldInFilterWriter = filterWriterClass.getDeclaredField("out");
            outFieldInFilterWriter.setAccessible(true);
            internalOut = outFieldInFilterWriter.get(internalOut);
        }

        // Adjusted assertion: the internal 'out' field of CSVPrinter wraps the original Appendable,
        // but the wrapped object may not be the same instance due to internal wrapping.
        // Instead, we check that the internalOut is the same as the original appendable mock.
        assertSame(appendable, internalOut);
    }

    @Test
    @Timeout(8000)
    void testPrintWithNullAppendableThrowsException() {
        assertThrows(NullPointerException.class, () -> csvFormat.print(null));
    }
}