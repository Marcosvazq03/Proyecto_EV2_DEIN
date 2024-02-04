package utils;


/**
 * Clase de utilidad para operaciones de cadenas de caracteres.
 */
public class StringUtils {
	
    /**
     * Constructor privado para prevenir la instanciación de la clase.
     *
     * @throws IllegalAccessException Si se intenta instanciar.
     */
    private StringUtils() throws IllegalAccessException {
        throw new IllegalAccessException("Clase de utilidad");
    }
	
    /**
     * Devuelve una cadena sin espacios al principio y al final, o una cadena vacía si la entrada es nula.
     *
     * @param str La cadena a recortar.
     * @return La cadena recortada, o una cadena vacía si la entrada es nula.
     */
    public static String trimToEmpty(final String str) {
        return str == null ? "" : str.trim();
    }
    
    /**
     * Devuelve una cadena sin espacios al principio y al final, o nulo si la entrada es nula.
     *
     * @param str La cadena a recortar.
     * @return La cadena recortada, o nulo si la entrada es nula.
     */
    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }
    
    /**
     * Comprueba si una secuencia de caracteres está vacía ("") o es nula.
     *
     * @param cs La secuencia de caracteres a comprobar.
     * @return Verdadero si la secuencia de caracteres está vacía o es nula.
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
    
    /**
     * Comprueba si una secuencia de caracteres está en blanco, vacía ("") o es nula.
     *
     * @param cs La secuencia de caracteres a comprobar.
     * @return Verdadero si la secuencia de caracteres está en blanco, vacía o es nula.
     */
    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Devuelve la longitud de una secuencia de caracteres, o 0 si la secuencia es nula.
     *
     * @param cs La secuencia de caracteres.
     * @return La longitud de la secuencia de caracteres, o 0 si la secuencia es nula.
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
    
    /**
     * Recorta una cadena y devuelve nulo si la cadena recortada está vacía.
     *
     * @param str La cadena a recortar.
     * @return La cadena recortada, o nulo si la cadena recortada está vacía.
     */
    public static String trimToNull(final String str) {
        final String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }
    
    /**
     * Compara dos secuencias de caracteres para determinar si son iguales.
     *
     * @param cs1 La primera secuencia de caracteres.
     * @param cs2 La segunda secuencia de caracteres.
     * @return Verdadero si las dos secuencias de caracteres son iguales.
     */
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        // Comparación paso a paso
        final int length = cs1.length();
        for (int i = 0; i < length; i++) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Compara dos regiones de dos secuencias de caracteres para determinar si son iguales.
     *
     * @param cs La secuencia de caracteres.
     * @param ignoreCase Si se debe ignorar el caso durante la comparación.
     * @param thisStart El inicio de la región en la secuencia de caracteres.
     * @param substring La secuencia de caracteres a comparar.
     * @param start El inicio de la región en la secuencia de caracteres a comparar.
     * @param length La longitud de las regiones a comparar.
     * @return Verdadero si las dos regiones son iguales.
     */
    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
            final CharSequence substring, final int start, final int length)    {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extraer estos valores primero para detectar NullPointerExceptions de la misma manera que la versión de java.lang.String
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Comprobar parámetros inválidos
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }

        // Comprobar que las regiones tienen suficiente longitud
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // La misma comprobación real que en String.regionMatches():
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * Compara dos secuencias de caracteres para determinar si son iguales, ignorando el caso.
     *
     * @param cs1 La primera secuencia de caracteres.
     * @param cs2 La segunda secuencia de caracteres.
     * @return Verdadero si las dos secuencias de caracteres son iguales, ignorando el caso.
     */
    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        return regionMatches(cs1, true, 0, cs2, 0, cs1.length());
    }
    
}
