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
import java.lang.reflect.Constructor;

class CSVFormat_18_1Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultConstructor() throws Exception {
        // Use reflection to create instance with nullString set to null
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(
                ',', '"', null, null, null,
                false, true, "\r\n", null, null,
                null, false, false, false);
        assertNull(csvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullString() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withNullString("NULL");
        assertEquals("NULL", csvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullStringEmpty() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withNullString("");
        assertEquals("", csvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullStringNull() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withNullString(null);
        assertNull(csvFormat.getNullString());
    }
}