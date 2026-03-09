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

public class CSVFormat_12_4Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // For static fields, target should be null
        if (Modifier.isStatic(field.getModifiers())) {
            field.set(null, value);
        } else {
            field.set(target, value);
        }
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsCloneWhenHeaderIsNotNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("initial"); // create instance with header initialized
        String[] header = new String[] {"a", "b", "c"};
        setFinalField(format, "header", header);

        String[] returnedHeader = format.getHeader();

        assertNotNull(returnedHeader);
        assertArrayEquals(header, returnedHeader);
        assertNotSame(header, returnedHeader);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsNullWhenHeaderIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("initial"); // create instance with header initialized
        setFinalField(format, "header", null);

        String[] returnedHeader = format.getHeader();

        assertNull(returnedHeader);
    }
}