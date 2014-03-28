/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.sport.test.rules;

import co.edu.uniandes.csw.sport.logic.dto.SportDTO;
import co.edu.uniandes.csw.sport.persistence.converter.SportConverter;
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;
import java.beans.Statement;
import java.lang.reflect.Field;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;

import org.junit.runners.model.FrameworkMethod;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author admin
 */
public class SportTestRule implements MethodRule {

    private final Object[] params;
    private final String fieldName;
    
    public SportTestRule() {
        PodamFactory factory = new PodamFactoryImpl();
        params = new Object[5];
        for (int i = 0; i < params.length; i++) {
            SportDTO entity = factory.manufacturePojo(SportDTO.class);
            entity.setId((long) 0);
            params[i] = entity;

        }
        fieldName = "dataSample";
    }

    private boolean isInContainer() {
        Exception e = new Exception();
        e.fillInStackTrace();
        return e.getStackTrace()[e.getStackTrace().length - 1].getClassName().equals("java.lang.Thread");
    }

    public org.junit.runners.model.Statement apply(final org.junit.runners.model.Statement stmnt, FrameworkMethod fm, final Object ob) {
        return new org.junit.runners.model.Statement() {
            public void evaluate() throws Throwable {
                System.out.println("rule - before " + ob.hashCode());
                if (isInContainer()) {
                    for (int i = 0; i < params.length; i++) {
                        Object param = params[i];
                        Field targetField = ob.getClass().getDeclaredField(fieldName);
                        if (!targetField.isAccessible()) {
                            targetField.setAccessible(true);
                        }
                        targetField.set(ob, param);
                        stmnt.evaluate();
                    }
                } else {
                    stmnt.evaluate();
                }
                //System.out.println("rule - after " + o.hashCode());
            }
        };
    }

}
