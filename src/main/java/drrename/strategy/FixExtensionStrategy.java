package drrename.strategy;

import java.util.ResourceBundle;

public class FixExtensionStrategy extends RenamingStrategyProto {

	private static final String name_identifier = "strategy.fix-extension.name";

	private static final String help_identifier = "strategy.fix-extension.help";

	public FixExtensionStrategy(ResourceBundle resourceBundle, RenamingConfig renamingConfig) {
        super(resourceBundle, renamingConfig);
    }

	@Override
	protected String getNameId() {
		return name_identifier;
	}

	@Override
	protected String getHelpTextId() {
		return help_identifier;
	}

	@Override
	public String applyStrategyOnString(String fileNameString) {
		if(!getConfig().isIncludeFileExtension()) return fileNameString;
		String[] split = fileNameString.split("\\s");
		if(split[split.length-1].length() == 3){
			String reverse = new StringBuffer(fileNameString).reverse().toString();
			reverse = reverse.replaceFirst("\\s", ".");
			String result = new StringBuffer(reverse).reverse().toString();
			return result;
		}
		return fileNameString;
	}

	@Override
	public boolean isReplacing() {
		return false;
	}
}
