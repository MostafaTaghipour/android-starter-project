@file:Suppress("UNCHECKED_CAST")

package ir.rainyday.android.common.helpers

import java.lang.reflect.Method
import java.math.BigDecimal
import java.util.HashMap

/**
 * Generic object converter.
 *
 *
 * <h3>Use examples</h3>
 *
 *
 * <pre>
 * Object o1 = Boolean.TRUE;
 * Integer i = ObjectConverter.convert(o1, Integer.class);
 * System.out.println(i); // 1
 *
 * Object o2 = "false";
 * Boolean b = ObjectConverter.convert(o2, Boolean.class);
 * System.out.println(b); // false
 *
 * Object o3 = new Integer(123);
 * String s = ObjectConverter.convert(o3, String.class);
 * System.out.println(s); // 123
</pre> *
 *
 *
 * Not all possible conversions are implemented. You can extend the <tt>ObjectConverter</tt>
 * easily by just adding a new method to it, with the appropriate logic. For example:
 *
 *
 * <pre>
 * public static ToObject fromObjectToObject(FromObject fromObject) {
 * // Implement.
 * }
</pre> *
 *
 *
 * The method name doesn't matter. It's all about the parameter type and the return type.
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/08/generic-object-converter.html
 */
object ConvertorUtil {

    // Init ---------------------------------------------------------------------------------------

    private val CONVERTERS = HashMap<String, Method>()

    init {
        // Preload converters.
        val methods = ConvertorUtil::class.java.declaredMethods
        for (method in methods) {
            if (method.parameterTypes.size == 1) {
                // Converter should accept 1 argument. This skips the convert() method.
                CONVERTERS.put(method.parameterTypes[0].name + "_"
                        + method.returnType.name, method)
            }
        }
    }

    // Action -------------------------------------------------------------------------------------

    /**
     * Convert the given object value to the given class.
     *
     * @param from The object value to be converted.
     * @param to   The type class which the given object should be converted to.
     * @return The converted object value.
     * @throws NullPointerException          If 'to' is null.
     * @throws UnsupportedOperationException If no suitable converter can be found.
     * @throws RuntimeException              If conversion failed somehow. This can be caused by at least
     * an ExceptionInInitializerError, IllegalAccessException or InvocationTargetException.
     */
    fun <T> convert(from: Any?, to: Class<T>): T? {

        // Null is just null.
        if (from == null) {
            return null
        }

        // Can we cast? Then just do it.
        if (to.isAssignableFrom(from.javaClass)) {
            return to.cast(from)
        }

        // Lookup the suitable converter.
        val converterId = from.javaClass.name + "_" + to.name
        val converter = CONVERTERS[converterId] ?: //            throw new UnsupportedOperationException("Cannot convert from "
//                    + from.getClass().getName() + " to " + to.getName()
//                    + ". Requested converter does not exist.");
                return if (PrimitiveDefaults.getDefaultValue(to) == null) null else PrimitiveDefaults.getDefaultValue(to) as T?

        // Convert the value.
        try {
            return to.cast(converter.invoke(to, from))
        } catch (e: Exception) {
            //            throw new RuntimeException("Cannot convert from "
            //                    + from.getClass().getName() + " to " + to.getName()
            //                    + ". Conversion failed with " + e.getMessage(), e);

            return if (PrimitiveDefaults.getDefaultValue(to) == null) null else PrimitiveDefaults.getDefaultValue(to) as T?

        }

    }

    // Converters ---------------------------------------------------------------------------------

    /**
     * Converts Integer to Boolean. If integer value is 0, then return FALSE, else return TRUE.
     *
     * @param value The Integer to be converted.
     * @return The converted Boolean value.
     */
    fun integerToBoolean(value: Int): Boolean {
        return if (value == 0) java.lang.Boolean.FALSE else java.lang.Boolean.TRUE
    }

    /**
     * Converts Boolean to Integer. If boolean value is TRUE, then return 1, else return 0.
     *
     * @param value The Boolean to be converted.
     * @return The converted Integer value.
     */
    fun booleanToInteger(value: Boolean): Int {
        return if (value) 1 else 0
    }

