package hitlist.logic.parser;

/**
 * A prefix that marks the beginning of an argument in an arguments string.
 * E.g. 't/' in 'add James t/ friend'.
 */
public class Prefix {
    private final String prefix;

    /**
     * Constructs a Prefix with the given string.
     * If the prefix doesn't start with '/', it will be added automatically.
     *
     * @param prefix The prefix string, with or without the leading '/'
     */
    public Prefix(String prefix) {
        if (prefix != null && !prefix.isEmpty() && !prefix.startsWith("/")) {
            this.prefix = "/" + prefix;
        } else {
            this.prefix = prefix;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public String toString() {
        return getPrefix();
    }

    @Override
    public int hashCode() {
        return prefix == null ? 0 : prefix.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Prefix)) {
            return false;
        }

        Prefix otherPrefix = (Prefix) other;

        // Handle null cases
        if (this.prefix == null && otherPrefix.prefix == null) {
            return true;
        }
        if (this.prefix == null || otherPrefix.prefix == null) {
            return false;
        }

        return this.prefix.equals(otherPrefix.prefix);
    }
}
