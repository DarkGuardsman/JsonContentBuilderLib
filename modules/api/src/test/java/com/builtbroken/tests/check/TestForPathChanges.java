package com.builtbroken.tests.check;

import com.builtbroken.builder.data.IJsonGeneratedObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This test exists to reminder me if I refactor without updating things
 * Created by Dark(DarkGuardsman, Robert) on 2019-05-14.
 */
public class TestForPathChanges
{
    @Test
    public void testServicePath()
    {
        Assertions.assertEquals("com.builtbroken.builder.data.IJsonGeneratedObject", IJsonGeneratedObject.class.toString().replace("interface", "").trim());
    }
}
