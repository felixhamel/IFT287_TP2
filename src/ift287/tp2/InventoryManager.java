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

import com.google.common.base.Strings;

public class InventoryManager 
{
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
	public InventoryManager(String storageFileNameWithoutExtention) throws InvalidStorageFileNameException, FailedToCreateStorageFileException, FailedToReadStorageException 
	{
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
		showMenu();
	}
	
	/**
	 * Loads all the players and cards from storage.
	 * @throws FailedToReadStorageException 
	 */
	private void loadPlayersFromStorage() throws FailedToReadStorageException 
	{
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(storageFile));
			String line;
			while((line = reader.readLine()) != null) {
				// Decode line
				if(line.length() > 0) {
					createPlayerFromLineInStorage(line);
				}
			}
		} catch(IOException | InvalidParameterException e) {
			throw new FailedToReadStorageException(storageFile.getName(), e);			
		} finally {
			try {
			reader.close();
			} catch(IOException e) {
				// We don't care !
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
		
		// Create player
		String cle = splitedInformations[0];
		String nomJoueur = splitedInformations[1];
		int cardNumber = Integer.parseInt(splitedInformations[2]);
		
		Joueur player = new Joueur(cle, nomJoueur);
		
		// Create cards
		for(int i = 0; i < cardNumber; ++i) {
			String cardTitle = splitedInformations[3+(3*i)];
			String teamName = splitedInformations[4+(3*i)];
			int cardYear = Integer.parseInt(splitedInformations[5+(3*i)]);
			Carte card = new Carte(cardTitle, teamName, cardYear);
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
			throw new FailedToSaveInventoryException(storageFile.getName(), e);
		} finally {
			try {
			writer.close();
			} catch(IOException e) {
				// Do nothing, we don't care.
			}
		}
	}
	
	/**
	 * Show menu and ask user what to do next.
	 */
	public void showMenu() 
	{
		
	}
}
