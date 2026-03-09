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

import java.lang.reflect.Method;

class CSVFormatWithHeaderTest {

    private enum TestEnum {
        FIRST, SECOND, THIRD
    }

    private enum EmptyEnum {}

    @Test
    @Timeout(8000)
    void withHeader_withNullEnum_returnsCSVFormatWithNullHeader() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to invoke withHeader(Class<? extends Enum<?>>) with null to avoid ambiguous call
        Method method = CSVFormat.class.getMethod("withHeader", Class.class);
        CSVFormat result = (CSVFormat) method.invoke(format, new Object[] { null });
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void withHeader_withEmptyEnum_returnsCSVFormatWithEmptyHeader() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getMethod("withHeader", Class.class);
        CSVFormat result = (CSVFormat) method.invoke(format, new Object[] { EmptyEnum.class });
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());
    }

    @Test
    @Timeout(8000)
    void withHeader_withEnum_returnsCSVFormatWithEnumNamesAsHeader() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getMethod("withHeader", Class.class);
        CSVFormat result = (CSVFormat) method.invoke(format, new Object[] { TestEnum.class });
        assertNotNull(result);
        String[] expected = {"FIRST", "SECOND", "THIRD"};
        assertArrayEquals(expected, result.getHeader());
    }
}