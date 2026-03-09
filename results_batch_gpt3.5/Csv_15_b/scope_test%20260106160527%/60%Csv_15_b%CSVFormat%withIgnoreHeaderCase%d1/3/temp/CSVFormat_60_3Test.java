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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class CSVFormat_60_3Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_noArg_callsWithIgnoreHeaderCaseTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat spyFormat = Mockito.spy(original);

        // Mock the withIgnoreHeaderCase(boolean) to return a known CSVFormat instance
        CSVFormat expectedFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        Mockito.doReturn(expectedFormat).when(spyFormat).withIgnoreHeaderCase(true);

        // Call the focal method
        CSVFormat result = spyFormat.withIgnoreHeaderCase();

        // Verify that withIgnoreHeaderCase(true) was called exactly once
        Mockito.verify(spyFormat, Mockito.times(1)).withIgnoreHeaderCase(true);

        // Assert that the returned format is the expected one
        assertSame(expectedFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_reflectionInvoke() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getMethod("withIgnoreHeaderCase");

        Object result = method.invoke(format);

        assertNotNull(result);
        assertTrue(result instanceof CSVFormat);

        CSVFormat resultFormat = (CSVFormat) result;
        // The returned CSVFormat should have ignoreHeaderCase true
        assertTrue(resultFormat.getIgnoreHeaderCase());
    }
}