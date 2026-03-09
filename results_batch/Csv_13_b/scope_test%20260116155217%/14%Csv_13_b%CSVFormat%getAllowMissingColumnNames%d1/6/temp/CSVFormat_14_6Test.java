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

public class CSVFormat_14_6Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() throws Exception {
        Method method = CSVFormat.class.getMethod("withAllowMissingColumnNames", boolean.class);
        CSVFormat format = (CSVFormat) method.invoke(CSVFormat.DEFAULT, false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        assertTrue(format.getAllowMissingColumnNames());
    }

}