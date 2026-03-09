package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_16_3Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_DefaultFalse() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(',', null, null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false, false, false, false);
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_TrueViaWithMethod() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_TrueViaWithIgnoreSurroundingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces();
        assertTrue(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_FalseViaWithIgnoreSurroundingSpacesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_ConstantTDF() {
        assertTrue(CSVFormat.TDF.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_ConstantDefault() {
        assertFalse(CSVFormat.DEFAULT.getIgnoreSurroundingSpaces());
    }
}