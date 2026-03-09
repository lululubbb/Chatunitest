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
import java.lang.reflect.Modifier;

public class CSVFormat_12_5Test {

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsCloneWhenHeaderNotNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("dummy"); // create a new instance with header to avoid modifying DEFAULT directly

        String[] originalHeader = new String[] {"col1", "col2"};
        setFinalField(format, "header", originalHeader);

        String[] header = format.getHeader();

        assertNotNull(header);
        assertArrayEquals(originalHeader, header);
        assertNotSame(originalHeader, header);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsNullWhenHeaderIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("dummy"); // create a new instance with header to avoid modifying DEFAULT directly

        setFinalField(format, "header", null);

        String[] header = format.getHeader();

        assertNull(header);
    }
}