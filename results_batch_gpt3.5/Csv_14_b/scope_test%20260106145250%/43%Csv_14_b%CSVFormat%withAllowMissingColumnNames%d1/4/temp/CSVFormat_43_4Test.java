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
import java.lang.reflect.Method;

class CSVFormat_43_4Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_noArg() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames();

        assertNotNull(newFormat, "Returned CSVFormat should not be null");
        assertTrue(newFormat.getAllowMissingColumnNames(), "allowMissingColumnNames should be true");
        // Ensure original is unchanged (immutable style)
        assertFalse(baseFormat.getAllowMissingColumnNames(), "Original CSVFormat allowMissingColumnNames should be false");
        // The returned instance should be different if the flag changes
        if (baseFormat.getAllowMissingColumnNames() != newFormat.getAllowMissingColumnNames()) {
            assertNotSame(baseFormat, newFormat, "New CSVFormat instance expected when flag changes");
        } else {
            assertSame(baseFormat, newFormat, "Same CSVFormat instance expected when flag unchanged");
        }
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_booleanTrue() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        // Use reflection to invoke public withAllowMissingColumnNames(boolean)
        Method method = CSVFormat.class.getMethod("withAllowMissingColumnNames", boolean.class);

        CSVFormat newFormat = (CSVFormat) method.invoke(baseFormat, true);

        assertNotNull(newFormat);
        assertTrue(newFormat.getAllowMissingColumnNames());
        assertFalse(baseFormat.getAllowMissingColumnNames());
        if (baseFormat.getAllowMissingColumnNames() != newFormat.getAllowMissingColumnNames()) {
            assertNotSame(baseFormat, newFormat);
        } else {
            assertSame(baseFormat, newFormat);
        }
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_booleanFalse() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        Method method = CSVFormat.class.getMethod("withAllowMissingColumnNames", boolean.class);

        CSVFormat newFormat = (CSVFormat) method.invoke(baseFormat, false);

        assertNotNull(newFormat);
        assertFalse(newFormat.getAllowMissingColumnNames());
        assertTrue(baseFormat.getAllowMissingColumnNames());
        if (baseFormat.getAllowMissingColumnNames() != newFormat.getAllowMissingColumnNames()) {
            assertNotSame(baseFormat, newFormat);
        } else {
            assertSame(baseFormat, newFormat);
        }
    }
}