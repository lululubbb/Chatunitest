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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CSVFormat_30_5Test {

    @Test
    @Timeout(8000)
    void testPrint() throws IOException {
        // Given
        Appendable out = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, csvFormat);

        // When
        CSVPrinter result = csvFormat.print(out);

        // Then
        assertNotNull(result);
        assertEquals(csvPrinter.getClass(), result.getClass());
    }

    @Test
    @Timeout(8000)
    void testPrintWithIOException() {
        // Given
        Appendable out = mock(Appendable.class);
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        try {
            // When
            Mockito.doThrow(new IOException()).when(out).append(Mockito.any(CharSequence.class));
            csvFormat.print(out);
            fail("IOException expected");
        } catch (IOException e) {
            // Then
            assertTrue(true);
        }
    }
}