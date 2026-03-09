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

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_30_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testParse_ReturnsCSVParser() throws IOException {
        try (Reader reader = new java.io.StringReader("a,b\n1,2")) {
            CSVParser parser = csvFormat.parse(reader);
            assertNotNull(parser);
        }
    }

    @Test
    @Timeout(8000)
    public void testParse_WithNullReader_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            csvFormat.parse(null);
        });
    }

    @Test
    @Timeout(8000)
    public void testParse_WithCustomFormat() throws IOException {
        CSVFormat customFormat = CSVFormat.newFormat(';')
                .withQuote('\'')
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\n");
        try (Reader reader = new java.io.StringReader("a;b\n'1';'2'")) {
            CSVParser parser = customFormat.parse(reader);
            assertNotNull(parser);
        }
    }
}