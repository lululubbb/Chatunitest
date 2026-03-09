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

class CSVFormatValueOfTest {

    @Test
    @Timeout(8000)
    void testValueOf_DefaultFormats() {
        // Test known predefined formats by name
        CSVFormat f1 = CSVFormat.valueOf("DEFAULT");
        assertNotNull(f1);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), f1.getDelimiter());

        CSVFormat f2 = CSVFormat.valueOf("EXCEL");
        assertNotNull(f2);
        assertEquals(CSVFormat.EXCEL.getDelimiter(), f2.getDelimiter());

        CSVFormat f3 = CSVFormat.valueOf("INFORMIX_UNLOAD");
        assertNotNull(f3);
        assertEquals(CSVFormat.INFORMIX_UNLOAD.getDelimiter(), f3.getDelimiter());

        CSVFormat f4 = CSVFormat.valueOf("INFORMIX_UNLOAD_CSV");
        assertNotNull(f4);
        assertEquals(CSVFormat.INFORMIX_UNLOAD_CSV.getDelimiter(), f4.getDelimiter());

        CSVFormat f5 = CSVFormat.valueOf("MYSQL");
        assertNotNull(f5);
        assertEquals(CSVFormat.MYSQL.getDelimiter(), f5.getDelimiter());

        CSVFormat f6 = CSVFormat.valueOf("POSTGRESQL_CSV");
        assertNotNull(f6);
        assertEquals(CSVFormat.POSTGRESQL_CSV.getDelimiter(), f6.getDelimiter());

        CSVFormat f7 = CSVFormat.valueOf("POSTGRESQL_TEXT");
        assertNotNull(f7);
        assertEquals(CSVFormat.POSTGRESQL_TEXT.getDelimiter(), f7.getDelimiter());

        CSVFormat f8 = CSVFormat.valueOf("RFC4180");
        assertNotNull(f8);
        assertEquals(CSVFormat.RFC4180.getDelimiter(), f8.getDelimiter());

        CSVFormat f9 = CSVFormat.valueOf("TDF");
        assertNotNull(f9);
        assertEquals(CSVFormat.TDF.getDelimiter(), f9.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testValueOf_InvalidFormat() {
        // The predefined enum valueOf throws IllegalArgumentException if not found
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("NonExistentFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf((String) null));
    }

}