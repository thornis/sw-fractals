package fractals;

import fractals.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractFractalBuilder<F extends Fractal> implements FractalBuilder<F> {
    private List<Parameter> parameters;

    private final class ParameterImpl implements Parameter {
        private Object parent;
        private Class<?> clazz;
        private Integer order;
        private String id;
        private Class<?> type;
        private String name;
        private Method getMethod;
        private List<Parameter.Value> predefinedValues;
        private boolean modifiable;

        private ParameterImpl(Object parent, Class clazz, String order, String id, String name, Class<?> type,
                              List<Value> predefinedValues, boolean modifiable) {
            this.parent = parent;
            this.clazz = clazz;
            this.order = order == null || order.isEmpty() ? 0 : Integer.valueOf(order);
            this.type = type;
            this.id = id;
            this.name = name;
            this.predefinedValues = predefinedValues;
            this.modifiable = modifiable;
            String getMethodName = "get" + Character.toUpperCase(id.charAt(0)) + id.substring(1);
            try {
                getMethod = clazz.getMethod(getMethodName);
            } catch (NoSuchMethodException e) {
                Env.instance().logException(e);
            }
        }

        public Integer getOrder() {
            return order;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<?> getType() {
            return type;
        }

        @Override
        public Object getValue() {
            if (getMethod != null) {
                try {
                    return getMethod.invoke(parent);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Env.instance().logException(e);
                }
            }
            return null;
        }

        @Override
        public List<Value> getPredefinedValues() {
            return predefinedValues;
        }

        @Override
        public void setValue(Object value) {
        }

        @Override
        public boolean isModifiable() {
            return modifiable;
        }

        @Override
        public String toString() {
            return getId() + '[' + getType().getSimpleName() + ']';
        }

    }

    public AbstractFractalBuilder() {
        parameters = inspectParameters();
    }

    private final List<Parameter> inspectParameters() {
        final List<Field> fields = new ArrayList<Field>(8);
        Class<?> currentClass = getClass();
        while (currentClass != Object.class) {
            for (final Field field : currentClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Parameter.Marker.class)) {
                    fields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        List<ParameterImpl> result = new ArrayList<>(fields.size());
        for (Field field : fields) {
            String parameterId = field.getName();
            Parameter.Marker marker = field.getAnnotation(Parameter.Marker.class);
            String description = marker.description();
            if (description.isEmpty()) {
                description = null;
            }
            Class<?> parameterType = field.getType();
            List<Parameter.Value> predefinedValues = getPredefinedValues(parameterId);
            result.add(new ParameterImpl(this, field.getDeclaringClass(), marker.order(), parameterId, description, parameterType,
                    predefinedValues, marker.modifiable()));
        }
        Collections.sort(result, (ParameterImpl p1, ParameterImpl p2) -> p1.getOrder().compareTo(p2.getOrder()));
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns all predefined values for the given parameter. If there are none,
     * returns and ampty list.
     *
     * @param parameterId parameter id
     * @return all predefined values for the given parameter
     */

    protected List<Parameter.Value> getPredefinedValues(String parameterId) {
        return Collections.emptyList();
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public List<UserTransition> getTransitions() {
        return Collections.emptyList();
    }

    /**
     * Returns the parameter with the given id.
     *
     * @param id parameter id
     * @return parameter
     */

    public Parameter getParameter(String id) {
        for (Parameter parameter : getParameters()) {
            if (id.equals(parameter.getId())) {
                return parameter;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return Util.getDescriptionFromClassName(getClass());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

}
