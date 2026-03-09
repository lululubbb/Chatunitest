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

class CSVFormat_4_3Test {

    @Test
    @Timeout(8000)
    void testValueOfKnownFormats() {
        // Test with predefined format names
        assertSame(CSVFormat.DEFAULT, CSVFormat.valueOf("DEFAULT"));
        assertSame(CSVFormat.EXCEL, CSVFormat.valueOf("EXCEL"));
        assertSame(CSVFormat.INFORMIX_UNLOAD, CSVFormat.valueOf("INFORMIX_UNLOAD"));
        assertSame(CSVFormat.INFORMIX_UNLOAD_CSV, CSVFormat.valueOf("INFORMIX_UNLOAD_CSV"));
        assertSame(CSVFormat.MYSQL, CSVFormat.valueOf("MYSQL"));
        assertSame(CSVFormat.POSTGRESQL_CSV, CSVFormat.valueOf("POSTGRESQL_CSV"));
        assertSame(CSVFormat.POSTGRESQL_TEXT, CSVFormat.valueOf("POSTGRESQL_TEXT"));
        assertSame(CSVFormat.RFC4180, CSVFormat.valueOf("RFC4180"));
        assertSame(CSVFormat.TDF, CSVFormat.valueOf("TDF"));
    }

    @Test
    @Timeout(8000)
    void testValueOfCaseInsensitive() {
        // Should be case insensitive for predefined names
        assertSame(CSVFormat.DEFAULT, CSVFormat.valueOf("default"));
        assertSame(CSVFormat.EXCEL, CSVFormat.valueOf("eXceL"));
        assertSame(CSVFormat.RFC4180, CSVFormat.valueOf("rfc4180"));
    }

    @Test
    @Timeout(8000)
    void testValueOfInvalidFormatThrowsException() {
        // Unknown format string should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UnknownFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(null));
    }
}