package com.vmladenov.cook.core;

public final class BooleanValue {
	private Boolean value;

	public BooleanValue(Boolean value) {
		this.setValue(value);
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
}
