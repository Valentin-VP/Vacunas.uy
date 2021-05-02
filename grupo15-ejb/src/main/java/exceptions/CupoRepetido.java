package exceptions;

public class CupoRepetido extends Exception{
	private static final long serialVersionUID = 1L;
	
	public CupoRepetido(String string) {
		super(string);
	}
}