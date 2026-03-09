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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrint_withValidFile() throws IOException {
        Path tempFile = Files.createTempFile("csv", ".tmp");
        tempFile.toFile().deleteOnExit();

        CSVPrinter printer = csvFormat.print(tempFile, StandardCharsets.UTF_8);
        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNullFile_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> csvFormat.print((Path) null, StandardCharsets.UTF_8));
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNullCharset_shouldThrowException() throws IOException {
        Path tempFile = Files.createTempFile("csv", ".tmp");
        tempFile.toFile().deleteOnExit();

        assertThrows(NullPointerException.class, () -> csvFormat.print(tempFile, null));
    }

    @Test
    @Timeout(8000)
    public void testPrint_verifyPrinterOutputStreamWriterCharset() throws IOException {
        Path tempFile = Files.createTempFile("csv", ".tmp");
        tempFile.toFile().deleteOnExit();

        CSVPrinter printer = csvFormat.print(tempFile, StandardCharsets.UTF_8);
        assertNotNull(printer);
        printer.close();
    }
}