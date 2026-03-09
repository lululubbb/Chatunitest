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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_38_4Test {

    @Test
    @Timeout(8000)
    public void testPrintRecord() throws IOException {
        // Given
        StringWriter out = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object[] values = {"value1", "value2", "value3"};

        // When
        csvFormat.printRecord(out, values);

        // Then
        assertEquals("value1,value2,value3" + System.lineSeparator(), out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecordWithEmptyValues() throws IOException {
        // Given
        StringWriter out = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object[] values = {};

        // When
        csvFormat.printRecord(out, values);

        // Then
        assertEquals(System.lineSeparator(), out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecordWithNullValues() throws IOException {
        // Given
        StringWriter out = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object[] values = {null, "value2", null};

        // When
        csvFormat.printRecord(out, values);

        // Then
        assertEquals("null,value2,null" + System.lineSeparator(), out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecordWithSpecialCharacters() throws IOException {
        // Given
        StringWriter out = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Object[] values = {"\"value1\"", "value,2", "value\n3"};

        // When
        csvFormat.printRecord(out, values);

        // Then
        assertEquals("\"\"\"value1\"\"\",\"value,2\",\"value\n3\"" + System.lineSeparator(), out.toString());
    }
}