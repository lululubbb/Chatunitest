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
import static org.mockito.Mockito.*;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_68_6Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord_noArg_callsWithSkipHeaderRecordTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat result = format.withSkipHeaderRecord();

        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord_booleanArg_true() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat result = format.withSkipHeaderRecord(true);

        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord_booleanArg_false() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);

        CSVFormat result = format.withSkipHeaderRecord(false);

        assertNotNull(result);
        assertFalse(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord_reflectionInvoke() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getMethod("withSkipHeaderRecord");
        Object result = method.invoke(format);

        assertNotNull(result);
        assertTrue(((CSVFormat) result).getSkipHeaderRecord());
    }
}