package exceptions;

public class CertificadoInexistente extends Exception{
	private static final long serialVersionUID = 1L;
	
	public CertificadoInexistente(String string) {
		super(string);
	}
}