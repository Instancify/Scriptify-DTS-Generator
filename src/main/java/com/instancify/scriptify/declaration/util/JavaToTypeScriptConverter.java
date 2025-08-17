package com.instancify.scriptify.declaration.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Java types to TypeScript types converter.
 */
public class JavaToTypeScriptConverter {

    private static final Map<Class<?>, String> TYPE_MAPPING = new HashMap<>();

    static {
        // Primitives and it's wrappers
        TYPE_MAPPING.put(boolean.class, "boolean");
        TYPE_MAPPING.put(Boolean.class, "boolean");

        TYPE_MAPPING.put(byte.class, "number");
        TYPE_MAPPING.put(Byte.class, "number");
        TYPE_MAPPING.put(short.class, "number");
        TYPE_MAPPING.put(Short.class, "number");
        TYPE_MAPPING.put(int.class, "number");
        TYPE_MAPPING.put(Integer.class, "number");
        TYPE_MAPPING.put(long.class, "number");
        TYPE_MAPPING.put(Long.class, "number");
        TYPE_MAPPING.put(float.class, "number");
        TYPE_MAPPING.put(Float.class, "number");
        TYPE_MAPPING.put(double.class, "number");
        TYPE_MAPPING.put(Double.class, "number");

        TYPE_MAPPING.put(char.class, "string");
        TYPE_MAPPING.put(Character.class, "string");
        TYPE_MAPPING.put(String.class, "string");

        TYPE_MAPPING.put(BigInteger.class, "number");
        TYPE_MAPPING.put(BigDecimal.class, "number");

        // Date and time
        TYPE_MAPPING.put(Date.class, "Date");
        TYPE_MAPPING.put(LocalDate.class, "Date");
        TYPE_MAPPING.put(LocalDateTime.class, "Date");
        TYPE_MAPPING.put(LocalTime.class, "Date");

        // Object and void
        TYPE_MAPPING.put(Object.class, "any");
        TYPE_MAPPING.put(void.class, "void");
        TYPE_MAPPING.put(Void.class, "void");
    }

    /**
     * Converts a Java class to a TypeScript type.
     *
     * @param type Java class for conversion.
     * @return TypeScript type as a string.
     */
    public static String convert(Class<?> type) {
        if (type == null) {
            return "any";
        }
        return convertType(type);
    }

    /**
     * Converts Java Type (including generic) to TypeScript type.
     *
     * @param type Java Type for conversion
     * @return TypeScript type as a string
     */
    public static String convert(Type type) {
        if (type == null) {
            return "any";
        }

        if (type instanceof Class<?>) {
            return convertType((Class<?>) type);
        } else if (type instanceof ParameterizedType parameterizedType) {
            return convertParameterizedType(parameterizedType);
        } else {
            return "any";
        }
    }

    private static String convertType(Class<?> type) {
        if (TYPE_MAPPING.containsKey(type)) {
            return TYPE_MAPPING.get(type);
        }

        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            return convertType(componentType) + "[]";
        }

        if (isCollectionType(type)) {
            return "any[]";
        }

        if (isMapType(type)) {
            return "{ [key: string]: any }";
        }

        if (type.isEnum()) {
            return "string";
        }

        return type.getName();
    }

    private static String convertParameterizedType(ParameterizedType paramType) {
        Class<?> rawType = (Class<?>) paramType.getRawType();
        Type[] typeArgs = paramType.getActualTypeArguments();

        if (isCollectionType(rawType)) {
            if (typeArgs.length > 0) {
                String elementType = convert(typeArgs[0]);
                return elementType + "[]";
            }
            return "any[]";
        }

        if (isMapType(rawType)) {
            String keyType = "string";
            String valueType = "any";

            if (typeArgs.length >= 1) {
                Type keyTypeArg = typeArgs[0];
                if (keyTypeArg instanceof Class<?> keyClass) {
                    if (isStringType(keyClass)) {
                        keyType = "string";
                    } else if (isNumberType(keyClass)) {
                        keyType = "number";
                    } else {
                        keyType = "string"; // fallback
                    }
                }
            }

            if (typeArgs.length >= 2) {
                valueType = convert(typeArgs[1]);
            }

            return "{ [key: " + keyType + "]: " + valueType + " }";
        }

        if (rawType.equals(Optional.class) && typeArgs.length > 0) {
            return convert(typeArgs[0]) + " | null";
        }

        if (typeArgs.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(rawType.getSimpleName()).append("<");
            for (int i = 0; i < typeArgs.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(convert(typeArgs[i]));
            }
            sb.append(">");
            return sb.toString();
        }

        return rawType.getSimpleName();
    }

    private static boolean isCollectionType(Class<?> type) {
        return Collection.class.isAssignableFrom(type) ||
                List.class.isAssignableFrom(type) ||
                Set.class.isAssignableFrom(type) ||
                Queue.class.isAssignableFrom(type) ||
                Deque.class.isAssignableFrom(type);
    }

    private static boolean isMapType(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    private static boolean isStringType(Class<?> type) {
        return type.equals(String.class) ||
                type.equals(char.class) ||
                type.equals(Character.class);
    }

    private static boolean isNumberType(Class<?> type) {
        return type.equals(int.class) || type.equals(Integer.class) ||
                type.equals(long.class) || type.equals(Long.class) ||
                type.equals(short.class) || type.equals(Short.class) ||
                type.equals(byte.class) || type.equals(Byte.class) ||
                type.equals(float.class) || type.equals(Float.class) ||
                type.equals(double.class) || type.equals(Double.class) ||
                type.equals(BigInteger.class) || type.equals(BigDecimal.class);
    }
}
