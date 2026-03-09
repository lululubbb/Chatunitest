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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    private enum TestEnum {
        ALPHA, BETA, GAMMA
    }

    @Test
    @Timeout(8000)
    void withHeader_NullEnum_ReturnsSameAsWithHeaderNull() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader((Class<? extends Enum<?>>) null);
        CSVFormat expected = baseFormat.withHeader((String[]) null);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void withHeader_EmptyEnum_ReturnsFormatWithEmptyHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(EmptyEnumStatic.class);
        CSVFormat expected = baseFormat.withHeader(new String[0]);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void withHeader_EnumWithValues_ReturnsFormatWithHeaderNames() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(TestEnum.class);
        String[] expectedHeader = new String[] { "ALPHA", "BETA", "GAMMA" };
        CSVFormat expected = baseFormat.withHeader(expectedHeader);
        assertEquals(expected, result);
        assertArrayEquals(expectedHeader, result.getHeader());
    }

    // static nested enum with no constants
    private static enum EmptyEnumStatic { }
}