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
import org.junit.jupiter.api.BeforeEach;

public class CSVFormat_28_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_NullStringIsNull() {
        // CSVFormat is immutable, so use withNullString(null) to get a new instance with nullString set to null
        csvFormat = csvFormat.withNullString(null);

        boolean result = csvFormat.isNullStringSet();

        assertFalse(result, "Expected isNullStringSet() to return false when nullString is null");
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_NullStringIsNotNull() {
        // CSVFormat is immutable, so use withNullString("NULL") to get a new instance with nullString set to "NULL"
        csvFormat = csvFormat.withNullString("NULL");

        boolean result = csvFormat.isNullStringSet();

        assertTrue(result, "Expected isNullStringSet() to return true when nullString is not null");
    }
}