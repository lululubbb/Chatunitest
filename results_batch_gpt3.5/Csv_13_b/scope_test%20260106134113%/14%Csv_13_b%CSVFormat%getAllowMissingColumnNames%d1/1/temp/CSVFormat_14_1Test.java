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

public class CSVFormat_14_1Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_defaultFalse() {
        CSVFormat format = CSVFormat.newFormat(',');
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_withAllowMissingColumnNamesTrue() {
        CSVFormat format = CSVFormat.newFormat(',').withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_withAllowMissingColumnNamesFalse() {
        CSVFormat format = CSVFormat.newFormat(',').withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_onPredefinedConstants() {
        assertFalse(CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.RFC4180.getAllowMissingColumnNames());
        assertTrue(CSVFormat.EXCEL.getAllowMissingColumnNames());
        assertFalse(CSVFormat.TDF.getAllowMissingColumnNames());
        assertFalse(CSVFormat.MYSQL.getAllowMissingColumnNames());
    }
}