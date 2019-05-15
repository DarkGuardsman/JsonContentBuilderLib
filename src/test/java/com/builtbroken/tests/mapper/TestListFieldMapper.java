package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.converter.ConverterRefs;
import com.builtbroken.builder.mapper.anno.JsonMapping;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;

/**
 * Lists have a special case in the field mapper that need tested
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-15.
 */
public class TestListFieldMapper
{

    private static JsonArray testData;

    @Test
    public void testArrayList()
    {
        //map
        ClassForMappingTest object = new ClassForMappingTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, createData("arrayList"), false);

        //Test
        Assertions.assertNotNull(object.arrayList);
        Assertions.assertEquals(testData.size(), object.arrayList.size());
        Assertions.assertEquals(testData.get(0).getAsString(), object.arrayList.get(0));
        Assertions.assertEquals(testData.get(1).getAsString(), object.arrayList.get(1));
    }

    @Test
    public void testLinkedList()
    {
        //map
        ClassForMappingTest object = new ClassForMappingTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, createData("linkedList"), false);

        //Test
        Assertions.assertNotNull(object.linkedList);
        Assertions.assertEquals(testData.size(), object.linkedList.size());
        Assertions.assertEquals(testData.get(0).getAsString(), object.linkedList.get(0));
        Assertions.assertEquals(testData.get(1).getAsString(), object.linkedList.get(1));
    }

    @Test
    public void testQueue()
    {
        //map
        ClassForMappingTest object = new ClassForMappingTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, createData("queue"), false);

        //Test
        Assertions.assertNotNull(object.queue);
        Assertions.assertEquals(testData.size(), object.queue.size());
        Assertions.assertEquals(testData.get(0).getAsString(), object.queue.poll()); //A
        Assertions.assertEquals(testData.get(1).getAsString(), object.queue.poll()); //B
    }

    @Test
    public void testStack()
    {
        //map
        ClassForMappingTest object = new ClassForMappingTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, createData("stack"), false);

        //Test
        Assertions.assertNotNull(object.stack);
        Assertions.assertEquals(testData.size(), object.stack.size());
        Assertions.assertEquals(testData.get(1).getAsString(), object.stack.pop()); //B
        Assertions.assertEquals(testData.get(0).getAsString(), object.stack.pop()); //A
    }

    @Test
    public void testConsumer()
    {
        //map
        ClassForMappingTest object = new ClassForMappingTest();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, createData("consumer"), false);

        //Test
        Assertions.assertNotNull(object.out);
        Assertions.assertEquals(testData.size(), object.out.size());
        Assertions.assertEquals(testData.get(0).getAsString(), object.out.get(0));
        Assertions.assertEquals(testData.get(1).getAsString(), object.out.get(1));
    }

    @Test
    public void testNullError()
    {
        Assertions.assertThrows(RuntimeException.class, () ->
        {
            ClassForMappingTest object = new ClassForMappingTest();
            ContentBuilderLib.getMainLoader().jsonMappingHandler.map("testClass", object, createData("error"), false);
        });
    }

    @BeforeAll
    public static void setup()
    {
        //Setup
        ContentBuilderLib.getMainLoader().setup();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.register(ClassForMappingTest.class, "testClass");

        testData = new JsonArray();
        testData.add("a");
        testData.add("b");
    }

    private JsonObject createData(String key)
    {
        JsonObject data = new JsonObject();
        data.add(key, testData.deepCopy());
        return data;
    }

    @AfterAll
    public static void cleanup()
    {
        //Cleanup
        ContentBuilderLib.destroy();
    }

    private static class ClassForMappingTest
    {

        @JsonMapping(keys = "error", type = ConverterRefs.LIST, args = ConverterRefs.STRING)
        public List<String> errorField;

        @JsonMapping(keys = "arrayList", type = ConverterRefs.LIST, args = ConverterRefs.STRING)
        public final ArrayList<String> arrayList = new ArrayList();

        @JsonMapping(keys = "linkedList", type = ConverterRefs.LIST, args = ConverterRefs.STRING)
        public final LinkedList<String> linkedList = new LinkedList();

        @JsonMapping(keys = "queue", type = ConverterRefs.LIST, args = ConverterRefs.STRING)
        public final Queue<String> queue = new LinkedList();

        @JsonMapping(keys = "stack", type = ConverterRefs.LIST, args = ConverterRefs.STRING)
        public final Stack<String> stack = new Stack();

        public final List<String> out = new ArrayList();

        @JsonMapping(keys = "consumer", type = ConverterRefs.LIST, args = ConverterRefs.STRING)
        public final Consumer<String> consumer = (text) -> out.add(text);
    }
}
