package exceptions;

public class CertificadoRepetido extends Exception{
	private static final long serialVersionUID = 1L;
	
	public CertificadoRepetido(String string) {
		super(string);
	}
}