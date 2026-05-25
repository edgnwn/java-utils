package com.edgnwn.random;

import java.security.SecureRandom;

/**
 * A utility class for generating secure, random strings based on different character pools and modes.
 * This class uses {@link SecureRandom} to ensure cryptographically strong random outputs.
 *
 * @author Edi Gunawan
 * @version 1.0
 */
public class RandomStringGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Defines the character sets allowed for string generation.
     * Each mode maps to a specific, pre-defined pool of characters.
     */
    public enum Mode {
        /**
         * Only numbers (0-9).
         */
        NUMERIC(Pool.NUMBERS),
        /**
         * Lowercase alphabetic characters (a-z).
         */
        LOWERCASE(Pool.LOWERCASE),
        /**
         * Uppercase alphabetic characters (A-Z).
         */
        UPPERCASE(Pool.UPPERCASE),
        /**
         * Uppercase and lowercase alphabetic characters (a-z, A-Z).
         */
        ALPHABET(Pool.LOWERCASE + Pool.UPPERCASE),
        /**
         * Alphanumeric characters (a-z, A-Z, 0-9).
         */
        ALPHANUMERIC(Pool.LOWERCASE + Pool.UPPERCASE + Pool.NUMBERS),
        /**
         * All characters including lowercase, uppercase, numbers, and symbols.
         */
        ALL(Pool.LOWERCASE + Pool.UPPERCASE + Pool.NUMBERS + Pool.SYMBOLS);

        private final String characterPool;

        /**
         * Constructor to associate each mode with its respective character pool.
         *
         * @param characterPool The string containing allowed characters for the mode.
         */
        Mode(String characterPool) {
            this.characterPool = characterPool;
        }

        /**
         * Gets the character pool associated with this mode.
         *
         * @return A string of allowed characters.
         */
        public String getCharacterPool() {
            return this.characterPool;
        }

        /**
         * Internal holder class to safely manage character constants.
         * This prevents illegal forward references during enum initialization.
         */
        private static class Pool {
            private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
            private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            private static final String NUMBERS = "0123456789";
            private static final String SYMBOLS = "!@#$%^&*()-_=+<>?";
        }
    }

    /**
     * Generates a random string based on the specified length and character mode.
     *
     * @param length The length of the generated string. Must be greater than 0.
     * @param mode   The {@link Mode} determining which character pool to use.
     * @return A randomly generated string.
     * @throws IllegalArgumentException If the provided length is less than or equal to 0.
     */
    public static String generate(int length, Mode mode) {
        validateLength(length);

        String characterPool = mode.getCharacterPool();
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(characterPool.length());
            result.append(characterPool.charAt(index));
        }

        return result.toString();
    }

    // =========================================================================
    // Convenience Method : Numbers Generation
    // =========================================================================

    /**
     * Generates a random numeric string with a default length of 6 digits.
     *
     * @return A random 6-digit numeric string.
     */
    public static String generateNumericString() {
        return generateNumericString(6);
    }

    /**
     * Generates a random numeric string with the specified length.
     *
     * @param length The length of the numeric string to be generated. Must be greater than 0.
     * @return A randomly generated numeric string.
     * @throws IllegalArgumentException If the provided length is less than or equal to 0.
     */
    public static String generateNumericString(int length) {
        return generate(length, Mode.NUMERIC);
    }

    // =========================================================================
    // Convenience Method : Lowercase Generation
    // =========================================================================

    /**
     * Generates a random lowercase alphabetic string with a default length of 6 characters.
     *
     * @return A random 6-character lowercase string.
     */
    public static String generateLowercaseString() {
        return generateLowercaseString(6);
    }

    /**
     * Generates a random lowercase alphabetic string with the specified length.
     *
     * @param length The length of the lowercase string to be generated. Must be greater than 0.
     * @return A randomly generated lowercase string.
     * @throws IllegalArgumentException If the provided length is less than or equal to 0.
     */
    public static String generateLowercaseString(int length) {
        return generate(length, Mode.LOWERCASE);
    }

    // =========================================================================
    // Convenience Method : Uppercase Generation
    // =========================================================================

    /**
     * Generates a random uppercase alphabetic string with a default length of 6 characters.
     *
     * @return A random 6-character uppercase string.
     */
    public static String generateUppercaseString() {
        return generateUppercaseString(6);
    }

    /**
     * Generates a random uppercase alphabetic string with the specified length.
     *
     * @param length The length of the uppercase string to be generated. Must be greater than 0.
     * @return A randomly generated uppercase string.
     * @throws IllegalArgumentException If the provided length is less than or equal to 0.
     */
    public static String generateUppercaseString(int length) {
        return generate(length, Mode.UPPERCASE);
    }

    // =========================================================================
    // Convenience Method : Alphabet (Lowercase and Uppercase) Generation
    // =========================================================================

    /**
     * Generates a random mixed-case alphabetic string (a-z, A-Z) with a default length of 6 characters.
     *
     * @return A random 6-character mixed-case alphabetic string.
     */
    public static String generateAlphabetString() {
        return generateAlphabetString(6);
    }

    /**
     * Generates a random mixed-case alphabetic string (a-z, A-Z) with the specified length.
     *
     * @param length The length of the alphabetic string to be generated. Must be greater than 0.
     * @return A randomly generated mixed-case alphabetic string.
     * @throws IllegalArgumentException If the provided length is less than or equal to 0.
     */
    public static String generateAlphabetString(int length) {
        return generate(length, Mode.ALPHABET);
    }

    // =========================================================================
    // Pattern-Based Generation (Masking)
    // =========================================================================

    /**
     * Generates a random string based on a custom pattern mask with full symbolic tokens.
     * Alphanumeric characters are kept as literal text, while placeholders are randomized.
     * <p>
     * <b>Pattern Rules:</b>
     * <ul>
     * <li>{@code '#'} - Replaced with a random numeric digit (0-9).</li>
     * <li>{@code '?'} - Replaced with a random mixed-case alphabetic letter (a-z, A-Z).</li>
     * <li>{@code '^'} - Replaced with a random UPPERCASE alphabetic letter (A-Z).</li>
     * <li>{@code '~'} - Replaced with a random lowercase alphabetic letter (a-z).</li>
     * <li>{@code '%'} - Replaced with a random alphanumeric character (0-9, a-z, A-Z).</li>
     * <li>{@code '\'} - Escapes any token to treat it as a literal. To get a literal backslash at the end, use {@code '\\'}.</li>
     * </ul>
     * </p>
     *
     * @param pattern The custom pattern mask defining the string structure. Cannot be null or empty.
     * @return A randomly generated string matching the specified pattern.
     * @throws IllegalArgumentException If the provided pattern is null, empty, or contains a dangling escape character {@code '\'} at the very end.
     */
    public static String generateByPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }

        StringBuilder result = new StringBuilder();
        String numbers = Mode.NUMERIC.getCharacterPool();
        String lowercase = Mode.LOWERCASE.getCharacterPool();
        String uppercase = Mode.UPPERCASE.getCharacterPool();
        String alphabet = Mode.ALPHABET.getCharacterPool();
        String alphanumeric = Mode.ALPHANUMERIC.getCharacterPool();

        for (int i = 0; i < pattern.length(); i++) {
            char currentChar = pattern.charAt(i);

            if (currentChar == '\\') {
                if (i + 1 < pattern.length()) {
                    result.append(pattern.charAt(i + 1));
                    i++;
                } else {
                    throw new IllegalArgumentException("Dangling escape character '\\' at the end of pattern");
                }
            } else {
                switch (currentChar) {
                    case '#' -> result.append(numbers.charAt(RANDOM.nextInt(numbers.length())));
                    case '?' -> result.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
                    case '^' -> result.append(uppercase.charAt(RANDOM.nextInt(uppercase.length())));
                    case '~' -> result.append(lowercase.charAt(RANDOM.nextInt(lowercase.length())));
                    case '%' -> result.append(alphanumeric.charAt(RANDOM.nextInt(alphanumeric.length())));
                    default  -> result.append(currentChar);
                }
            }
        }

        return result.toString();
    }

    // =========================================================================
    // Private Helper Method(s)
    // =========================================================================

    /**
     * Validates that the requested string length is positive.
     *
     * @param length The length to validate.
     * @throws IllegalArgumentException If the length is less than or equal to 0.
     */
    private static void validateLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }
    }
}