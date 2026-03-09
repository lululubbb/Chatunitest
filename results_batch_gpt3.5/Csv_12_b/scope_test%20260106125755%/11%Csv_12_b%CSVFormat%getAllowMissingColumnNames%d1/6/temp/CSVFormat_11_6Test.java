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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

class CSVFormat_11_6Test {

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_DefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to access the private final field allowMissingColumnNames
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);
        boolean value = field.getBoolean(format);
        assertFalse(value);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_True() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);
        boolean value = field.getBoolean(format);
        assertTrue(value);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_False() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);
        boolean value = field.getBoolean(format);
        assertFalse(value);
        assertFalse(format.getAllowMissingColumnNames());
    }
}