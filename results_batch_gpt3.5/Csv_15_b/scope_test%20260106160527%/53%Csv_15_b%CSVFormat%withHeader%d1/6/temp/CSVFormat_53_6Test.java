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
import static org.mockito.Mockito.*;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

class CSVFormatWithHeaderTest {

    private enum TestEnum {
        FIRST, SECOND, THIRD
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullEnum() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader((Class<? extends Enum<?>>) null);
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyEnum() throws Exception {
        @SuppressWarnings("unchecked")
        Class<? extends Enum<?>> mockEnumClass = mock(Class.class);

        @SuppressWarnings("unchecked")
        Enum<?>[] emptyEnumConstants = (Enum<?>[]) Array.newInstance(TestEnum.class, 0);

        when(mockEnumClass.getEnumConstants()).thenReturn(emptyEnumConstants);

        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(mockEnumClass);
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NormalEnum() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(TestEnum.class);
        assertNotNull(result);
        assertArrayEquals(new String[]{"FIRST", "SECOND", "THIRD"}, result.getHeader());
    }

}