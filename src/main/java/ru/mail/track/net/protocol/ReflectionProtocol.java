package ru.mail.track.net.protocol;

import ru.mail.track.message.messagetypes.Message;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliakseisemchankau on 1.12.15.
 */
public class ReflectionProtocol implements Protocol {
    @Override
    public Message decode(byte[] bytes) {
        String code = new String(bytes);
        String[] parts = code.split("-");
        String clazzName = parts[0];

        Class clazz = null;

        System.out.println(System.getProperty("user.dir"));

        try {
            clazz = Class.forName(clazzName);
        } catch (ClassNotFoundException cnfExc) {
            System.err.println("can't load class with name=" + clazzName);
            cnfExc.printStackTrace();
        }

        Object object = null;
        try {
            Constructor<?> ctor = clazz.getConstructor();
            object = ctor.newInstance(new Object[] {  });
        } catch (NoSuchMethodException nsmExc) {
            System.err.println("no empty constructor for " + clazzName);
        } catch (Exception e) {
            System.err.println("some fun with creating an instance(");
            e.printStackTrace();
        }

        String inner = (parts[1].split("}"))[0];

        String[] keyVals = inner.split(",");

        for(String keyVal : keyVals) {
            String fieldName = keyVal.split(":")[0];
            String fieldValue = keyVal.split(":")[1];
            Field field = null;
            try {
                field = clazz.getField(fieldName);
            } catch (NoSuchFieldException nsfExc) {
                System.err.println("no such field:" + fieldName);
                nsfExc.printStackTrace();
            }
            field.setAccessible(true);
            Object value = null;
            if ("null".equals(fieldValue)) {
                value = null;
            } else if (fieldValue.charAt(0) == '\'') {
                String fieldAsString = fieldValue.toString();
                value = new String(fieldAsString.substring(1,fieldAsString.length() - 1));
                try {
                    field.set(object, value);
                } catch (IllegalAccessException iaExc) {
                    System.err.println("can't access for field=" + fieldName);
                }
            } else {
                value = Long.valueOf(fieldValue);
                try {
                    field.set(object, value);
                } catch (IllegalAccessException iaExc) {
                    System.err.println("can't access for field=" + fieldName + " with value=" + value);
                    iaExc.printStackTrace();
            }
            }
        }
        System.out.println(object.toString());
        return (Message) object;
    }

    @Override
    public byte[] encode(Message msg) {
        StringBuilder sb = new StringBuilder();
        Class clazz = msg.getClass();
        sb.append(clazz.getName());

        sb.append(" -");

        List<Field> fields = getAllDeclaredFields(msg.getClass());

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(msg);
            } catch (IllegalAccessException iaExc) {
                System.err.println("illegal access to field" + fieldName + " in " + msg.toString());
                iaExc.printStackTrace();
            }
            try {
                //sb.append('\'');
                sb.append(fieldName);
                //sb.append('\'');
                sb.append(":");
                if (value == null) {
                    sb.append("null");
                } else if (value instanceof String) {
                    sb.append('\'');
                    sb.append(value.toString());
                    sb.append('\'');
                } else {
                    sb.append(value.toString());
                }
                sb.append(", ");
            } catch (NullPointerException npExc) {
                System.err.println("value is null:(");
                npExc.printStackTrace();
            }

        }

        sb.append('+');
        System.out.println("result is=" + sb.toString());
        return sb.toString().getBytes();
    }

    public List<Field> getAllDeclaredFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllDeclaredFields(clazz.getSuperclass()));
        }
        for (Field field : clazz.getDeclaredFields()) {
            fields.add(field);
        }
        return fields;
    }

}
