package inventairePackage.exceptions;

public class FailedToCreateStorageFileException extends Exception
{
	private static final long serialVersionUID = -6729426051685656218L;

	public FailedToCreateStorageFileException(String fileLocation, Throwable exception)
	{
		super(String.format("Failed to create storage file at location '%s'.", fileLocation), exception);
	}
}
