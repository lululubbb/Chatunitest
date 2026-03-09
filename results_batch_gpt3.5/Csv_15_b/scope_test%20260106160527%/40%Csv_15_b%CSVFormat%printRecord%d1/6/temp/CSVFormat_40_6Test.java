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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVFormatPrintRecordTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_noValues() throws IOException {
        csvFormat.printRecord(out);
        // Should not append anything because no values
        verify(out, times(0)).append(anyString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_singleValue() throws Throwable {
        // Spy on csvFormat to verify internal print call
        CSVFormat spyFormat = spy(csvFormat);
        Appendable appendable = mock(Appendable.class);

        // Use reflection to get the print method with signature:
        // void print(Object value, Appendable out, boolean newRecord)
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Use reflection to get the println method:
        Method printlnMethod = CSVFormat.class.getDeclaredMethod("println", Appendable.class);
        printlnMethod.setAccessible(true);

        // Call printRecord normally
        spyFormat.printRecord(appendable, "value1");

        // Verify that internal print(Object, Appendable, boolean) was called with correct args
        verify(spyFormat, times(1)).print("value1", appendable, true);
        verify(spyFormat, times(1)).println(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_multipleValues() throws Throwable {
        CSVFormat spyFormat = spy(csvFormat);
        Appendable appendable = mock(Appendable.class);

        Object[] values = new Object[] {"val1", 2, null};

        spyFormat.printRecord(appendable, values);

        InOrder inOrder = inOrder(spyFormat);
        inOrder.verify(spyFormat).print("val1", appendable, true);
        inOrder.verify(spyFormat).print(2, appendable, false);
        inOrder.verify(spyFormat).print(null, appendable, false);
        inOrder.verify(spyFormat).println(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectionInvoke() throws Exception {
        Appendable appendable = mock(Appendable.class);
        Object[] values = new Object[] {"a", "b"};

        Method printRecordMethod = CSVFormat.class.getDeclaredMethod("printRecord", Appendable.class, Object[].class);
        printRecordMethod.setAccessible(true);

        // When invoking varargs via reflection, wrap values inside an Object[]
        printRecordMethod.invoke(csvFormat, appendable, (Object) values);

        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }
}