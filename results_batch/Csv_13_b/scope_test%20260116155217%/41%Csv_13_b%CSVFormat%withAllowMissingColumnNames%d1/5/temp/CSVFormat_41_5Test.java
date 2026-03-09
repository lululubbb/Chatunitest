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

public class CSVFormat_41_5Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Test the public method withAllowMissingColumnNames() which calls withAllowMissingColumnNames(true)
        CSVFormat result = format.withAllowMissingColumnNames();
        assertNotNull(result);
        assertTrue(result.getAllowMissingColumnNames());

        // Test withAllowMissingColumnNames(boolean) with false to verify it disables the flag
        CSVFormat disabled = format.withAllowMissingColumnNames(false);
        assertNotNull(disabled);
        assertFalse(disabled.getAllowMissingColumnNames());

        // Use reflection to invoke the method withAllowMissingColumnNames(boolean)
        Method withAllowMissingColumnNamesBool = CSVFormat.class.getDeclaredMethod("withAllowMissingColumnNames", boolean.class);
        withAllowMissingColumnNamesBool.setAccessible(true);

        CSVFormat reflectResult = (CSVFormat) withAllowMissingColumnNamesBool.invoke(format, true);
        assertNotNull(reflectResult);
        assertTrue(reflectResult.getAllowMissingColumnNames());

        CSVFormat reflectResultFalse = (CSVFormat) withAllowMissingColumnNamesBool.invoke(format, false);
        assertNotNull(reflectResultFalse);
        assertFalse(reflectResultFalse.getAllowMissingColumnNames());

        // Verify immutability: original format should remain unchanged
        assertFalse(format.getAllowMissingColumnNames());
    }
}