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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_68_3Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_noArg() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withSkipHeaderRecord();
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_boolean_true() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method withSkipHeaderRecordBool = CSVFormat.class.getMethod("withSkipHeaderRecord", boolean.class);

        CSVFormat result = (CSVFormat) withSkipHeaderRecordBool.invoke(format, true);
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_boolean_false() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method withSkipHeaderRecordBool = CSVFormat.class.getMethod("withSkipHeaderRecord", boolean.class);

        CSVFormat result = (CSVFormat) withSkipHeaderRecordBool.invoke(format, false);
        assertNotNull(result);
        assertFalse(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_boolean_returnsNewInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method withSkipHeaderRecordBool = CSVFormat.class.getMethod("withSkipHeaderRecord", boolean.class);

        CSVFormat resultTrue = (CSVFormat) withSkipHeaderRecordBool.invoke(format, true);
        CSVFormat resultFalse = (CSVFormat) withSkipHeaderRecordBool.invoke(format, false);

        assertNotSame(format, resultTrue);
        assertNotSame(format, resultFalse);
        assertNotSame(resultTrue, resultFalse);
    }
}