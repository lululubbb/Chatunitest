package com.google.gson.graph;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

class GraphAdapterBuilder_546_3Test {

    private GraphAdapterBuilder graphAdapterBuilder;

    @BeforeEach
    void setUp() {
        graphAdapterBuilder = new GraphAdapterBuilder();
    }

    @Test
    @Timeout(8000)
    void testConstructor_initializesFields() throws Exception {
        // Using reflection to verify private fields are initialized properly
        Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
        instanceCreatorsField.setAccessible(true);
        Object instanceCreators = instanceCreatorsField.get(graphAdapterBuilder);
        assertNotNull(instanceCreators);
        assertTrue(instanceCreators instanceof Map);
        assertTrue(((Map<?, ?>) instanceCreators).isEmpty());

        Field constructorConstructorField = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
        constructorConstructorField.setAccessible(true);
        Object constructorConstructor = constructorConstructorField.get(graphAdapterBuilder);
        assertNotNull(constructorConstructor);
        // We cannot easily verify the internal state of ConstructorConstructor, just check not null
    }

    @Test
    @Timeout(8000)
    void testAddType_withoutInstanceCreator_returnsThisAndAddsType() throws Exception {
        Type mockType = String.class;
        GraphAdapterBuilder returned = graphAdapterBuilder.addType(mockType);
        assertSame(graphAdapterBuilder, returned);

        Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
        instanceCreatorsField.setAccessible(true);
        Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(graphAdapterBuilder);
        // Since addType(Type) adds a default InstanceCreator internally, the map should contain one entry
        assertEquals(1, instanceCreators.size());
        assertNotNull(instanceCreators.get(mockType));
    }

    @Test
    @Timeout(8000)
    void testAddType_withInstanceCreator_returnsThisAndAddsInstanceCreator() throws Exception {
        Type mockType = String.class;
        @SuppressWarnings("unchecked")
        InstanceCreator<String> instanceCreator = mock(InstanceCreator.class);

        GraphAdapterBuilder returned = graphAdapterBuilder.addType(mockType, instanceCreator);
        assertSame(graphAdapterBuilder, returned);

        Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
        instanceCreatorsField.setAccessible(true);
        Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(graphAdapterBuilder);
        assertEquals(1, instanceCreators.size());
        assertSame(instanceCreator, instanceCreators.get(mockType));
    }

    @Test
    @Timeout(8000)
    void testRegisterOn_callsGsonBuilderRegisterTypeAdapterFactory() {
        GsonBuilder gsonBuilder = mock(GsonBuilder.class);

        graphAdapterBuilder.registerOn(gsonBuilder);

        verify(gsonBuilder).registerTypeAdapterFactory(any());
    }
}