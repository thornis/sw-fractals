package fractals.util;

public final class Util {

    private Util() {
    }

    public static String getDescriptionFromClassName(Class clazz) {
        String simpleName = clazz.getSimpleName();
        StringBuilder b = new StringBuilder(simpleName.length() + 1);
        for (int i = 0; i < simpleName.length(); i++) {
            if (i > 0 && Character.isUpperCase(simpleName.charAt(i))) {
                b.append(' ');
                b.append(Character.toLowerCase(simpleName.charAt(i)));
            } else {
                b.append(simpleName.charAt(i));
            }
        }
        return b.toString();
    }

}
