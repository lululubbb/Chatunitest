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

import java.lang.reflect.Field;

class CSVFormat_42_1Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_TogglesFlagCorrectly() throws Exception {
        // Create initial CSVFormat with allowMissingColumnNames = false
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getAllowMissingColumnNames());

        // Call withAllowMissingColumnNames(true)
        CSVFormat modifiedTrue = original.withAllowMissingColumnNames(true);
        assertNotNull(modifiedTrue);
        assertTrue(modifiedTrue.getAllowMissingColumnNames());

        // Call withAllowMissingColumnNames(false)
        CSVFormat modifiedFalse = original.withAllowMissingColumnNames(false);
        assertNotNull(modifiedFalse);
        assertFalse(modifiedFalse.getAllowMissingColumnNames());

        // Ensure original is unchanged
        assertFalse(original.getAllowMissingColumnNames());

        // Ensure new instances are different objects
        assertNotSame(original, modifiedTrue);
        assertNotSame(original, modifiedFalse);

        // Reflection to verify private field allowMissingColumnNames
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);
        boolean originalFlag = field.getBoolean(original);
        boolean modifiedTrueFlag = field.getBoolean(modifiedTrue);
        boolean modifiedFalseFlag = field.getBoolean(modifiedFalse);

        assertFalse(originalFlag);
        assertTrue(modifiedTrueFlag);
        assertFalse(modifiedFalseFlag);
    }
}