package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LinkedTreeMapRemoveInternalTest {

    private LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @SuppressWarnings("unchecked")
    private Node<String, String> createNode(String key, String value) {
        Node<String, String> node = mock(Node.class);
        when(node.key).thenReturn(key);
        when(node.value).thenReturn(value);
        when(node.height).thenReturn(1);
        when(node.left).thenReturn(null);
        when(node.right).thenReturn(null);
        when(node.parent).thenReturn(null);
        when(node.prev).thenReturn(null);
        when(node.next).thenReturn(null);
        return node;
    }

    private Node<String, String> createRealNode(String key, String value) {
        // Using reflection to create a real Node instance since constructor is package-private
        try {
            Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");
            // Node constructor: LinkedTreeMap.Node(LinkedTreeMap, K key, V value, Node<K,V> parent)
            return (Node<String, String>) nodeClass.getDeclaredConstructor(
                    LinkedTreeMap.class, Object.class, Object.class, nodeClass)
                    .newInstance(map, key, value, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method getRemoveInternalMethod() throws NoSuchMethodException {
        Method method = LinkedTreeMap.class.getDeclaredMethod("removeInternal", Node.class, boolean.class);
        method.setAccessible(true);
        return method;
    }

    private Method getReplaceInParentMethod() throws NoSuchMethodException {
        Method method = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", Node.class, Node.class);
        method.setAccessible(true);
        return method;
    }

    private Method getRebalanceMethod() throws NoSuchMethodException {
        Method method = LinkedTreeMap.class.getDeclaredMethod("rebalance", Node.class, boolean.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    @Timeout(8000)
    void testRemoveInternal_unlinkTrue_noChildren() throws Throwable {
        Node<String, String> node = createRealNode("key", "value");
        // Setup linked list prev and next nodes
        Node<String, String> prev = createRealNode("prev", "prevVal");
        Node<String, String> next = createRealNode("next", "nextVal");
        // Link them
        node.prev = prev;
        node.next = next;
        prev.next = node;
        next.prev = node;

        // Spy on map to verify replaceInParent, rebalance called
        LinkedTreeMap<String, String> spyMap = spy(map);

        Method removeInternal = getRemoveInternalMethod();

        // ReplaceInParent and rebalance are private, spy to verify calls
        doNothing().when(spyMap).replaceInParent(any(), any());
        doNothing().when(spyMap).rebalance(any(), anyBoolean());

        // Set size and modCount to test decrement and increment
        spyMap.size = 5;
        spyMap.modCount = 10;

        // Call removeInternal with unlink=true
        removeInternal.invoke(spyMap, node, true);

        // Verify unlink: prev.next and next.prev updated
        assertSame(next, prev.next);
        assertSame(prev, next.prev);

        // Verify replaceInParent called with node and null (no children)
        verify(spyMap).replaceInParent(node, null);

        // Verify rebalance called with originalParent == null and insert false
        verify(spyMap).rebalance(null, false);

        // Verify size decremented and modCount incremented
        assertEquals(4, spyMap.size);
        assertEquals(11, spyMap.modCount);
    }

    @Test
    @Timeout(8000)
    void testRemoveInternal_unlinkFalse_leftChildOnly() throws Throwable {
        LinkedTreeMap<String, String> spyMap = spy(map);

        Node<String, String> node = createRealNode("key", "value");
        Node<String, String> left = createRealNode("left", "leftVal");

        node.left = left;
        left.parent = node;

        doNothing().when(spyMap).replaceInParent(any(), any());
        doNothing().when(spyMap).rebalance(any(), anyBoolean());

        spyMap.size = 3;
        spyMap.modCount = 7;

        Method removeInternal = getRemoveInternalMethod();

        removeInternal.invoke(spyMap, node, false);

        verify(spyMap).replaceInParent(node, left);
        assertNull(node.left);
        verify(spyMap).rebalance(node.parent, false);
        assertEquals(2, spyMap.size);
        assertEquals(8, spyMap.modCount);
    }

    @Test
    @Timeout(8000)
    void testRemoveInternal_unlinkFalse_rightChildOnly() throws Throwable {
        LinkedTreeMap<String, String> spyMap = spy(map);

        Node<String, String> node = createRealNode("key", "value");
        Node<String, String> right = createRealNode("right", "rightVal");

        node.right = right;
        right.parent = node;

        doNothing().when(spyMap).replaceInParent(any(), any());
        doNothing().when(spyMap).rebalance(any(), anyBoolean());

        spyMap.size = 3;
        spyMap.modCount = 7;

        Method removeInternal = getRemoveInternalMethod();

        removeInternal.invoke(spyMap, node, false);

        verify(spyMap).replaceInParent(node, right);
        assertNull(node.right);
        verify(spyMap).rebalance(node.parent, false);
        assertEquals(2, spyMap.size);
        assertEquals(8, spyMap.modCount);
    }

    @Test
    @Timeout(8000)
    void testRemoveInternal_unlinkFalse_bothChildren_leftHigher() throws Throwable {
        LinkedTreeMap<String, String> spyMap = spy(map);

        // Create nodes
        Node<String, String> node = createRealNode("key", "value");
        Node<String, String> left = createRealNode("left", "leftVal");
        Node<String, String> right = createRealNode("right", "rightVal");

        node.left = left;
        node.right = right;
        left.parent = node;
        right.parent = node;

        // Setup heights: left.height > right.height
        left.height = 3;
        right.height = 2;

        // Setup left.last() returns adjacent
        Node<String, String> adjacent = createRealNode("adjacent", "adjVal");
        adjacent.height = 1;

        // Spy adjacent's last() method - it's a real method, so we must override
        // We cannot mock final methods easily, so we use reflection to set left.last() to adjacent
        // Instead, we patch left.last() by subclassing Node with overridden last()
        Node<String, String> leftSpy = new Node<String, String>(map, "left", "leftVal", node) {
            @Override
            Node<String, String> last() {
                return adjacent;
            }
        };
        leftSpy.height = 3;
        leftSpy.parent = node;
        node.left = leftSpy;

        // Spy map methods
        doNothing().when(spyMap).replaceInParent(any(), any());
        doNothing().when(spyMap).rebalance(any(), anyBoolean());

        // Spy removeInternal(adjacent, false) call: we override removeInternal to call real method for adjacent
        Method removeInternal = getRemoveInternalMethod();

        // To verify recursive call, spy map and call real method on adjacent
        doCallRealMethod().when(spyMap).removeInternal(eq(adjacent), eq(false));

        spyMap.size = 10;
        spyMap.modCount = 20;

        removeInternal.invoke(spyMap, node, false);

        // Verify adjacent replaced node in parent
        verify(spyMap).replaceInParent(node, adjacent);

        // Verify adjacent.left == left and adjacent.right == right
        assertSame(leftSpy, adjacent.left);
        assertSame(right, adjacent.right);

        // Verify left.parent and right.parent updated
        assertSame(adjacent, leftSpy.parent);
        assertSame(adjacent, right.parent);

        // Verify node.left and node.right set to null
        assertNull(node.left);
        assertNull(node.right);

        // Verify adjacent.height updated correctly
        int expectedHeight = Math.max(leftSpy.height, right.height) + 1;
        assertEquals(expectedHeight, adjacent.height);

        // Verify size decremented and modCount incremented
        assertEquals(9, spyMap.size);
        assertEquals(21, spyMap.modCount);
    }

    @Test
    @Timeout(8000)
    void testRemoveInternal_unlinkTrue_bothChildren_rightHigher() throws Throwable {
        LinkedTreeMap<String, String> spyMap = spy(map);

        // Setup linked list nodes for unlink
        Node<String, String> node = createRealNode("node", "val");
        Node<String, String> prev = createRealNode("prev", "prevVal");
        Node<String, String> next = createRealNode("next", "nextVal");
        node.prev = prev;
        node.next = next;
        prev.next = node;
        next.prev = node;

        // Setup children
        Node<String, String> left = createRealNode("left", "leftVal");
        Node<String, String> right = createRealNode("right", "rightVal");
        node.left = left;
        node.right = right;
        left.parent = node;
        right.parent = node;

        left.height = 1;
        right.height = 4;

        // Setup right.first() returns adjacent
        Node<String, String> adjacent = createRealNode("adjacent", "adjVal");
        adjacent.height = 2;

        Node<String, String> rightSpy = new Node<String, String>(map, "right", "rightVal", node) {
            @Override
            Node<String, String> first() {
                return adjacent;
            }
        };
        rightSpy.height = 4;
        rightSpy.parent = node;
        node.right = rightSpy;

        // Spy map methods
        doNothing().when(spyMap).replaceInParent(any(), any());
        doNothing().when(spyMap).rebalance(any(), anyBoolean());

        doCallRealMethod().when(spyMap).removeInternal(eq(adjacent), eq(false));

        spyMap.size = 7;
        spyMap.modCount = 15;

        Method removeInternal = getRemoveInternalMethod();

        removeInternal.invoke(spyMap, node, true);

        // Verify unlink performed
        assertSame(next, prev.next);
        assertSame(prev, next.prev);

        // Verify adjacent replaced node
        verify(spyMap).replaceInParent(node, adjacent);

        // Verify adjacent.left == left and adjacent.right == rightSpy
        assertSame(left, adjacent.left);
        assertSame(rightSpy, adjacent.right);

        // Verify parents updated
        assertSame(adjacent, left.parent);
        assertSame(adjacent, rightSpy.parent);

        // Verify node.left and node.right null
        assertNull(node.left);
        assertNull(node.right);

        // Verify adjacent.height updated
        int expectedHeight = Math.max(left.height, rightSpy.height) + 1;
        assertEquals(expectedHeight, adjacent.height);

        // Verify size decremented and modCount incremented
        assertEquals(6, spyMap.size);
        assertEquals(16, spyMap.modCount);
    }
}