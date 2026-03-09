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

import java.lang.reflect.Method;

public class CSVFormat_14_2Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        boolean result = invokeGetAllowMissingColumnNames(format);
        assertFalse(result, "DEFAULT should not allow missing column names");
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Excel() throws Exception {
        CSVFormat format = CSVFormat.EXCEL;
        boolean result = invokeGetAllowMissingColumnNames(format);
        assertTrue(result, "EXCEL should allow missing column names");
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        boolean result = invokeGetAllowMissingColumnNames(format);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        boolean result = invokeGetAllowMissingColumnNames(format);
        assertFalse(result);
    }

    private boolean invokeGetAllowMissingColumnNames(CSVFormat format) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        method.setAccessible(true);
        return (boolean) method.invoke(format);
    }
}