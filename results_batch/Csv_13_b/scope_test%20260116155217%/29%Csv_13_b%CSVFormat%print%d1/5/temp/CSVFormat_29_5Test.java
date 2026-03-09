package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_29_5Test {

    private Appendable appendable;

    @BeforeEach
    void setUp() {
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.print(appendable);
        assertNotNull(printer);

        // Check printer.getOut()
        assertEquals(appendable, printer.getOut());

        // Use reflection to get the 'format' field from CSVPrinter
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat printerFormat = (CSVFormat) formatField.get(printer);

        assertEquals(format, printerFormat);
    }

    @Test
    @Timeout(8000)
    void testPrintWithNullAppendableThrowsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(NullPointerException.class, () -> format.print(null));
    }

}