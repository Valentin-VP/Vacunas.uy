package exceptions;

public class ConstanciaInexistente extends Exception{
	private static final long serialVersionUID = 1L;
	
	public ConstanciaInexistente(String string) {
		super(string);
	}
}