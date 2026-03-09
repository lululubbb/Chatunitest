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

class CSVFormat_70_6Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_noArg() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withSkipHeaderRecord();

        // The returned format should be different if skipHeaderRecord was false originally
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());

        // Original should remain unchanged (immutable style)
        assertFalse(original.getSkipHeaderRecord());

        // Using reflection to invoke public withSkipHeaderRecord(boolean)
        Method method = CSVFormat.class.getMethod("withSkipHeaderRecord", boolean.class);

        CSVFormat invokedTrue = (CSVFormat) method.invoke(original, true);
        assertTrue(invokedTrue.getSkipHeaderRecord());

        CSVFormat invokedFalse = (CSVFormat) method.invoke(original, false);
        assertFalse(invokedFalse.getSkipHeaderRecord());

        // If the flag is already set, withSkipHeaderRecord(true) should return the same instance
        CSVFormat sameInstance = (CSVFormat) method.invoke(result, true);
        assertSame(result, sameInstance);

        // If the flag is changed, a new instance should be returned
        CSVFormat newInstance = (CSVFormat) method.invoke(result, false);
        assertNotSame(result, newInstance);
        assertFalse(newInstance.getSkipHeaderRecord());
    }
}