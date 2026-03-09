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

import java.lang.reflect.Method;

class CSVFormat_14_3Test {

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_DefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        boolean allowMissingColumnNames = invokeGetAllowMissingColumnNames(format);
        assertFalse(allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_ExcelTrue() throws Exception {
        CSVFormat format = CSVFormat.EXCEL;
        boolean allowMissingColumnNames = invokeGetAllowMissingColumnNames(format);
        assertTrue(allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        boolean allowMissingColumnNames = invokeGetAllowMissingColumnNames(format);
        assertTrue(allowMissingColumnNames);
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        boolean allowMissingColumnNames = invokeGetAllowMissingColumnNames(format);
        assertFalse(allowMissingColumnNames);
    }

    private boolean invokeGetAllowMissingColumnNames(CSVFormat format) throws Exception {
        Method method = CSVFormat.class.getMethod("getAllowMissingColumnNames");
        return (boolean) method.invoke(format);
    }
}