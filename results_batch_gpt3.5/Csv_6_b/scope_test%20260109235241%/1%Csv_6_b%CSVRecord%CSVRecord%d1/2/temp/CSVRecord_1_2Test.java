package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_1_2Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[] { "value1", "value2" };
        comment = "This is a comment";
        recordNumber = 123L;
    }

    @Test
    @Timeout(8000)
    public void testConstructorAndGetters() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // get by index
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(2));

        // get by name
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
        assertThrows(IllegalArgumentException.class, () -> record.get("unknown"));

        // get by enum
        assertEquals("value1", record.get(TestEnum.header1));
        assertEquals("value2", record.get(TestEnum.header2));

        // getComment and getRecordNumber
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    public enum TestEnum { header1, header2 }

    @Test
    @Timeout(8000)
    public void testIsConsistent() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());

        // inconsistent values length (simulate by reflection)
        String[] inconsistentValues = new String[] { "value1" };
        CSVRecord record2 = new CSVRecord(inconsistentValues, mapping, comment, recordNumber);
        assertFalse(record2.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsMappedAndIsSet() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isMapped("header1"));
        assertFalse(record.isMapped("unknown"));

        assertTrue(record.isSet("header1"));
        assertFalse(record.isSet("unknown"));

        // value null or empty string
        String[] vals = new String[] { null, "" };
        CSVRecord record2 = new CSVRecord(vals, mapping, comment, recordNumber);
        assertFalse(record2.isSet("header1"));
        assertFalse(record2.isSet("header2"));
    }

    @Test
    @Timeout(8000)
    public void testIteratorAndSize() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Iterator<String> it = record.iterator();
        assertNotNull(it);

        int count = 0;
        while (it.hasNext()) {
            String val = it.next();
            assertEquals(values[count], val);
            count++;
        }
        assertEquals(values.length, count);

        assertEquals(values.length, record.size());
    }

    @Test
    @Timeout(8000)
    public void testPutIn() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = record.putIn(map);
        assertSame(map, result);
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testToList() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(record);

        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    public void testToMap() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = record.toMap();

        assertEquals(2, map.size());
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testToString() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        String str = record.toString();
        assertNotNull(str);
        assertTrue(str.contains("value1"));
        assertTrue(str.contains("value2"));
    }

    @Test
    @Timeout(8000)
    public void testValuesArray() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(record);
        assertArrayEquals(values, vals);
    }

    @Test
    @Timeout(8000)
    public void testConstructorNullValues() throws Exception {
        CSVRecord record = new CSVRecord(null, mapping, comment, recordNumber);
        // values should be empty array, not null
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(record);
        assertNotNull(vals);
        assertEquals(0, vals.length);
    }
}