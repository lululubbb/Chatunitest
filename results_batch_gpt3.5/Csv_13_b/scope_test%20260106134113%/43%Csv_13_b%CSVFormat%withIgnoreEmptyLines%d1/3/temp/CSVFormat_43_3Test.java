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

class CSVFormat_43_3Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreEmptyLines(true);

        // The method withIgnoreEmptyLines(true) should set ignoreEmptyLines to true
        assertNotNull(modified);

        // Use reflection to access the private field 'ignoreEmptyLines'
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);

        boolean modifiedIgnoreEmptyLines = ignoreEmptyLinesField.getBoolean(modified);
        boolean originalIgnoreEmptyLines = ignoreEmptyLinesField.getBoolean(original);

        assertTrue(modifiedIgnoreEmptyLines);

        // The original instance should remain unchanged (immutable pattern)
        assertFalse(originalIgnoreEmptyLines);

        // The modified instance should be a different object
        assertNotSame(original, modified);
    }
}