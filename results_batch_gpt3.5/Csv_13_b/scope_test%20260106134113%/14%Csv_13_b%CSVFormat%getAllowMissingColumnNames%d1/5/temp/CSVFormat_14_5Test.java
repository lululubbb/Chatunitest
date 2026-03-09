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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_14_5Test {

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_ExcelConstant() {
        CSVFormat format = CSVFormat.EXCEL;
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_RFC4180Constant() {
        CSVFormat format = CSVFormat.RFC4180;
        assertFalse(format.getAllowMissingColumnNames());
    }
}