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

public class CSVFormat_50_5Test {

    @Test
    @Timeout(8000)
    public void testWithFirstRecordAsHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat resultFormat = baseFormat.withFirstRecordAsHeader();

        assertNotNull(resultFormat, "Resulting CSVFormat should not be null");
        // It should have header set (non-null and length == 0 because withHeader() sets empty headers to empty array)
        assertNotNull(resultFormat.getHeader(), "Header should be set");
        assertEquals(0, resultFormat.getHeader().length, "Header length should be 0");
        // It should skip the header record
        assertTrue(resultFormat.getSkipHeaderRecord(), "SkipHeaderRecord should be true");

        // The original format should not be modified (immutability)
        assertNull(baseFormat.getHeader(), "Original format header should be null");
        assertFalse(baseFormat.getSkipHeaderRecord(), "Original format skipHeaderRecord should be false");
    }
}