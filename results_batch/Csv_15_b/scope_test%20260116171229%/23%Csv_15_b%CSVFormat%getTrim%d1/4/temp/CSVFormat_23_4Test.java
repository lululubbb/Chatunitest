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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;

class CSVFormat_23_4Test {

    @Test
    @Timeout(8000)
    void testGetTrim_DefaultFalse() throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(',', '"', null, null, null, false, true, "\r\n",
                null, new Object[0], new String[0], false, false, false, false, false, false);
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrim_True() throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(',', '"', null, null, null, false, true, "\r\n",
                null, new Object[0], new String[0], false, false, false, true, false, false);
        assertTrue(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrim_UsingWithTrimTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        assertTrue(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrim_UsingWithTrimFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);
        assertFalse(format.getTrim());
    }
}