package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class CSVFormat_10_6Test {

    private CSVFormat defaultFormat;
    private CSVFormat customFormat;

    @BeforeEach
    void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
        customFormat = CSVFormat.newFormat(';');
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Default() {
        assertEquals(',', defaultFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Custom() {
        assertEquals(';', customFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithTabDelimiter() {
        CSVFormat tabFormat = CSVFormat.TDF;
        assertEquals('\t', tabFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithMySQLFormat() {
        CSVFormat mysqlFormat = CSVFormat.MYSQL;
        assertEquals('\t', mysqlFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithExcelFormat() {
        CSVFormat excelFormat = CSVFormat.EXCEL;
        assertEquals(',', excelFormat.getDelimiter());
    }
}