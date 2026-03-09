package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

public class CSVRecord_5_2Test {

    @Test
    @Timeout(8000)
    public void testGetComment_NonNullComment() {
        String comment = "This is a comment";
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, comment, 1L);

        assertEquals(comment, record.getComment());
    }

    @Test
    @Timeout(8000)
    public void testGetComment_NullComment() {
        String comment = null;
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, comment, 1L);

        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    public void testInvokeGetCommentViaReflection() throws Exception {
        String comment = "reflection comment";
        String[] values = new String[] {"val1"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, comment, 10L);

        Method getCommentMethod = CSVRecord.class.getDeclaredMethod("getComment");
        getCommentMethod.setAccessible(true);
        Object result = getCommentMethod.invoke(record);

        assertEquals(comment, result);
    }
}