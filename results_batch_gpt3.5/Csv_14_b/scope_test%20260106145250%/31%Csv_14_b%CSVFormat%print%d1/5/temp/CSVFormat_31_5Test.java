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
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintWithValidFile() throws IOException {
        File file = new File(tempDir, "test.csv");
        try (CSVPrinter printer = csvFormat.print(file.toPath(), StandardCharsets.UTF_8)) {
            assertNotNull(printer);
        }
        assertTrue(file.exists());
        assertTrue(file.length() >= 0);
    }

    @Test
    @Timeout(8000)
    void testPrintWithNullCharset() {
        File file = new File(tempDir, "test.csv");
        assertThrows(NullPointerException.class, () -> csvFormat.print(file.toPath(), null));
    }

    @Test
    @Timeout(8000)
    void testPrintWithNonWritableFile() throws IOException {
        File file = new File(tempDir, "readonly.csv");
        boolean created = file.createNewFile();
        assertTrue(created || file.exists());
        boolean madeReadOnly = file.setWritable(false);
        assertTrue(madeReadOnly || !file.canWrite());

        IOException exception = assertThrows(IOException.class, () -> {
            try (CSVPrinter printer = csvFormat.print(file.toPath(), StandardCharsets.UTF_8)) {
                // no-op
            }
        });
        assertNotNull(exception);
    }
}