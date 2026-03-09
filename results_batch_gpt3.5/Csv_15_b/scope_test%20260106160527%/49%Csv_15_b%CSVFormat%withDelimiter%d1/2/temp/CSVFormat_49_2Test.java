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

public class CSVFormat_49_2Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiterValid() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat updated = original.withDelimiter(newDelimiter);

        assertNotNull(updated);
        assertEquals(newDelimiter, updated.getDelimiter());
        // original remains unchanged
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), original.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiterLineBreakCharCR() {
        CSVFormat original = CSVFormat.DEFAULT;
        char lineBreak = '\r'; // CR

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(lineBreak);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiterLineBreakCharLF() {
        CSVFormat original = CSVFormat.DEFAULT;
        char lineBreak = '\n'; // LF

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(lineBreak);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiterLineBreakCharCRLF() {
        CSVFormat original = CSVFormat.DEFAULT;
        char cr = '\r';
        char lf = '\n';

        IllegalArgumentException thrownCR = assertThrows(IllegalArgumentException.class, () -> original.withDelimiter(cr));
        assertEquals("The delimiter cannot be a line break", thrownCR.getMessage());

        IllegalArgumentException thrownLF = assertThrows(IllegalArgumentException.class, () -> original.withDelimiter(lf));
        assertEquals("The delimiter cannot be a line break", thrownLF.getMessage());
    }
}