package fractals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public interface Parameter {

    public static class Value {
        private String id;
        private String name;
        private Object value;

        public Value(String id, String name, Object value) {
            this.id = id;
            this.name = name;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public static @interface Marker {
        String order() default "";

        String description() default "";

        boolean modifiable() default false;
    }

    String getId();

    String getName();

    Class<?> getType();

    Object getValue();

    List<Value> getPredefinedValues();

    void setValue(Object value);

    boolean isModifiable();

}
