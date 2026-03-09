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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_28_3Test {

    @Test
    @Timeout(8000)
    void testIsNullStringSetWhenNullStringIsNull() {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Create a new instance of CSVFormat with null nullString using withNullString method
        CSVFormat modifiedFormat = csvFormat.withNullString(null);

        // Assert that isNullStringSet returns false
        assertFalse(modifiedFormat.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSetWhenNullStringIsNotNull() {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Create a new instance of CSVFormat with non-null nullString using withNullString method
        CSVFormat modifiedFormat = csvFormat.withNullString("NULL");

        // Assert that isNullStringSet returns true
        assertTrue(modifiedFormat.isNullStringSet());
    }
}