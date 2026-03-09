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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CSVFormat_14_4Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // getAllowMissingColumnNames is public, no need for reflection, but kept for compatibility
        Method method = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(format);
        assertFalse(result, "DEFAULT should not allow missing column names");
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNames() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames();
        Method method = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(format);
        assertTrue(result, "Format withAllowMissingColumnNames should allow missing column names");
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        Method method = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(format);
        assertFalse(result, "Format withAllowMissingColumnNames(false) should not allow missing column names");
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Immutable() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = format1.withAllowMissingColumnNames();
        CSVFormat format3 = format2.withAllowMissingColumnNames(false);

        Method method = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        method.setAccessible(true);

        boolean result1 = (boolean) method.invoke(format1);
        boolean result2 = (boolean) method.invoke(format2);
        boolean result3 = (boolean) method.invoke(format3);

        assertFalse(result1, "Original format should remain unchanged");
        assertTrue(result2, "Modified format2 should allow missing column names");
        assertFalse(result3, "Modified format3 should not allow missing column names");
    }
}