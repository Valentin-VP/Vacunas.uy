package exceptions;

public class CupoInexistente extends Exception{
	private static final long serialVersionUID = 1L;
	
	public CupoInexistente(String string) {
		super(string);
	}
}