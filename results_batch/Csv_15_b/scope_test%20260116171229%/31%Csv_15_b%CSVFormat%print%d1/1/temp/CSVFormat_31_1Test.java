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

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to get CSVFormat.DEFAULT and create a new instance with autoFlush = true
        Field defaultField = CSVFormat.class.getField("DEFAULT");
        CSVFormat defaultFormat = (CSVFormat) defaultField.get(null);

        // Since CSVFormat is immutable, use withAutoFlush(true) to get a new instance
        csvFormat = defaultFormat.withAutoFlush(true);
    }

    @Test
    @Timeout(8000)
    void testPrint_returnsCSVPrinter() throws IOException {
        StringBuilder appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        assertEquals(appendable, printer.getOut());
    }

    @Test
    @Timeout(8000)
    void testPrint_withMockAppendable() throws IOException {
        Appendable mockAppendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(mockAppendable);
        assertNotNull(printer);
        assertEquals(mockAppendable, printer.getOut());
    }

}