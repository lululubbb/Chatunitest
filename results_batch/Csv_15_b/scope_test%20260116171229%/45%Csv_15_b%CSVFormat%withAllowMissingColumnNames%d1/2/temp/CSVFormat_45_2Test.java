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

class CSVFormat_45_2Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_noArg() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withAllowMissingColumnNames();
        assertNotNull(result);
        assertTrue(result.getAllowMissingColumnNames());
        // The original instance should remain unchanged (immutable pattern)
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_booleanArg_true() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getMethod("withAllowMissingColumnNames", boolean.class);
        CSVFormat result = (CSVFormat) method.invoke(format, true);
        assertNotNull(result);
        assertTrue(result.getAllowMissingColumnNames());
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_booleanArg_false() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        Method method = CSVFormat.class.getMethod("withAllowMissingColumnNames", boolean.class);
        CSVFormat result = (CSVFormat) method.invoke(format, false);
        assertNotNull(result);
        assertFalse(result.getAllowMissingColumnNames());
        assertTrue(format.getAllowMissingColumnNames());
    }
}