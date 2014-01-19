package net.sehales.secon.addon;

public class InvalidAddonDescriptionException extends Exception {

	public enum Reason {
		INVALID_NAME, INVALID_VERSION, INVALID_CLASS_NAME
	}

	private static final long serialVersionUID = 1L;

	private Reason            reason;

	public InvalidAddonDescriptionException(Reason reason) {
		super(reason.toString());
		this.reason = reason;
	}

	public Reason getReason() {
		return reason;
	}
}
