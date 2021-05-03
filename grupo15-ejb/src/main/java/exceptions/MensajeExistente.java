package exceptions;

public class MensajeExistente extends Exception {

	private static final long serialVersionUID = 1L;

	public MensajeExistente(String string) {
		super(string);
	}
}
