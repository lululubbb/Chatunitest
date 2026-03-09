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

import java.lang.reflect.Method;

public class CSVFormat_60_6Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_noArg() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withIgnoreHeaderCase();

        assertNotNull(result);
        assertNotSame(original, result);
        assertTrue(result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_booleanArg_true() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        Method withIgnoreHeaderCaseBoolean = CSVFormat.class.getMethod("withIgnoreHeaderCase", boolean.class);

        CSVFormat resultTrue = (CSVFormat) withIgnoreHeaderCaseBoolean.invoke(original, Boolean.TRUE);
        assertNotNull(resultTrue);
        assertNotSame(original, resultTrue);
        assertTrue(resultTrue.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_booleanArg_false() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);

        Method withIgnoreHeaderCaseBoolean = CSVFormat.class.getMethod("withIgnoreHeaderCase", boolean.class);

        CSVFormat resultFalse = (CSVFormat) withIgnoreHeaderCaseBoolean.invoke(original, Boolean.FALSE);
        assertNotNull(resultFalse);
        assertNotSame(original, resultFalse);
        assertFalse(resultFalse.getIgnoreHeaderCase());
    }
}