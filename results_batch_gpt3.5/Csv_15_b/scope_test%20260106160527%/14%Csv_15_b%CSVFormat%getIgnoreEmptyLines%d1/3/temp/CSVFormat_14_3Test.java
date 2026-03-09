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
import java.io.IOException;
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
import org.junit.jupiter.api.Test;

public class CSVFormat_14_3Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_MYSQL() {
        CSVFormat format = CSVFormat.MYSQL;
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_PostgreSQLCSV() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_PostgreSQLText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_TDF() {
        CSVFormat format = CSVFormat.TDF;
        assertTrue(format.getIgnoreEmptyLines());
    }
}