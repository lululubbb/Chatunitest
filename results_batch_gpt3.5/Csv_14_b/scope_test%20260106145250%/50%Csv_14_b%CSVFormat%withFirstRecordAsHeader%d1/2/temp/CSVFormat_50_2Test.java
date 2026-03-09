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

class CSVFormat_50_2Test {

    @Test
    @Timeout(8000)
    void testWithFirstRecordAsHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withFirstRecordAsHeader();

        assertNotNull(result);
        assertNotSame(format, result);

        // The returned format should have a header set (non-null and length 0)
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);

        // The returned format should skip header record
        assertTrue(result.getSkipHeaderRecord());

        // Ensure original format is unchanged
        assertNull(format.getHeader());
        assertFalse(format.getSkipHeaderRecord());
    }
}