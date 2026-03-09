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

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatWithHeaderTest {

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }

    private enum EmptyEnum {
    }

    @Test
    @Timeout(8000)
    void withHeader_NullEnum_ReturnsFormatWithNullHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((Class<? extends Enum<?>>) null);
        assertNotNull(result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void withHeader_EmptyEnum_ReturnsFormatWithEmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(EmptyEnum.class);
        assertNotNull(result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);
    }

    @Test
    @Timeout(8000)
    void withHeader_NonEmptyEnum_ReturnsFormatWithHeaderNames() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(TestEnum.class);
        assertNotNull(result);
        assertNotNull(result.getHeader());
        String[] header = result.getHeader();
        assertEquals(TestEnum.values().length, header.length);
        for (int i = 0; i < header.length; i++) {
            assertEquals(TestEnum.values()[i].name(), header[i]);
        }
    }
}