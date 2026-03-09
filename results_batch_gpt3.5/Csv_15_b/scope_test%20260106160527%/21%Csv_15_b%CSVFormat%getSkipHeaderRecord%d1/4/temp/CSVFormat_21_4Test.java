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
import java.lang.reflect.Field;

class CSVFormat_21_4Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_DefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithSkipHeaderRecordTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithSkipHeaderRecordFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_ConstantFormats() throws Exception {
        assertFalse(getSkipHeaderRecordUsingReflection(CSVFormat.EXCEL));
        assertFalse(getSkipHeaderRecordUsingReflection(CSVFormat.INFORMIX_UNLOAD));
        assertFalse(getSkipHeaderRecordUsingReflection(CSVFormat.INFORMIX_UNLOAD_CSV));
        assertFalse(getSkipHeaderRecordUsingReflection(CSVFormat.MYSQL));
        assertFalse(getSkipHeaderRecordUsingReflection(CSVFormat.POSTGRESQL_CSV));
        assertFalse(getSkipHeaderRecordUsingReflection(CSVFormat.POSTGRESQL_TEXT));
        assertFalse(getSkipHeaderRecordUsingReflection(CSVFormat.RFC4180));
        assertFalse(getSkipHeaderRecordUsingReflection(CSVFormat.TDF));
    }

    private boolean getSkipHeaderRecordUsingReflection(CSVFormat format) throws Exception {
        Field field = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        field.setAccessible(true);
        return field.getBoolean(format);
    }
}