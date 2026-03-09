package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeTypeAdapterFactory_49_3Test {

    private RuntimeTypeAdapterFactory<Object> factory;
    private Gson gson;

    private static final String TYPE_FIELD = "type";

    static class Base {}
    static class SubtypeA extends Base {}
    static class SubtypeB extends Base {}

    @BeforeEach
    public void setUp() throws Exception {
        // Create instance of RuntimeTypeAdapterFactory with baseType Base.class, typeFieldName "type", maintainType false
        factory = createFactoryInstance(Base.class, TYPE_FIELD, false);
        // Register subtypes
        factory.labelToSubtype.put("a", SubtypeA.class);
        factory.subtypeToLabel.put(SubtypeA.class, "a");
        factory.labelToSubtype.put("b", SubtypeB.class);
        factory.subtypeToLabel.put(SubtypeB.class, "b");

        gson = mock(Gson.class);
    }

    private RuntimeTypeAdapterFactory<Object> createFactoryInstance(Class<?> baseType, String typeFieldName, boolean maintainType) throws Exception {
        // Use reflection to call private constructor
        var ctor = RuntimeTypeAdapterFactory.class.getDeclaredConstructor(Class.class, String.class, boolean.class);
        ctor.setAccessible(true);
        @SuppressWarnings("unchecked")
        RuntimeTypeAdapterFactory<Object> instance = (RuntimeTypeAdapterFactory<Object>) ctor.newInstance(baseType, typeFieldName, maintainType);
        return instance;
    }

    @Test
    @Timeout(8000)
    public void create_nullTypeToken_returnsNull() {
        assertNull(factory.create(gson, null));
    }

    @Test
    @Timeout(8000)
    public void create_typeNotHandled_returnsNull() {
        // Create TypeToken of unrelated class
        TypeToken<String> stringType = TypeToken.get(String.class);
        assertNull(factory.create(gson, stringType));
    }

    @Test
    @Timeout(8000)
    public void create_recognizeSubtypesTrue_handlesSubtypes() throws Exception {
        factory.recognizeSubtypes();
        TypeToken<SubtypeA> typeToken = TypeToken.get(SubtypeA.class);

        // Setup gson.getAdapter(JsonElement.class) mock
        TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        // Setup delegate adapters for subtypes
        TypeAdapter<SubtypeA> delegateA = mock(TypeAdapter.class);
        TypeAdapter<SubtypeB> delegateB = mock(TypeAdapter.class);
        when(gson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateA);
        when(gson.getDelegateAdapter(factory, TypeToken.get(SubtypeB.class))).thenReturn(delegateB);

        TypeAdapter<SubtypeA> adapter = factory.create(gson, typeToken);
        assertNotNull(adapter);

        // Test that adapter is nullSafe typeadapter
        assertNotNull(adapter.nullSafe());

        // Setup JsonReader and JsonWriter mocks for read/write tests
        JsonReader reader = mock(JsonReader.class);
        JsonWriter writer = mock(JsonWriter.class);

        // Prepare JsonObject with type field for read
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TYPE_FIELD, "a");
        JsonElement jsonElement = jsonObject;

        // Mock jsonElementAdapter.read(reader) returns jsonElement
        when(jsonElementAdapter.read(reader)).thenReturn(jsonElement);

        // Setup delegateA.fromJsonTree to return a SubtypeA instance
        SubtypeA instanceA = new SubtypeA();
        when(delegateA.fromJsonTree(jsonElement)).thenReturn(instanceA);

        // Test read method returns the correct instance
        SubtypeA readResult = adapter.read(reader);
        assertSame(instanceA, readResult);

        // Prepare to test write method
        SubtypeA value = new SubtypeA();
        JsonObject delegateJsonObject = new JsonObject();
        when(delegateA.toJsonTree(value)).thenReturn(delegateJsonObject);

        // jsonElementAdapter.write(writer, any(JsonObject.class)) should do nothing
        doNothing().when(jsonElementAdapter).write(any(), any());

        // Call write - should call delegate and write json with type field
        adapter.write(writer, value);

        // Verify delegate.toJsonTree called
        verify(delegateA).toJsonTree(value);
        // Verify jsonElementAdapter.write called with a JsonObject containing type field
        ArgumentCaptor<JsonObject> jsonObjectCaptor = ArgumentCaptor.forClass(JsonObject.class);
        verify(jsonElementAdapter).write(eq(writer), jsonObjectCaptor.capture());

        JsonObject writtenObject = jsonObjectCaptor.getValue();
        assertTrue(writtenObject.has(TYPE_FIELD));
        assertEquals("a", writtenObject.get(TYPE_FIELD).getAsString());
    }

    @Test
    @Timeout(8000)
    public void create_maintainTypeTrue_writeUsesOriginalJsonObject() throws Exception {
        factory = createFactoryInstance(Base.class, TYPE_FIELD, true);
        factory.labelToSubtype.put("a", SubtypeA.class);
        factory.subtypeToLabel.put(SubtypeA.class, "a");
        gson = mock(Gson.class);

        TypeToken<SubtypeA> typeToken = TypeToken.get(SubtypeA.class);

        TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        TypeAdapter<SubtypeA> delegateA = mock(TypeAdapter.class);
        when(gson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateA);

        TypeAdapter<SubtypeA> adapter = factory.create(gson, typeToken);
        assertNotNull(adapter);

        JsonReader reader = mock(JsonReader.class);
        JsonWriter writer = mock(JsonWriter.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TYPE_FIELD, "a");
        when(jsonElementAdapter.read(reader)).thenReturn(jsonObject);
        SubtypeA instanceA = new SubtypeA();
        when(delegateA.fromJsonTree(jsonObject)).thenReturn(instanceA);

        SubtypeA readResult = adapter.read(reader);
        assertSame(instanceA, readResult);

        JsonObject delegateJsonObject = new JsonObject();
        when(delegateA.toJsonTree(any())).thenReturn(delegateJsonObject);

        doNothing().when(jsonElementAdapter).write(writer, delegateJsonObject);

        adapter.write(writer, new SubtypeA());

        verify(delegateA).toJsonTree(any());
        verify(jsonElementAdapter).write(writer, delegateJsonObject);
    }

    @Test
    @Timeout(8000)
    public void create_read_missingTypeField_throwsJsonParseException() throws IOException {
        TypeToken<Base> typeToken = TypeToken.get(Base.class);

        TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        TypeAdapter<Base> delegateA = mock(TypeAdapter.class);
        when(gson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateA);
        factory.labelToSubtype.put("a", SubtypeA.class);
        factory.subtypeToLabel.put(SubtypeA.class, "a");

        TypeAdapter<Base> adapter = factory.create(gson, typeToken);
        assertNotNull(adapter);

        JsonReader reader = mock(JsonReader.class);
        JsonObject jsonObject = new JsonObject(); // no type field
        when(jsonElementAdapter.read(reader)).thenReturn(jsonObject);

        JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(reader));
        assertTrue(ex.getMessage().contains("does not define a field named " + TYPE_FIELD));
    }

    @Test
    @Timeout(8000)
    public void create_read_unknownSubtypeLabel_throwsJsonParseException() throws IOException {
        TypeToken<Base> typeToken = TypeToken.get(Base.class);

        TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        factory.labelToSubtype.put("a", SubtypeA.class);
        factory.subtypeToLabel.put(SubtypeA.class, "a");

        TypeAdapter<Base> adapter = factory.create(gson, typeToken);
        assertNotNull(adapter);

        JsonReader reader = mock(JsonReader.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TYPE_FIELD, "unknown");
        when(jsonElementAdapter.read(reader)).thenReturn(jsonObject);

        JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(reader));
        assertTrue(ex.getMessage().contains("did you forget to register a subtype?"));
    }

    @Test
    @Timeout(8000)
    public void create_write_unknownSubtype_throwsJsonParseException() throws IOException {
        TypeToken<Base> typeToken = TypeToken.get(Base.class);

        TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        factory.labelToSubtype.put("a", SubtypeA.class);
        factory.subtypeToLabel.put(SubtypeA.class, "a");

        TypeAdapter<Base> adapter = factory.create(gson, typeToken);
        assertNotNull(adapter);

        Base unknownSubtype = new Base() {};

        JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.write(mock(JsonWriter.class), unknownSubtype));
        assertTrue(ex.getMessage().contains("did you forget to register a subtype?"));
    }

    @Test
    @Timeout(8000)
    public void create_write_serializeFieldNameConflict_throwsJsonParseException() throws IOException {
        TypeToken<Base> typeToken = TypeToken.get(Base.class);

        TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        TypeAdapter<SubtypeA> delegateA = mock(TypeAdapter.class);
        when(gson.getDelegateAdapter(factory, TypeToken.get(SubtypeA.class))).thenReturn(delegateA);

        factory.labelToSubtype.put("a", SubtypeA.class);
        factory.subtypeToLabel.put(SubtypeA.class, "a");

        TypeAdapter<Base> adapter = factory.create(gson, typeToken);
        assertNotNull(adapter);

        SubtypeA instanceA = new SubtypeA();
        JsonObject jsonObjectWithTypeField = new JsonObject();
        jsonObjectWithTypeField.addProperty(TYPE_FIELD, "someValue");

        when(delegateA.toJsonTree(instanceA)).thenReturn(jsonObjectWithTypeField);

        JsonWriter writer = mock(JsonWriter.class);

        JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.write(writer, instanceA));
        assertTrue(ex.getMessage().contains("already defines a field named " + TYPE_FIELD));
    }
}