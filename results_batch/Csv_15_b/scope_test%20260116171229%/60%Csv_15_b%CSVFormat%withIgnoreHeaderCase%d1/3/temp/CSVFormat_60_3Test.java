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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_60_3Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase_noArg() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreHeaderCase();

        assertNotNull(modified);
        assertTrue(modified.getIgnoreHeaderCase());
        // The original instance should remain unchanged (immutability)
        assertFalse(original.getIgnoreHeaderCase());
        // The returned instance should not be the same as the original
        assertNotSame(original, modified);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase_boolean_true_false() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getMethod("withIgnoreHeaderCase", boolean.class);

        CSVFormat trueFormat = (CSVFormat) method.invoke(original, true);
        CSVFormat falseFormat = (CSVFormat) method.invoke(original, false);

        assertNotNull(trueFormat);
        assertNotNull(falseFormat);

        assertTrue(trueFormat.getIgnoreHeaderCase());
        assertFalse(falseFormat.getIgnoreHeaderCase());

        // When setting the same value as original, it may return the same instance
        CSVFormat sameTrueFormat = (CSVFormat) method.invoke(trueFormat, true);
        assertSame(trueFormat, sameTrueFormat);

        CSVFormat sameFalseFormat = (CSVFormat) method.invoke(falseFormat, false);
        assertSame(falseFormat, sameFalseFormat);
    }
}