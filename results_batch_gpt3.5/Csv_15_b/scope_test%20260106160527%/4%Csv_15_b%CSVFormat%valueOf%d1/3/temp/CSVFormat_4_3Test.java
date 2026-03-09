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
import org.junit.jupiter.api.function.Executable;

class CSVFormat_4_3Test {

    @Test
    @Timeout(8000)
    void testValueOf_withValidPredefinedFormats() {
        // Test all predefined format names in CSVFormat.Predefined enum
        for (CSVFormat.Predefined predefined : CSVFormat.Predefined.values()) {
            String name = predefined.name();
            CSVFormat format = CSVFormat.valueOf(name);
            assertNotNull(format, "CSVFormat.valueOf returned null for: " + name);
            assertEquals(predefined.getFormat(), format, "CSVFormat.valueOf did not return expected format for: " + name);
        }
    }

    @Test
    @Timeout(8000)
    void testValueOf_withInvalidFormatName() {
        String invalidName = "NON_EXISTENT_FORMAT_NAME";
        Executable call = () -> CSVFormat.valueOf(invalidName);
        assertThrows(IllegalArgumentException.class, call, "Expected IllegalArgumentException for invalid format name");
    }

    @Test
    @Timeout(8000)
    void testValueOf_nullInput() {
        Executable call = () -> CSVFormat.valueOf((String) null);
        assertThrows(NullPointerException.class, call, "Expected NullPointerException for null input");
    }
}