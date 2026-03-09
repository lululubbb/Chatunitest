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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_31_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrintReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        Class<?> printerClass = printer.getClass();
        Field outField = null;
        Field formatField = null;

        while (printerClass != null) {
            try {
                outField = printerClass.getDeclaredField("out");
                formatField = printerClass.getDeclaredField("format");
                break;
            } catch (NoSuchFieldException e) {
                printerClass = printerClass.getSuperclass();
            }
        }

        assertNotNull(outField, "Field 'out' not found in CSVPrinter class hierarchy");
        assertNotNull(formatField, "Field 'format' not found in CSVPrinter class hierarchy");

        outField.setAccessible(true);
        formatField.setAccessible(true);

        Object outValue = outField.get(printer);
        assertSame(appendable, outValue);

        Object formatValue = formatField.get(printer);
        assertSame(csvFormat, formatValue);
    }
}