package drrename.strategy;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Removes consecutive duplicate special characters (whitespace, underscore, hyphen, dot)
 * from a file name, collapsing each run into a single occurrence.
 * <p>
 * Examples:
 * <ul>
 *   <li>{@code "hello__world"} → {@code "hello_world"}</li>
 *   <li>{@code "hello  world"} → {@code "hello world"}</li>
 *   <li>{@code "file--name"} → {@code "file-name"}</li>
 *   <li>{@code "file..name"} → {@code "file.name"}</li>
 * </ul>
 */
public class RemoveDuplicateSpecialCharsStrategy extends RenamingStrategyProto {

    private static final String NAME_IDENTIFIER = "strategy.remove-duplicate-special-chars.name";
    private static final String HELP_IDENTIFIER = "strategy.remove-duplicate-special-chars.help";

    /** Matches two or more consecutive identical special characters from the target set. */
    private static final Pattern DUPLICATE_SPECIAL_CHARS = Pattern.compile("([\\s_\\-.])\\1+");

    public RemoveDuplicateSpecialCharsStrategy(ResourceBundle resourceBundle, RenamingConfig renamingConfig) {
        super(resourceBundle, renamingConfig);
    }

    @Override
    protected String getNameId() {
        return NAME_IDENTIFIER;
    }

    @Override
    protected String getHelpTextId() {
        return HELP_IDENTIFIER;
    }

    @Override
    public String applyStrategyOnString(String fileNameString) {
        return DUPLICATE_SPECIAL_CHARS.matcher(fileNameString).replaceAll("$1");
    }

    @Override
    public boolean isReplacing() {
        return false;
    }
}
