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

class CSVRecord_1_4Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    void setUp() {
        values = new String[] {"val1", "val2", "val3"};
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        comment = "This is a comment";
        recordNumber = 42L;
    }

    @Test
    @Timeout(8000)
    void testConstructorAndGetters() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // get(int)
        assertEquals("val1", record.get(0));
        assertEquals("val2", record.get(1));
        assertEquals("val3", record.get(2));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(3));

        // get(String)
        assertEquals("val1", record.get("A"));
        assertEquals("val2", record.get("B"));
        assertEquals("val3", record.get("C"));
        assertThrows(IllegalArgumentException.class, () -> record.get("D"));
        assertThrows(NullPointerException.class, () -> record.get((String)null));

        // get(Enum<?>)
        Enum<?> e = mock(Enum.class);
        when(e.name()).thenReturn("B");
        assertEquals("val2", record.get(e));
        when(e.name()).thenReturn("D");
        assertThrows(IllegalArgumentException.class, () -> record.get(e));
        when(e.name()).thenReturn(null);
        assertThrows(NullPointerException.class, () -> record.get(e));

        // getComment
        assertEquals(comment, record.getComment());

        // getRecordNumber
        assertEquals(recordNumber, record.getRecordNumber());

        // size
        assertEquals(3, record.size());

        // isConsistent (values length == mapping size)
        assertTrue(record.isConsistent());
        // inconsistent case
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("A", 0);
        CSVRecord record2 = new CSVRecord(values, map2, null, 1L);
        assertFalse(record2.isConsistent());

        // isMapped
        assertTrue(record.isMapped("A"));
        assertFalse(record.isMapped("D"));
        assertThrows(NullPointerException.class, () -> record.isMapped(null));

        // isSet
        assertTrue(record.isSet("A"));
        assertFalse(record.isSet("D"));
        assertThrows(NullPointerException.class, () -> record.isSet(null));

        // iterator
        Iterator<String> it = record.iterator();
        List<String> iterated = new ArrayList<>();
        while (it.hasNext()) {
            iterated.add(it.next());
        }
        assertEquals(Arrays.asList(values), iterated);

        // toMap
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("val1", map.get("A"));
        assertEquals("val2", map.get("B"));
        assertEquals("val3", map.get("C"));

        // toString contains values
        String str = record.toString();
        assertTrue(str.contains("val1"));
        assertTrue(str.contains("val2"));
        assertTrue(str.contains("val3"));

        // putIn
        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = record.putIn(targetMap);
        assertSame(targetMap, result);
        assertEquals(map, targetMap);

        // values() method via reflection (package-private)
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        Object returned = valuesMethod.invoke(record);
        assertTrue(returned instanceof String[]);
        String[] returnedValues = (String[]) returned;
        assertArrayEquals(values, returnedValues);

        // toList() private method via reflection
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(record);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    void testConstructorWithNullValues() {
        CSVRecord record = new CSVRecord(null, mapping, comment, recordNumber);
        assertNotNull(record);
        assertEquals(0, record.size());
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testConstructorWithNullMapping() {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);
        assertNotNull(record);
        assertEquals(3, record.size());
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertFalse(record.isMapped("A"));
    }

}