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

class CSVFormat_47_2Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase() throws Exception {
        CSVFormat originalFormat = CSVFormat.DEFAULT;

        // Using reflection to invoke withIgnoreHeaderCase(boolean) method
        CSVFormat newFormat = (CSVFormat) CSVFormat.class
                .getMethod("withIgnoreHeaderCase", boolean.class)
                .invoke(originalFormat, true);

        // The newFormat should not be the same instance as originalFormat
        assertNotSame(originalFormat, newFormat);

        // The newFormat should have ignoreHeaderCase set to true
        assertTrue(newFormat.getIgnoreHeaderCase());

        // The originalFormat should remain unchanged (ignoreHeaderCase false)
        assertFalse(originalFormat.getIgnoreHeaderCase());
    }
}