package exceptions;

public class AccionInvalida extends Exception{
	private static final long serialVersionUID = 1L;
	
	public AccionInvalida(String string) {
		super(string);
	}
}