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

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_53_5Test {

    private enum TestEnum {
        ONE, TWO, THREE
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((Class<? extends Enum<?>>) null);
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithEnum() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(TestEnum.class);
        assertNotNull(result);
        String[] expected = {"ONE", "TWO", "THREE"};
        assertArrayEquals(expected, result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderInvokesPublicWithHeaderArray() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] headerArray = new String[]{"A", "B"};

        // Use reflection to get public withHeader(String... header) method
        Method withHeaderArrayMethod = CSVFormat.class.getMethod("withHeader", String[].class);

        // Invoke method directly and verify new CSVFormat has correct header
        CSVFormat newFormat = (CSVFormat) withHeaderArrayMethod.invoke(format, (Object) headerArray);
        assertNotNull(newFormat);
        assertArrayEquals(headerArray, newFormat.getHeader());
    }
}