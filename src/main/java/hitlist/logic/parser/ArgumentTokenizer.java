package hitlist.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tokenizes arguments string of the form: {@code preamble </prefix>value </prefix>value ...}<br>
 *     e.g. {@code some preamble text /t 11.00 /t12.00 /k /m July}  where prefixes are {@code /t /k /m}.<br>
 * 1. An argument's value can be an empty string e.g. the value of {@code /k} in the above example.<br>
 * 2. Leading and trailing whitespaces of an argument value will be discarded.<br>
 * 3. An argument may be repeated and all its values will be accumulated e.g. the value of {@code /t}
 *    in the above example.<br>
 * 4. Prefixes can be written with or without a space after them (e.g., "/nJohn" or "/n John").
 */
public class ArgumentTokenizer {

    /**
     * Tokenizes an arguments string and returns an {@code ArgumentMultimap} object that maps prefixes to their
     * respective argument values. Only the given prefixes will be recognized in the arguments string.
     *
     * @param argsString Arguments string of the form: {@code preamble </prefix>value </prefix>value ...}
     * @param prefixes   Prefixes to tokenize the arguments string with
     * @return           ArgumentMultimap object that maps prefixes to their arguments
     */
    public static ArgumentMultimap tokenize(String argsString, Prefix... prefixes) {
        List<PrefixPosition> positions = findAllPrefixPositions(argsString, prefixes);
        return extractArguments(argsString, positions);
    }

    /**
     * Finds all zero-based prefix positions in the given arguments string.
     *
     * @param argsString Arguments string of the form: {@code preamble </prefix>value </prefix>value ...}
     * @param prefixes   Prefixes to find in the arguments string
     * @return           List of zero-based prefix positions in the given arguments string
     */
    private static List<PrefixPosition> findAllPrefixPositions(String argsString, Prefix... prefixes) {
        return Arrays.stream(prefixes)
                .flatMap(prefix -> findPrefixPositions(argsString, prefix).stream())
                .collect(Collectors.toList());
    }

    /**
     * {@see findAllPrefixPositions}
     */
    private static List<PrefixPosition> findPrefixPositions(String argsString, Prefix prefix) {
        List<PrefixPosition> positions = new ArrayList<>();

        int currentIndex = 0;
        while (currentIndex < argsString.length()) {
            int prefixPosition = findPrefixPosition(argsString, prefix.getPrefix(), currentIndex);
            if (prefixPosition == -1) {
                break;
            }
            positions.add(new PrefixPosition(prefix, prefixPosition));
            // Move past this prefix to find the next one
            currentIndex = prefixPosition + 1;
        }

        return positions;
    }

    /**
     * Returns the index of the first occurrence of {@code prefix} in
     * {@code argsString} starting from index {@code fromIndex}.
     * The prefix can be found with or without a space before it.
     * Returns -1 if no such occurrence can be found.
     */
    private static int findPrefixPosition(String argsString, String prefix, int fromIndex) {
        if (fromIndex < 0 || fromIndex >= argsString.length()) {
            return -1;
        }

        // Look for the prefix starting from fromIndex
        int prefixIndex = argsString.indexOf(prefix, fromIndex);

        while (prefixIndex != -1) {
            // Check if this is a valid prefix occurrence:
            // Either at the start of string, or preceded by whitespace
            if (prefixIndex == 0 || Character.isWhitespace(argsString.charAt(prefixIndex - 1))) {
                return prefixIndex;
            }
            // Look for next occurrence after this one
            prefixIndex = argsString.indexOf(prefix, prefixIndex + 1);
        }

        return -1;
    }

    /**
     * Extracts prefixes and their argument values, and returns an {@code ArgumentMultimap} object that maps the
     * extracted prefixes to their respective arguments. Prefixes are extracted based on their zero-based positions in
     * {@code argsString}.
     *
     * @param argsString      Arguments string of the form: {@code preamble </prefix>value </prefix>value ...}
     * @param prefixPositions Zero-based positions of all prefixes in {@code argsString}
     * @return                ArgumentMultimap object that maps prefixes to their arguments
     */
    private static ArgumentMultimap extractArguments(String argsString, List<PrefixPosition> prefixPositions) {

        // Sort by start position
        prefixPositions.sort((prefix1, prefix2) -> prefix1.getStartPosition() - prefix2.getStartPosition());

        // Insert a PrefixPosition to represent the preamble
        PrefixPosition preambleMarker = new PrefixPosition(new Prefix(""), 0);
        prefixPositions.add(0, preambleMarker);

        // Add a dummy PrefixPosition to represent the end of the string
        PrefixPosition endPositionMarker = new PrefixPosition(new Prefix(""), argsString.length());
        prefixPositions.add(endPositionMarker);

        // Map prefixes to their argument values (if any)
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        for (int i = 0; i < prefixPositions.size() - 1; i++) {
            // Extract and store prefixes and their arguments
            Prefix argPrefix = prefixPositions.get(i).getPrefix();
            String argValue = extractArgumentValue(argsString, prefixPositions.get(i), prefixPositions.get(i + 1));
            argMultimap.put(argPrefix, argValue);
        }

        return argMultimap;
    }

    /**
     * Returns the trimmed value of the argument in the arguments string specified by {@code currentPrefixPosition}.
     * The end position of the value is determined by {@code nextPrefixPosition}.
     */
    private static String extractArgumentValue(String argsString,
                                               PrefixPosition currentPrefixPosition,
                                               PrefixPosition nextPrefixPosition) {
        Prefix prefix = currentPrefixPosition.getPrefix();

        // If this is the preamble marker, handle specially
        if (prefix.getPrefix().isEmpty()) {
            String value = argsString.substring(0, nextPrefixPosition.getStartPosition());
            return value.trim();
        }

        int valueStartPos = currentPrefixPosition.getStartPosition() + prefix.getPrefix().length();

        // Skip any whitespace after the prefix
        while (valueStartPos < argsString.length() && Character.isWhitespace(argsString.charAt(valueStartPos))) {
            valueStartPos++;
        }

        String value = argsString.substring(valueStartPos, nextPrefixPosition.getStartPosition());
        return value.trim();
    }

    /**
     * Represents a prefix's position in an arguments string.
     */
    private static class PrefixPosition {
        private int startPosition;
        private final Prefix prefix;

        PrefixPosition(Prefix prefix, int startPosition) {
            this.prefix = prefix;
            this.startPosition = startPosition;
        }

        int getStartPosition() {
            return startPosition;
        }

        Prefix getPrefix() {
            return prefix;
        }
    }

}