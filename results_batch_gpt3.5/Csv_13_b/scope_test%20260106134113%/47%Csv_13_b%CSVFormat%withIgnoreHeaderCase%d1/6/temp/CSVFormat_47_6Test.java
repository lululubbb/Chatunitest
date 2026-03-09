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

class CSVFormat_47_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreHeaderCase();
        assertNotNull(modified);
        // It should be a different instance
        assertNotSame(original, modified);

        // Use reflection to access the private final field ignoreHeaderCase
        Field field = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        field.setAccessible(true);
        boolean originalIgnoreHeaderCase = field.getBoolean(original);
        boolean modifiedIgnoreHeaderCase = field.getBoolean(modified);

        // The ignoreHeaderCase flag should be true in the modified instance
        assertTrue(modifiedIgnoreHeaderCase);
        // The original instance should remain unchanged (false by default)
        assertFalse(originalIgnoreHeaderCase);
    }
}