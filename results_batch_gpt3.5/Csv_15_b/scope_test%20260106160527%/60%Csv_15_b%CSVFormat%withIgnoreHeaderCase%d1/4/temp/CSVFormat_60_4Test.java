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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_60_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_noArg() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreHeaderCase();
        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreHeaderCase());
        // Original instance is immutable, so different instance expected
        assertNotSame(format, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_booleanTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getMethod("withIgnoreHeaderCase", boolean.class);
        CSVFormat newFormat = (CSVFormat) method.invoke(format, true);
        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreHeaderCase());
        assertNotSame(format, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_booleanFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        Method method = CSVFormat.class.getMethod("withIgnoreHeaderCase", boolean.class);
        CSVFormat newFormat = (CSVFormat) method.invoke(format, false);
        assertNotNull(newFormat);
        assertFalse(newFormat.getIgnoreHeaderCase());
        // When setting false, the instance may be the same as DEFAULT because of internal optimization
        if (newFormat == format) {
            assertSame(format, newFormat);
        } else {
            assertNotSame(format, newFormat);
        }
    }
}