    /**
     * Converts Double to BigDecimal.
     *
     * @param value The Double to be converted.
     * @return The converted BigDecimal value.
     */
    fun doubleToBigDecimal(value: Double): BigDecimal {
        return BigDecimal(value.toDouble())
    }

    /**
     * Converts BigDecimal to Double.
     *
     * @param value The BigDecimal to be converted.
     * @return The converted Double value.
     */
    fun bigDecimalToDouble(value: BigDecimal): Double {
        return value.toDouble()
    }

    /**
     * Converts Integer to String.
     *
     * @param value The Integer to be converted.
     * @return The converted String value.
     */
    fun integerToString(value: Int): String {
        return value.toString()
    }

    /**
     * Converts String to Integer.
     *
     * @param value The String to be converted.
     * @return The converted Integer value.
     */
    fun stringToInteger(value: String): Int {
        return Integer.valueOf(value)
    }

    /**
     * Converts Boolean to String.
     *
     * @param value The Boolean to be converted.
     * @return The converted String value.
     */
    fun booleanToString(value: Boolean): String {
        return value.toString()
    }

    /**
     * Converts String to Boolean.
     *
     * @param value The String to be converted.
     * @return The converted Boolean value.
     */
    fun stringToBoolean(value: String): Boolean? {
        return if (value.toLowerCase() == "true" || value === "1") true else false
    }


    fun stringToDouble(value: String): Double {
        return java.lang.Double.parseDouble(value)
    }

    fun doubleToString(value: Double): String {
        return value.toString()
    }


    fun stringToFloat(value: String): Float {
        return java.lang.Float.parseFloat(value)
    }

    fun floatToString(value: Float): String {
        return value.toString()
    }

    fun stringToLong(value: String): Long {
        return java.lang.Long.parseLong(value)
    }

    fun longToString(value: Long): String {
        return value.toString()
    }

    fun longToInteger(value: Long): Int? {
        return stringToInteger(value.toString())
    }

    fun integerToLong(value: Int): Long {
        return stringToLong(value.toString())
    }



    // You can implement more converter methods here.


    object PrimitiveDefaults {
        // These gets initialized to their default values
        private val DEFAULT_BOOLEAN: Boolean = false
        private val DEFAULT_BYTE: Byte = 0
        private val DEFAULT_SHORT: Short = 0
        private val DEFAULT_INT: Int = 0
        private val DEFAULT_LONG: Long = 0
        private val DEFAULT_FLOAT: Float = 0.toFloat()
        private val DEFAULT_DOUBLE: Double = 0.toDouble()
        private val DEFAULT_STRING: String? = null

        fun getDefaultValue(clazz: Class<*>): Any? {
            return if (clazz == Boolean::class.java) {
                DEFAULT_BOOLEAN
            } else if (clazz == Byte::class.java) {
                DEFAULT_BYTE
            } else if (clazz == Short::class.java) {
                DEFAULT_SHORT
            } else if (clazz == Int::class.java) {
                DEFAULT_INT
            } else if (clazz == Long::class.java) {
                DEFAULT_LONG
            } else if (clazz == Float::class.java) {
                DEFAULT_FLOAT
            } else if (clazz == Double::class.java) {
                DEFAULT_DOUBLE
            } else if (clazz == String::class.java) {
                DEFAULT_STRING
            } else {
                null
            }
        }
    }


}// Utility class, hide the constructor.






//********************** Extensions ******************************//

inline val Int.boolValue
    get() = ConvertorUtil.integerToBoolean(this)

inline val Boolean.intValue
    get() = ConvertorUtil.booleanToInteger(this)

inline val String.boolValue
    get() = ConvertorUtil.stringToBoolean(this)


inline val String.doubleValue
    get() = ConvertorUtil.stringToDouble(this)


inline val String.floatValue
    get() = ConvertorUtil.stringToFloat(this)

inline val String.intValue
    get() = ConvertorUtil.stringToInteger(this)

inline val Long.intValue
    get() = ConvertorUtil.longToInteger(this)

inline val Int.longValue
    get() = ConvertorUtil.integerToLong(this)
