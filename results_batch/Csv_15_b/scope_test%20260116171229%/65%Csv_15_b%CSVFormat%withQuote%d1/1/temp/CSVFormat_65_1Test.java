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

public class CSVFormat_65_1Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() {
        // Using a normal quote character
        CSVFormat format = CSVFormat.DEFAULT.withQuote(Character.valueOf('"'));
        assertNotNull(format);
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());

        // Using a different quote character
        CSVFormat format2 = CSVFormat.DEFAULT.withQuote(Character.valueOf('\''));
        assertNotNull(format2);
        assertEquals(Character.valueOf('\''), format2.getQuoteCharacter());

        // Using a control character as quote
        CSVFormat format3 = CSVFormat.DEFAULT.withQuote(Character.valueOf('\n'));
        assertNotNull(format3);
        assertEquals(Character.valueOf('\n'), format3.getQuoteCharacter());

        // Using the null char as quote (edge case)
        CSVFormat format4 = CSVFormat.DEFAULT.withQuote(Character.valueOf('\0'));
        assertNotNull(format4);
        assertEquals(Character.valueOf('\0'), format4.getQuoteCharacter());
    }
}