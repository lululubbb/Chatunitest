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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_23_2Test {

    @Test
    @Timeout(8000)
    void testGetTrim_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrim_WithTrimTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        assertTrue(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrim_WithTrimFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrim_NewFormat_DefaultTrim() {
        CSVFormat format = CSVFormat.newFormat(',');
        // newFormat sets trim to false by default as per constructor usage
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrim_CustomInstance() throws Exception {
        // Using reflection to create a CSVFormat instance with trim true
        Class<?> quoteModeClass = null;
        for (Class<?> innerClass : CSVFormat.class.getDeclaredClasses()) {
            if ("QuoteMode".equals(innerClass.getSimpleName())) {
                quoteModeClass = innerClass;
                break;
            }
        }
        assertNotNull(quoteModeClass, "QuoteMode class not found");

        @SuppressWarnings("unchecked")
        Constructor<CSVFormat> constructor = (Constructor<CSVFormat>) CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteModeClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // The 3rd parameter (quoteMode) should be an instance of QuoteMode enum or null
        Object quoteModeInstance = null;

        CSVFormat format = constructor.newInstance(
                ',', null, quoteModeInstance, null, null,
                false, false, "\n", null, null, null,
                false, false, false, true, false, false);
        assertTrue(format.getTrim());
    }
}