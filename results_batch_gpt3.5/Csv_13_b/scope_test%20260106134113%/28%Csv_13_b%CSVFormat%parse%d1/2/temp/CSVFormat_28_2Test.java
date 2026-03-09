package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_28_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT.withHeader(); // explicitly set header to avoid potential parse issues
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_returnsCSVParser() throws IOException {
        Reader reader = new java.io.StringReader("a,b,c\n1,2,3");

        CSVParser result = csvFormat.parse(reader);

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }
}