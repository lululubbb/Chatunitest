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
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private final CSVFormat csvFormat = CSVFormat.DEFAULT;
    private final Charset charset = Charset.defaultCharset();

    @Test
    @Timeout(8000)
    void testPrintCreatesCSVPrinter() throws IOException {
        // Create a temporary file instead of mocking File
        Path tempFile = Files.createTempFile("csvformattest", ".csv");
        try {
            CSVPrinter printer = csvFormat.print(tempFile.toFile(), charset);

            assertNotNull(printer);
            assertEquals(CSVPrinter.class, printer.getClass());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsNullPointerException() {
        // Passing null charset should throw NullPointerException as per current implementation
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("csvformattest", ".csv");
            File file = tempFile.toFile();
            assertThrows(NullPointerException.class, () -> csvFormat.print(file, null));
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException ignored) {
                }
            }
        }
    }
}