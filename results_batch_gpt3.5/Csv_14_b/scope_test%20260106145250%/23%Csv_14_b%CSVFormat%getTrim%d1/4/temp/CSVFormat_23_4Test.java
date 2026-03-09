package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_23_4Test {

    @Test
    @Timeout(8000)
    void testGetTrimDefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to get the value of the private final field 'trim'
        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trimValue = trimField.getBoolean(format);

        assertFalse(trimValue);
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrimTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        assertTrue(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrimFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);
        assertFalse(format.getTrim());
    }
}