package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CSVFormat_11_2Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_DefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // getAllowMissingColumnNames is public, so no need for reflection, but kept for test style
        Method method = CSVFormat.class.getMethod("getAllowMissingColumnNames");
        boolean result = (boolean) method.invoke(format);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_TrueWhenSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        Method method = CSVFormat.class.getMethod("getAllowMissingColumnNames");
        boolean result = (boolean) method.invoke(format);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_FalseWhenSetFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        Method method = CSVFormat.class.getMethod("getAllowMissingColumnNames");
        boolean result = (boolean) method.invoke(format);
        assertFalse(result);
    }
}