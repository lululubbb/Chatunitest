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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_1_2Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        values = new String[]{"a", "b", "c"};
        mapping = new HashMap<>();
        mapping.put("one", 0);
        mapping.put("two", 1);
        mapping.put("three", 2);
        comment = "comment";
        recordNumber = 42L;
    }

    private CSVRecord createCSVRecord(String[] vals, Map<String, Integer> map, String comm, long recNum) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(vals, map, comm, recNum);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullValues() throws Exception {
        CSVRecord record = createCSVRecord(null, mapping, comment, recordNumber);
        assertEquals(0, record.size());
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(3));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
    }

    @Test
    @Timeout(8000)
    public void testGetByName() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertEquals("a", record.get("one"));
        assertEquals("b", record.get("two"));
        assertEquals("c", record.get("three"));
        assertThrows(IllegalArgumentException.class, () -> record.get((String) null));
        assertThrows(IllegalArgumentException.class, () -> record.get("four"));
    }

    private enum TestEnum {one, two, three}

    @Test
    @Timeout(8000)
    public void testGetByEnum() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        assertEquals("a", record.get(TestEnum.one));
        assertEquals("b", record.get(TestEnum.two));
        assertEquals("c", record.get(TestEnum.three));
        assertThrows(IllegalArgumentException.class, () -> record.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    public void testGetCommentAndRecordNumber() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());

        // inconsistent: values length different from mapping size
        String[] vals = new String[]{"a", "b"};
        CSVRecord record2 = createCSVRecord(vals, mapping, comment, recordNumber);
        assertFalse(record2.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsMapped() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isMapped("one"));
        assertFalse(record.isMapped("four"));
        assertFalse(record.isMapped(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isSet("one"));
        assertFalse(record.isSet("four"));
        assertFalse(record.isSet(null));

        String[] valsWithNull = new String[]{"a", null, "c"};
        CSVRecord record2 = createCSVRecord(valsWithNull, mapping, comment, recordNumber);
        assertFalse(record2.isSet("two"));
    }

    @Test
    @Timeout(8000)
    public void testIterator() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Iterator<String> it = record.iterator();
        List<String> iterated = new ArrayList<>();
        while(it.hasNext()) {
            iterated.add(it.next());
        }
        assertEquals(Arrays.asList(values), iterated);
    }

    @Test
    @Timeout(8000)
    public void testPutIn() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = record.putIn(map);
        assertSame(map, result);
        assertEquals(3, map.size());
        assertEquals("a", map.get("one"));
        assertEquals("b", map.get("two"));
        assertEquals("c", map.get("three"));
    }

    @Test
    @Timeout(8000)
    public void testSize() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertEquals(3, record.size());

        CSVRecord record2 = createCSVRecord(null, mapping, comment, recordNumber);
        assertEquals(0, record2.size());
    }

    @Test
    @Timeout(8000)
    public void testToList() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(record);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    public void testToMap() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("a", map.get("one"));
        assertEquals("b", map.get("two"));
        assertEquals("c", map.get("three"));
    }

    @Test
    @Timeout(8000)
    public void testToString() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        String str = record.toString();
        assertTrue(str.contains("a"));
        assertTrue(str.contains("b"));
        assertTrue(str.contains("c"));
        assertTrue(str.contains("comment"));
        assertTrue(str.contains(String.valueOf(recordNumber)));
    }

    @Test
    @Timeout(8000)
    public void testValues() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(record);
        assertArrayEquals(values, vals);
    }

}