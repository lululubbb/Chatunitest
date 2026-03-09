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

class CSVFormatWithHeaderTest {

    private enum TestEnum {
        FIRST, SECOND, THIRD
    }

    private enum EmptyEnum {}

    @Test
    @Timeout(8000)
    void testWithHeader_NullEnum() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader((Class<? extends Enum<?>>) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        // Should be a different instance (immutable style)
        assertNotSame(baseFormat, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyEnum() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(EmptyEnum.class);
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());
        assertNotSame(baseFormat, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NonEmptyEnum() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(TestEnum.class);
        assertNotNull(result);
        String[] expectedHeader = new String[] {"FIRST", "SECOND", "THIRD"};
        assertArrayEquals(expectedHeader, result.getHeader());
        assertNotSame(baseFormat, result);
    }

}