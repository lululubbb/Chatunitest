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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormatWithHeaderTest {

    private enum TestEnum {
        FIRST, SECOND, THIRD
    }

    private enum EmptyEnum {}

    @Test
    @Timeout(8000)
    void withHeader_NullEnum_ReturnsSameFormatWithNullHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader((Class<? extends Enum<?>>) null);
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void withHeader_EmptyEnum_ReturnsFormatWithEmptyHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(EmptyEnum.class);
        assertNotNull(result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);
    }

    @Test
    @Timeout(8000)
    void withHeader_EnumWithValues_ReturnsFormatWithHeaderNames() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(TestEnum.class);
        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(TestEnum.values().length, header.length);
        for (int i = 0; i < header.length; i++) {
            assertEquals(TestEnum.values()[i].name(), header[i]);
        }
    }

    @Test
    @Timeout(8000)
    void withHeader_InvokesWithHeaderStringArray() throws Exception {
        CSVFormat spyFormat = spy(CSVFormat.DEFAULT);

        // Prepare header array from TestEnum
        String[] headerArray = new String[]{"FIRST", "SECOND", "THIRD"};

        // Stub withHeader(String...) to return spyFormat when called with a String array equal to headerArray
        doReturn(spyFormat).when(spyFormat).withHeader(argThat(argument -> 
            argument != null && Arrays.equals(argument, headerArray)
        ));

        // Use reflection to invoke withHeader(Class<? extends Enum<?>>)
        Method method = CSVFormat.class.getDeclaredMethod("withHeader", Class.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(spyFormat, TestEnum.class);

        assertNotNull(result);
        verify(spyFormat).withHeader(argThat(argument -> 
            argument != null && Arrays.equals(argument, headerArray)
        ));
    }
}