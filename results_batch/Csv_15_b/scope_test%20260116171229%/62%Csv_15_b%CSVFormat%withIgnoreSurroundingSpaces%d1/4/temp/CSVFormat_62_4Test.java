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

class CSVFormat_62_4Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Call focal method without parameter
        CSVFormat result = format.withIgnoreSurroundingSpaces();

        assertNotNull(result, "Resulting CSVFormat should not be null");
        assertTrue(result.getIgnoreSurroundingSpaces(), "ignoreSurroundingSpaces should be true");

        // Original format should remain unchanged (immutable)
        assertFalse(format.getIgnoreSurroundingSpaces(), "Original format ignoreSurroundingSpaces should be false");

        // Calling again on the result should keep the flag true
        CSVFormat result2 = result.withIgnoreSurroundingSpaces();
        assertTrue(result2.getIgnoreSurroundingSpaces(), "ignoreSurroundingSpaces should remain true");
        // The result2 should be equal to result
        assertEquals(result, result2, "Calling withIgnoreSurroundingSpaces on already true should return same config");
    }
}