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

public class CSVFormat_52_3Test {

    @Test
    @Timeout(8000)
    public void testWithFirstRecordAsHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat resultFormat = baseFormat.withFirstRecordAsHeader();

        // It should be a new instance, not the same as baseFormat
        assertNotSame(baseFormat, resultFormat);

        // It should NOT have header set (null)
        assertNull(resultFormat.getHeader());

        // It should skip header record
        assertTrue(resultFormat.getSkipHeaderRecord());

        // The header of resultFormat should be same as result of withHeader() on baseFormat
        CSVFormat withHeaderFormat = baseFormat.withHeader((String[]) null);
        assertArrayEquals(withHeaderFormat.getHeader(), resultFormat.getHeader());

        // The skipHeaderRecord flag should be true
        assertTrue(resultFormat.getSkipHeaderRecord());
    }
}