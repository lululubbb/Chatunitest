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

class CSVFormat_27_4Test {

    @Test
    @Timeout(8000)
    void testIsNullStringSetWhenNullStringIsNull() throws Exception {
        // Use DEFAULT instance which has nullString == null
        CSVFormat format = CSVFormat.DEFAULT;
        // isNullStringSet should return false
        assertFalse(format.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSetWhenNullStringIsNotNull() throws Exception {
        // Create a CSVFormat with nullString set (e.g. MYSQL)
        CSVFormat format = CSVFormat.MYSQL;
        // isNullStringSet should return true
        assertTrue(format.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSetWithCustomNullString() throws Exception {
        // Use withNullString to create a new instance with a non-null nullString
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        assertTrue(format.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSetWithCustomNullStringNull() throws Exception {
        // Use withNullString to create a new instance with null nullString
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        assertFalse(format.isNullStringSet());
    }
}