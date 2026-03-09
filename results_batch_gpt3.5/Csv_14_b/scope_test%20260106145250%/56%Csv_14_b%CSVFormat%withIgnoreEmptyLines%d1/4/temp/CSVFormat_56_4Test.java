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

public class CSVFormat_56_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getIgnoreEmptyLines(), "DEFAULT format should NOT ignore empty lines");

        CSVFormat result = original.withIgnoreEmptyLines(true);
        assertNotNull(result);
        assertTrue(result.getIgnoreEmptyLines(), "Resulting format should ignore empty lines");

        // The returned instance should NOT be the same if original ignoreEmptyLines was false
        assertNotSame(original, result, "withIgnoreEmptyLines(true) should return new instance if ignoreEmptyLines was false");

        CSVFormat result2 = result.withIgnoreEmptyLines(true);
        assertSame(result, result2, "withIgnoreEmptyLines(true) should return same instance if ignoreEmptyLines is true");
    }
}