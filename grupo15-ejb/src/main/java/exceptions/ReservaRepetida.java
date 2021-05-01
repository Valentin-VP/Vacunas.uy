package exceptions;

public class ReservaRepetida extends Exception{
	private static final long serialVersionUID = 1L;
	
	public ReservaRepetida(String string) {
		super(string);
	}
}