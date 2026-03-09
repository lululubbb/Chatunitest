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
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_24_3Test {

    @Test
    @Timeout(8000)
    void testGetAutoFlush_DefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_WithAutoFlushTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAutoFlush(true);
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_WithAutoFlushFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAutoFlush(false);
        assertFalse(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_CustomInstanceViaReflection() throws Exception {
        // Using reflection to create a CSVFormat instance with autoFlush = true and false
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat formatTrue = ctor.newInstance(',', '"', null,
                null, null, false, true,
                "\r\n", null, null, null,
                false, false, false, false,
                false, true);
        assertTrue(formatTrue.getAutoFlush());

        CSVFormat formatFalse = ctor.newInstance(',', '"', null,
                null, null, false, true,
                "\r\n", null, null, null,
                false, false, false, false,
                false, false);
        assertFalse(formatFalse.getAutoFlush());
    }
}