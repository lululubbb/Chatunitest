package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_55_4Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord_noArg() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withSkipHeaderRecord();

        // withSkipHeaderRecord() returns a new CSVFormat with skipHeaderRecord true
        assertNotNull(result);
        assertNotSame(original, result);

        // Access skipHeaderRecord via public getter instead of reflection
        assertTrue(result.getSkipHeaderRecord());

        // Original instance's skipHeaderRecord should not be affected
        assertFalse(original.getSkipHeaderRecord());
    }
}