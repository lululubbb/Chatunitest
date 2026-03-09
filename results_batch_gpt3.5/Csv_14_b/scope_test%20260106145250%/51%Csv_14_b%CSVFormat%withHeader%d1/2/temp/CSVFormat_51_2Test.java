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

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_51_2Test {

    private enum TestEnum {
        FIRST, SECOND, THIRD
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((Class<? extends Enum<?>>) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithEnum() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(TestEnum.class);
        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(TestEnum.values().length, header.length);
        for (int i = 0; i < header.length; i++) {
            assertEquals(TestEnum.values()[i].name(), header[i]);
        }
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderReflectionInvoke() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getDeclaredMethod("withHeader", Class.class);
        method.setAccessible(true);

        // invoke with null
        Object resultNull = method.invoke(format, new Object[] { null });
        assertNotNull(resultNull);
        assertTrue(resultNull instanceof CSVFormat);
        CSVFormat formatNull = (CSVFormat) resultNull;
        assertNull(formatNull.getHeader());

        // invoke with enum
        Object resultEnum = method.invoke(format, (Object) TestEnum.class);
        assertNotNull(resultEnum);
        assertTrue(resultEnum instanceof CSVFormat);
        CSVFormat formatEnum = (CSVFormat) resultEnum;
        assertNotNull(formatEnum.getHeader());
        assertEquals(TestEnum.values().length, formatEnum.getHeader().length);
        for (int i = 0; i < formatEnum.getHeader().length; i++) {
            assertEquals(TestEnum.values()[i].name(), formatEnum.getHeader()[i]);
        }
    }
}