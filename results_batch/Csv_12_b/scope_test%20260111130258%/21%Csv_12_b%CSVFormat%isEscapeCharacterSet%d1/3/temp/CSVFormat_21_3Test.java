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
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class CSVFormat_21_3Test {

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet() {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        boolean isEscapeCharacterSet = csvFormat.isEscapeCharacterSet();

        assertFalse(isEscapeCharacterSet);
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSetWithEscapeCharacter() {
        CSVFormat csvFormat = CSVFormat.newFormat(',').withEscape('\\').withIgnoreEmptyLines(true);

        boolean isEscapeCharacterSet = csvFormat.isEscapeCharacterSet();

        assertTrue(isEscapeCharacterSet);
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSetWithNullEscapeCharacter() {
        CSVFormat csvFormat = CSVFormat.newFormat(',').withIgnoreEmptyLines(true);

        boolean isEscapeCharacterSet = csvFormat.isEscapeCharacterSet();

        assertFalse(isEscapeCharacterSet);
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSetWithPrivateEscapeCharacter() throws Exception {
        CSVFormat csvFormat = CSVFormat.newFormat(',').withEscape('\\').withIgnoreEmptyLines(true);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        escapeCharacterField.set(csvFormat, null);

        boolean isEscapeCharacterSet = csvFormat.isEscapeCharacterSet();

        assertFalse(isEscapeCharacterSet);
    }
}