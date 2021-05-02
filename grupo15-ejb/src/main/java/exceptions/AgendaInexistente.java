package exceptions;

public class AgendaInexistente extends Exception{
	private static final long serialVersionUID = 1L;
	
	public AgendaInexistente(String string) {
		super(string);
	}
}