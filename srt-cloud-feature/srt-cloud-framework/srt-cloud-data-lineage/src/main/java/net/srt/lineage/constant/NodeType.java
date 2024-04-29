package net.srt.lineage.constant;

/**
 *
 **/
public enum NodeType {

	/**
	 * DATABASE
	 */
	DATABASE(1, "DATABASE"),

	/**
	 * TABLE
	 */
	TABLE(2, "TABLE"),

	/**
	 * COLUMN
	 */
	COLUMN(3, "COLUMN");


	private final Integer code;
	private final String value;

	NodeType(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public Integer getCode() {
		return this.code;
	}
}
