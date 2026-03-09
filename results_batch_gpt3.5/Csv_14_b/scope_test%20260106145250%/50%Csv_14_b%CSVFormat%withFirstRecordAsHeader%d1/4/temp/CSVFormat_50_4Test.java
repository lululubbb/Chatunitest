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

public class CSVFormat_50_4Test {

    @Test
    @Timeout(8000)
    public void testWithFirstRecordAsHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat resultFormat = baseFormat.withFirstRecordAsHeader();

        assertNotNull(resultFormat);
        // The header should be non-null, but withFirstRecordAsHeader sets an empty header array
        assertNotNull(resultFormat.getHeader());
        // It should skip header record
        assertTrue(resultFormat.getSkipHeaderRecord());

        // The returned format should be different instance (immutability)
        assertNotSame(baseFormat, resultFormat);

        // The header array should be empty (withHeader() sets empty header)
        String[] header = resultFormat.getHeader();
        assertNotNull(header);
        assertEquals(0, header.length);
    }
}