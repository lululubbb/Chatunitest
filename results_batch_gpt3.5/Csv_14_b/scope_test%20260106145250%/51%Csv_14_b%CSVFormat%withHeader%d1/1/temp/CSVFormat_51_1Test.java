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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CSVFormatWithHeaderTest {

    private enum TestEnum {
        FIRST, SECOND, THIRD
    }

    @Test
    @Timeout(8000)
    void withHeader_nullEnum_returnsWithHeaderNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result;
        try {
            Method method = CSVFormat.class.getMethod("withHeader", Class.class);
            // Pass null properly as a single argument
            Object invoked = method.invoke(format, (Object) null);
            result = (CSVFormat) invoked;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void withHeader_enum_returnsHeaderNames() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(TestEnum.class);
        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertArrayEquals(new String[]{"FIRST", "SECOND", "THIRD"}, header);
    }

    @Test
    @Timeout(8000)
    void withHeader_enum_emptyEnum_returnsEmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(EmptyEnum.class);
        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(0, header.length);
    }

    private enum EmptyEnum {
    }
}