package ift287.tp2;

import ift287.tp2.entities.Carte;
import ift287.tp2.entities.Joueur;
import ift287.tp2.exceptions.FailedToCreateStorageFileException;
import ift287.tp2.exceptions.FailedToReadStorageException;
import ift287.tp2.exceptions.FailedToSaveInventoryException;
import ift287.tp2.exceptions.InvalidParameterException;
import ift287.tp2.exceptions.InvalidStorageFileNameException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Strings;

public class InventoryManager 
{
	private static Logger logger = Logger.getLogger("InventoryManager");
	private ArrayList<Joueur> players = new ArrayList<Joueur>();
	private File storageFile;
	
	/**
	 * Constructor.
	 * 
	 * @param storageFileNameWithoutExtention - Name of the file without extension.
	 * @throws InvalidStorageFileNameException 
	 * @throws FailedToCreateStorageFileException 
	 * @throws FailedToReadStorageException 
	 */
	public InventoryManager(String storageFileNameWithoutExtention, boolean debugLog) 
			throws InvalidStorageFileNameException, 
				   FailedToCreateStorageFileException, 
				   FailedToReadStorageException 
	{
		// Setup logger
		ConsoleHandler handler = new ConsoleHandler();
		if(debugLog) {
			handler.setLevel(Level.FINEST);
			logger.setLevel(Level.FINEST);
			logger.addHandler(handler);
		} else {
			handler.setLevel(Level.INFO);
			logger.setLevel(Level.INFO);
			logger.addHandler(handler);
		}
		
		// Make sure the name given is valid
		if(Strings.isNullOrEmpty(storageFileNameWithoutExtention)) {
			throw new InvalidStorageFileNameException(storageFileNameWithoutExtention);
		}
		
		// Create file if it didn't exists or load it content
		storageFile = new File(storageFileNameWithoutExtention + ".txt");
		if(!storageFile.exists()) {
			try {
				storageFile.createNewFile();
			} catch (IOException e) {
				throw new FailedToCreateStorageFileException(storageFileNameWithoutExtention, e);
			}
		} else {
			loadPlayersFromStorage();
		}
		
		// Show user menu and do what he ask for with the given options
		showMenu();
	}
	
	/**
	 * Loads all the players and cards from storage.
	 * @throws FailedToReadStorageException 
	 */
	private void loadPlayersFromStorage() throws FailedToReadStorageException 
	{
		logger.fine(String.format("Loading players and card from storage at '%s'.", storageFile.getName()));
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(storageFile));
			String line;
			while((line = reader.readLine()) != null) {
				if(line.length() > 0) {
					createPlayerFromLineInStorage(line);
				}
			}
		} catch(IOException | InvalidParameterException e) {
			logger.severe("Failed to read storage. " + e.getMessage());
			throw new FailedToReadStorageException(storageFile.getName(), e);			
		} finally {
			try {
			reader.close();
			} catch(IOException e) {
				logger.severe("Failed to close FileReader. " + e.getMessage());
			}
		}
	}
	
	/**
	 * Create a new player and it cards from a line in the storage.
	 * @param line - Line readed in the storage.
	 * @return Joueur - Created player
	 * @throws InvalidParameterException 
	 */
	private void createPlayerFromLineInStorage(String line) throws InvalidParameterException
	{
		String[] splitedInformations = line.split(";");
		logger.fine("Read line : " + line);
		
		// Create player
		String cle = splitedInformations[0];
		String nomJoueur = splitedInformations[1];
		int numberOfCards = Integer.parseInt(splitedInformations[2]);
		
		Joueur player = new Joueur(cle, nomJoueur);
		
		// Create cards
		for(int i = 0; i < numberOfCards; ++i) {
			String cardTitle = splitedInformations[3+(3*i)];
			String teamName = splitedInformations[4+(3*i)];
			int cardYear = Integer.parseInt(splitedInformations[5+(3*i)]);
			
			Carte card = new Carte(cardTitle, teamName, cardYear);
			player.addCarte(card);
		}
		
		players.add(player);
	}
	
	/**
	 * Save all the players and their cards to the storage file.
	 * @throws FailedToSaveInventoryException - Failed to create the storage file, failed to write to file or anything else related to IO.
	 */
	private void savePlayersToStorage() throws FailedToSaveInventoryException
	{
		FileWriter writer = null;
		try {
			writer = new FileWriter(storageFile, false);
			for(Joueur player : players) {
				writer.append(player.toString() + "\n");
			}
		} catch(IOException e) {
			logger.severe("Failed to save inventory to storage. " + e.getMessage());
			throw new FailedToSaveInventoryException(storageFile.getName(), e);
		} finally {
			try {
			writer.close();
			} catch(IOException e) {
				logger.severe("Failed to close FileWriter." + e.getMessage());
			}
		}
	}
	
	/**
	 * Show menu and ask user what to do next.
	 */
	public void showMenu() 
	{
		// TODO
	}
}
