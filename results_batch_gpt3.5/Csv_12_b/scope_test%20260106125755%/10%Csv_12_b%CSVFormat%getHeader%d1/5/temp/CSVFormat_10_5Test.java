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
import java.lang.reflect.Modifier;

public class CSVFormat_10_5Test {

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNull() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        // header is null by default
        String[] header = format.getHeader();
        assertNull(header, "Header should be null when not set");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneNotSameReference() throws Exception {
        String[] originalHeader = new String[] {"col1", "col2"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(originalHeader);

        String[] header1 = format.getHeader();
        String[] header2 = format.getHeader();

        assertNotNull(header1);
        assertArrayEquals(originalHeader, header1);
        assertNotSame(header1, header2, "getHeader should return a clone, not the same array instance");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderImmutability() throws Exception {
        String[] originalHeader = new String[] {"a", "b", "c"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(originalHeader);

        String[] header = format.getHeader();
        header[0] = "modified";

        String[] headerAfterModification = format.getHeader();
        assertEquals("a", headerAfterModification[0], "Internal header array should not be modified");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderUsingReflectionToSetPrivateField() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        // Remove final modifier from the field (works on Java 8 and below)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerField, headerField.getModifiers() & ~Modifier.FINAL);

        String[] testHeader = new String[]{"x", "y", "z"};
        headerField.set(format, testHeader);

        String[] returnedHeader = format.getHeader();
        assertArrayEquals(testHeader, returnedHeader);
        assertNotSame(testHeader, returnedHeader, "Returned array should be a clone, not the same reference");
    }
}