package exceptions;

public class ChatExistente extends Exception {

	private static final long serialVersionUID = 1L;

	public ChatExistente(String string) {
		super(string);
	}
}
