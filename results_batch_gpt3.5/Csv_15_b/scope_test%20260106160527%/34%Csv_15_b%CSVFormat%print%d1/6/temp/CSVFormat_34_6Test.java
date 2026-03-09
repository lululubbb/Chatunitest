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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatPrintTest {

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("csvformat-test", ".csv");
    }

    @AfterEach
    void tearDown() throws IOException {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinterSuccessfully() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.print(tempFile, StandardCharsets.UTF_8);
        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsIOExceptionWhenWriterFails() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a directory and pass it as a file path (writing to a directory fails)
        Path directory = Files.createTempDirectory("csvformat-invalid-dir");
        try {
            assertThrows(IOException.class, () -> format.print(directory, StandardCharsets.UTF_8));
        } finally {
            Files.deleteIfExists(directory);
        }
    }
}