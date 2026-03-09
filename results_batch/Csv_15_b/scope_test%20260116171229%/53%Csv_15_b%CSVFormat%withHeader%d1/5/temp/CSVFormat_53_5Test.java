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

class CSVFormat_53_5Test {

    private enum TestEnum {
        ALPHA, BETA, GAMMA
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithNullEnum() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader((Class<? extends Enum<?>>) null);
        // withHeader(null) should call withHeader(String[] header) with null, so header remains null
        assertNotNull(result);
        assertNull(result.getHeader());
        // The returned instance should be different than baseFormat (immutable pattern)
        assertNotSame(baseFormat, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithEnum() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat result = baseFormat.withHeader(TestEnum.class);
        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(TestEnum.values().length, header.length);
        for (int i = 0; i < header.length; i++) {
            assertEquals(TestEnum.values()[i].name(), header[i]);
        }
        // The returned instance should be different than baseFormat (immutable pattern)
        assertNotSame(baseFormat, result);
    }
}