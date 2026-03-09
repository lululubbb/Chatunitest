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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_67_1Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_String() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "\n";
        CSVFormat changed = original.withRecordSeparator(newSeparator);

        assertNotNull(changed);
        assertNotSame(original, changed);
        assertEquals(newSeparator, changed.getRecordSeparator());

        // Original remains unchanged
        assertEquals("\r\n", original.getRecordSeparator());

        // Test with null separator
        CSVFormat changedNull = original.withRecordSeparator((String) null);
        assertNotNull(changedNull);
        assertNotSame(original, changedNull);
        assertNull(changedNull.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_Char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Using reflection to invoke public withRecordSeparator(char)
        Method method = CSVFormat.class.getMethod("withRecordSeparator", char.class);

        CSVFormat changed = (CSVFormat) method.invoke(original, '\n');

        assertNotNull(changed);
        assertNotSame(original, changed);
        assertEquals("\n", changed.getRecordSeparator());

        // Test with line break char '\r'
        CSVFormat changedCR = (CSVFormat) method.invoke(original, '\r');
        assertNotNull(changedCR);
        assertNotSame(original, changedCR);
        assertEquals("\r", changedCR.getRecordSeparator());
    }
}