package exceptions;

public class ConstanciaRepetida extends Exception{
	private static final long serialVersionUID = 1L;
	
	public ConstanciaRepetida(String string) {
		super(string);
	}
}