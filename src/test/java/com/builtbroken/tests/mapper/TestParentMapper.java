package com.builtbroken.tests.mapper;

import com.builtbroken.builder.ContentBuilderLib;
import com.builtbroken.builder.mapper.JsonMapping;
import com.builtbroken.builder.mapper.JsonMappingHandler;
import com.builtbroken.tests.UnitTestHelpers;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;

/**
 * Used to test mapping methods and fields of parent classes
 * Created by Dark(DarkGuardsman, Robert) on 2019-03-27.
 */
public class TestParentMapper
{

    private static final String LAYER_1 = "layer1";
    private static final String LAYER_2 = "layer2";
    private static final String LAYER_2A = "layer2A";
    private static final String LAYER_3 = "layer3";

    private static final String JSON_ID = "id";
    private static final String JSON_ID_VAL = "trees";

    private static final String JSON_PROP_A = "propA";
    private static final int JSON_PROP_A_VAL = 15;

    private static final String JSON_PROP_B = "propB";
    private static final int JSON_PROP_B_VAL = 45;

    private static final String JSON_PROP_C = "propC";
    private static final int[] JSON_PROP_C_VAL = new int[]{3, 5, 1, 2};

    private static final String JSON_PROP_D = "propD";
    private static final float JSON_PROP_D_VAL = 2.3345f;

    private static final String JSON_PROP_E = "propE";
    private static final String JSON_PROP_E_VAL = "unit";

    //https://www.baeldung.com/junit-before-beforeclass-beforeeach-beforeall
    //https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/
    private static JsonObject object;
    private static JsonObject testObject;

    @Test
    public void testLayer1()
    {
        Layer1 object = new Layer1();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map(LAYER_1, object, testObject, false);
        Assertions.assertEquals(JSON_ID_VAL, object.id);
        Assertions.assertEquals(JSON_PROP_A_VAL, object.propA);
    }

    @Test
    public void testLayer2()
    {
        Layer2 object = new Layer2();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map(LAYER_2, object, testObject, false);
        Assertions.assertEquals(JSON_ID_VAL, object.id);
        Assertions.assertEquals(JSON_PROP_A_VAL, object.propA);

        Assertions.assertEquals(JSON_PROP_B_VAL, object.propB);
        Assertions.assertEquals(JSON_PROP_D_VAL, object.propD);
    }

    @Test
    public void testLayer2A()
    {
        Layer2A object = new Layer2A();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map(LAYER_2, object, testObject, false);
        Assertions.assertEquals(JSON_ID_VAL, object.id);
        Assertions.assertEquals(JSON_PROP_A_VAL, object.propA);

        Assertions.assertEquals(JSON_PROP_E_VAL, object.propE);
    }

    @Test
    public void testLayer3()
    {
        Layer3 object = new Layer3();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.map(LAYER_2, object, testObject, false);
        Assertions.assertEquals(JSON_ID_VAL, object.id);
        Assertions.assertEquals(JSON_PROP_A_VAL, object.propA);

        Assertions.assertEquals(JSON_PROP_B_VAL, object.propB);
        Assertions.assertEquals(JSON_PROP_D_VAL, object.propD);

        Assertions.assertEquals(JSON_PROP_C_VAL, object.propC);
    }

    @AfterEach
    public void afterEach()
    {
        //Sanity check
        Assertions.assertEquals(object, testObject, "Test modified the JSON, mapping should be non-destructive");
    }

    @BeforeAll
    public static void setup()
    {
        ContentBuilderLib.setup();
        ContentBuilderLib.getMainLoader().jsonMappingHandler.register(Layer1.class, LAYER_1);
        ContentBuilderLib.getMainLoader().jsonMappingHandler.register(Layer2.class, LAYER_2);
        ContentBuilderLib.getMainLoader().jsonMappingHandler.register(Layer2A.class, LAYER_2A);
        ContentBuilderLib.getMainLoader().jsonMappingHandler.register(Layer3.class, LAYER_3);

        object = new JsonObject();
        object.addProperty(JSON_PROP_A, JSON_PROP_A_VAL);
        object.addProperty(JSON_ID, JSON_ID_VAL);

        object.addProperty(JSON_PROP_B, JSON_PROP_B_VAL);
        object.addProperty(JSON_PROP_D, JSON_PROP_D_VAL);
        object.add(JSON_PROP_C, UnitTestHelpers.createJsonIntArray(JSON_PROP_C_VAL));
        object.addProperty(JSON_PROP_E, JSON_PROP_E_VAL);

        testObject = object.deepCopy();
    }

    @AfterAll
    public static void cleanup()
    {
        ContentBuilderLib.destroy();
    }

    public class Layer1
    {

        @JsonMapping(keys = JSON_PROP_A, type = "int")
        public int propA;

        public String id;

        @JsonMapping(keys = JSON_ID, type = "string")
        public void setID(String id)
        {
            this.id = id;
        }
    }

    public class Layer2 extends Layer1
    {

        @JsonMapping(keys = JSON_PROP_B, type = "int")
        public int propB;

        public float propD;

        @JsonMapping(keys = JSON_PROP_D, type = "float")
        public void setPropD(float p)
        {
            this.propD = p;
        }
    }

    public class Layer3 extends Layer2
    {

        @JsonMapping(keys = JSON_PROP_C, type = "int[]")
        public int[] propC;
    }

    public class Layer2A extends Layer1
    {

        @JsonMapping(keys = JSON_PROP_E, type = "String")
        public String propE;
    }
}
