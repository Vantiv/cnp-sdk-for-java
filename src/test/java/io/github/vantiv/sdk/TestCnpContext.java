package io.github.vantiv.sdk;

import static org.junit.Assert.assertSame;

import javax.xml.bind.JAXBContext;

import io.github.vantiv.sdk.generate.ObjectFactory;
import org.junit.Test;

public class TestCnpContext {

    @Test
    public void testGetJAXBContextReturnsSameObject() {
        JAXBContext context1 = CnpContext.getJAXBContext();
        JAXBContext context2 = CnpContext.getJAXBContext();

        assertSame(context1, context2);
    }

    @Test
    public void testGetObjectFactoryReturnsSameObject() {
        ObjectFactory factory1 = CnpContext.getObjectFactory();
        ObjectFactory factory2 = CnpContext.getObjectFactory();

        assertSame(factory1, factory2);
    }

}
