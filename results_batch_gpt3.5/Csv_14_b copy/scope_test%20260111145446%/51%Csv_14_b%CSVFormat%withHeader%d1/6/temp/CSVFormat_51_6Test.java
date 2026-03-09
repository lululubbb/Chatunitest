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
import org.mockito.Mockito;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_51_6Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        Class<? extends Enum<?>> headerEnum = TestEnum.class;
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreEmptyLines(false)
                .withIgnoreSurroundingSpaces(true)
                .withRecordSeparator("\r\n");

        // When
        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", Class.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, headerEnum);

        // Then
        assertNotNull(result);
        assertArrayEquals(new String[]{"HEADER1", "HEADER2", "HEADER3"}, result.getHeader());
    }

    private enum TestEnum {
        HEADER1, HEADER2, HEADER3
    }
}