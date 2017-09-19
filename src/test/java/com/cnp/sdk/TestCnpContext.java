package com.cnp.sdk;

import static org.junit.Assert.assertSame;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

import com.cnp.sdk.CnpContext;
import com.cnp.sdk.generate.ObjectFactory;

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
