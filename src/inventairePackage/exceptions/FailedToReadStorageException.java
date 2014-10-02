package inventairePackage.exceptions;

public class FailedToReadStorageException extends Exception
{
	private static final long serialVersionUID = 1495427625896882652L;

	public FailedToReadStorageException(String filename, Throwable cause)
	{
		super("File '%s' couldn't be readed.", cause);
	}
}