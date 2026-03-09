package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_40_6Test {

    @Test
    @Timeout(8000)
    void testWithHeaderComments_NullAndEmpty() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat resultNull = format.withHeaderComments((Object[]) null);
        assertNotNull(resultNull);
        assertNotSame(format, resultNull);
        assertNull(resultNull.getHeaderComments());

        CSVFormat resultEmpty = format.withHeaderComments(new Object[0]);
        assertNotNull(resultEmpty);
        assertNotSame(format, resultEmpty);
        assertNull(resultEmpty.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_SingleAndMultiple() {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat single = format.withHeaderComments((Object) "comment1");
        assertNotNull(single);
        assertNotSame(format, single);
        assertArrayEquals(new String[]{"comment1"}, single.getHeaderComments());

        CSVFormat multiple = format.withHeaderComments((Object[]) new String[]{"comment1", "comment2", "3"});
        assertNotNull(multiple);
        assertNotSame(format, multiple);
        assertArrayEquals(new String[]{"comment1", "comment2", "3"}, multiple.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderComments_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments((Object) "a");
        CSVFormat changed = format.withHeaderComments((Object) "b");
        assertNotSame(format, changed);
        assertArrayEquals(new String[]{"a"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"b"}, changed.getHeaderComments());
    }

}