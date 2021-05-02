package exceptions;

public class AgendaRepetida extends Exception{
	private static final long serialVersionUID = 1L;
	
	public AgendaRepetida(String string) {
		super(string);
	}
}