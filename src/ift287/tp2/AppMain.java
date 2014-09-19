package ift287.tp2;

import ift287.tp2.exceptions.FailedToCreateStorageFileException;
import ift287.tp2.exceptions.FailedToReadStorageException;
import ift287.tp2.exceptions.InvalidStorageFileNameException;
import ift287.tp2.exceptions.MissingParameterException;

public class AppMain 
{
	public static void main(String[] args) throws MissingParameterException, InvalidStorageFileNameException, FailedToCreateStorageFileException, FailedToReadStorageException 
	{
		// First parameter received is the name of the file we will use on the hard drive where this application is located
		if(args.length == 0) {
			throw new MissingParameterException("file", "you must provide a file where to store informations about players and cards.");
		} else {
			new InventoryManager(args[0]);
		}
	}
}
