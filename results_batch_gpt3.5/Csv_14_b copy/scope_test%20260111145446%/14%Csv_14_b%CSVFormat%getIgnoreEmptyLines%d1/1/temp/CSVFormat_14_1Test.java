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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_14_1Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Create an instance of CSVFormat
        CSVFormat csvFormat = new CSVFormat(',', '"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);

        // Use reflection to access the private field 'ignoreEmptyLines'
        Method method = CSVFormat.class.getDeclaredMethod("getIgnoreEmptyLines");
        method.setAccessible(true);

        // Invoke the method and assert the return value
        boolean result = (boolean) method.invoke(csvFormat);
        assertEquals(true, result);
    }
}