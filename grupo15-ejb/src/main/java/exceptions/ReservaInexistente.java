package exceptions;

public class ReservaInexistente extends Exception{
	private static final long serialVersionUID = 1L;
	
	public ReservaInexistente(String string) {
		super(string);
	}
}