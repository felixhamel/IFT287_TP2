package ift287.tp2;

import ift287.tp2.exceptions.MissingParameterException;

public class AppMain
{
	public static void main(String[] args) throws Exception
	{
		boolean logDebug = false;

		// First parameter received is the name of the file we will use on the
		// hard drive where this application is located
		if (args.length == 0) {
			throw new MissingParameterException("file", "you must provide a file where to store informations about players and cards.");
		} else {
			for (String parameter : args) {
				switch (parameter) {
					case "--debug":
					case "-d":
						logDebug = true;
						break;
					case "--help":
					case "-h":
						showHelp();
						System.exit(0); // If need to show the help, the program
										// should not continue
				}
			}
			new InventoryManager(args[0], logDebug);
		}
	}

	/**
	 * Show the help menu.
	 */
	public static void showHelp()
	{
		System.out.println("Applications commands : java -jar [*.jar] {STORAGE_FILE} --debug --help");
		System.out.println("	{STORAGE_FILE} -> File without extention where the informations are. If it don't exists, it will be created.");
		System.out.println("	--debug -d------> Show debug informations.");
		System.out.println("	--help -h ------> Show this help information.");
	}
}