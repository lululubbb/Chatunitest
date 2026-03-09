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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private File tempFile;

    @BeforeEach
    void setup() throws IOException {
        csvFormat = CSVFormat.DEFAULT;
        tempFile = File.createTempFile("csvformattest", ".csv");
        tempFile.deleteOnExit();
    }

    @Test
    @Timeout(8000)
    void testPrint_withValidFileAndCharset() throws IOException {
        Charset charset = Charset.forName("UTF-8");
        CSVPrinter printer = csvFormat.print(tempFile.toPath(), charset);
        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrint_withDifferentCharset() throws IOException {
        Charset charset = Charset.forName("ISO-8859-1");
        CSVPrinter printer = csvFormat.print(tempFile.toPath(), charset);
        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrint_withCustomCSVFormat() throws IOException {
        CSVFormat customFormat = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'').withRecordSeparator("\n");
        CSVPrinter printer = customFormat.print(tempFile.toPath(), Charset.defaultCharset());
        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrint_throwsIOExceptionWhenFileCannotBeWritten() throws Exception {
        File readOnlyFile = File.createTempFile("readonly", ".csv");
        readOnlyFile.deleteOnExit();
        boolean setReadOnly = readOnlyFile.setWritable(false, false);
        assertTrue(setReadOnly, "Failed to set file to read-only, test may not behave as expected");

        try {
            csvFormat.print(readOnlyFile.toPath(), Charset.defaultCharset());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected exception
        } finally {
            // Reset file writable to allow deletion on exit
            readOnlyFile.setWritable(true, false);
        }
    }
}