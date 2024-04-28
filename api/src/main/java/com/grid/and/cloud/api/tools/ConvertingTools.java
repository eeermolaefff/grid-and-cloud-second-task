package com.grid.and.cloud.api.tools;

import java.math.BigInteger;

public class ConvertingTools {

    public static Boolean toBoolean(Object object) {
        if (object == null)
            return null;

        if (object.getClass().isPrimitive())
            return (boolean) object;
        if (object instanceof Boolean)
            return (boolean) object;
        if (object instanceof String)
            return Boolean.parseBoolean((String) object);
        throw new IllegalArgumentException("Unexpected data type: " + object);
    }

    public static Integer toInt(Object object) {
        if (object == null)
            return null;

        if (object.getClass().isPrimitive())
            return (int) object;
        if (object instanceof Integer)
            return (int) object;
        if (object instanceof Long)
            return ((Long) object).intValue();
        if (object instanceof BigInteger)
            return ((BigInteger) object).intValue();
        if (object instanceof String)
            return Integer.parseInt((String) object);
        throw new IllegalArgumentException("Unexpected data type: " + object);
    }

    public static Double toDouble(Object object) {
        if (object == null)
            return null;

        if (object.getClass().isPrimitive())
            return (double) object;
        if (object instanceof Double)
            return (double) object;
        if (object instanceof Integer)
            return ((Integer) object).doubleValue();
        if (object instanceof Long)
            return ((Long) object).doubleValue();
        if (object instanceof BigInteger)
            return ((BigInteger) object).doubleValue();
        if (object instanceof String)
            return Double.parseDouble((String) object);
        throw new IllegalArgumentException("Unexpected data type: " + object);
    }
}
