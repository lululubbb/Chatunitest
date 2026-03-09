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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class CSVFormat_33_6Test {

    @Test
    @Timeout(8000)
    public void testPrint_FileCharset() throws IOException {
        // Prepare
        CSVFormat format = CSVFormat.DEFAULT;
        File tempFile = File.createTempFile("csvformattest", ".csv");
        tempFile.deleteOnExit();

        // Execute
        try (CSVPrinter printer = format.print(tempFile, StandardCharsets.UTF_8)) {
            // Verify
            assertNotNull(printer);
        }
    }

    @Test
    @Timeout(8000)
    public void testPrint_FileCharset_withDifferentFormat() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'').withRecordSeparator("\n");
        File tempFile = File.createTempFile("csvformattest2", ".csv");
        tempFile.deleteOnExit();

        try (CSVPrinter printer = format.print(tempFile, StandardCharsets.ISO_8859_1)) {
            assertNotNull(printer);
        }
    }

    @Test
    @Timeout(8000)
    public void testPrint_FileCharset_nullCharset() {
        CSVFormat format = CSVFormat.DEFAULT;
        File tempFile = new File("somefile.csv");

        assertThrows(NullPointerException.class, () -> {
            format.print(tempFile, null);
        });
    }

    @Test
    @Timeout(8000)
    public void testPrint_FileCharset_nullFile() {
        CSVFormat format = CSVFormat.DEFAULT;

        assertThrows(NullPointerException.class, () -> {
            format.print((File) null, StandardCharsets.UTF_8);
        });
    }
}