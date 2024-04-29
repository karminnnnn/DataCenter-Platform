package net.srt.lineage.constant;

/**
 *
 **/
public enum RelationType {

	/**
	 * DATABASE_TO_DATABASE
	 */
	DATABASE_TO_DATABASE(1, "DATABASE_TO_DATABASE"),

	/**
	 * DATABASE_CONTAIN_TABLE
	 */
	DATABASE_CONTAIN_TABLE(2, "DATABASE_CONTAIN_TABLE"),

	/**
	 * TABLE_TO_TABLE
	 */
	TABLE_TO_TABLE(3, "TABLE_TO_TABLE"),

	/**
	 * TABLE_CONSTAIN_COLUMN
	 */
	TABLE_CONTAIN_COLUMN(4, "TABLE_CONSTAIN_COLUMN"),

	/**
	 * COLUMN_TO_COLUMN
	 */
	COLUMN_TO_COLUMN(5, "COLUMN_TO_COLUMN");


	private final Integer code;
	private final String value;

	RelationType(Integer code, String value) {
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
