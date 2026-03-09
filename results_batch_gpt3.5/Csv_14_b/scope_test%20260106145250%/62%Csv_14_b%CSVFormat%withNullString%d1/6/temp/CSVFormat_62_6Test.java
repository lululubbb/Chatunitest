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

class CSVFormat_62_6Test {

    @Test
    @Timeout(8000)
    void testWithNullString() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test with a non-null string
        String nullStr = "NULL";
        CSVFormat modified = original.withNullString(nullStr);
        assertNotNull(modified);
        assertEquals(nullStr, modified.getNullString());
        // original should remain unchanged
        assertNull(original.getNullString());

        // Test with null string set to null explicitly
        CSVFormat modifiedNull = original.withNullString(null);
        assertNotNull(modifiedNull);
        assertNull(modifiedNull.getNullString());
        assertNull(original.getNullString());

        // Test chaining withNullString on modified instance
        CSVFormat modified2 = modified.withNullString("EMPTY");
        assertEquals("EMPTY", modified2.getNullString());
        assertEquals(nullStr, modified.getNullString());
    }
}