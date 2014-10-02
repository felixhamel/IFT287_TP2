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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Strings;

public class InventoryManager 
{
	private static Logger logger = Logger.getLogger("InventoryManager");
	private ArrayList<Joueur> players = new ArrayList<Joueur>();
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
		String cle = splitedInformations[0].replace("\"", "");
		String nomJoueur = splitedInformations[1].replace("\"", "");
		int numberOfCards = Integer.parseInt(splitedInformations[2].replace("\"", ""));
		
		Joueur player = new Joueur(cle, nomJoueur);
		
		// Create cards
		for(int i = 0; i < numberOfCards; ++i) {
			String cardTitle = splitedInformations[3+(3*i)].replace("\"", "");
			String teamName = splitedInformations[4+(3*i)].replace("\"", "");
			int cardYear = Integer.parseInt(splitedInformations[5+(3*i)].replace("\"", ""));
			
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
		int i = 1;
		while (i != 0){
			System.out.println("Application de gestion de cartes de baseball");
			System.out.println(" ");
			System.out.println("Voici la liste d'opérations valides :");
			System.out.println("1. Ajouter un joueur");
			System.out.println("2. Afficher l'information d'un joueur");
			System.out.println("3. Mise à jour de l'information d'un joueur");
			System.out.println("4. Effacer l'information d'un joueur");
			System.out.println("5. Liste des joueurs");
			System.out.println("6. Sauvegarde");
			System.out.println(" ");
			System.out.println("0. Sortir");
			System.out.print("Votre sélection :");
			try{
	            i = Integer.parseInt(br.readLine());
	        }catch(NumberFormatException nfe){
	            System.err.println("Invalid Format!");
	        } catch (IOException e) {
				e.printStackTrace();
			}
			
			redirectToOption(i);
		}
		exit();
	}
	/**
	 * Redirect to a function depending on the number of the option chosen.
	 * 
	 * @param option - Number of the option chosen by the user
	 */
	public void redirectToOption(int option)
	{
		switch (option){
		case 1:
			addPlayer();
			break;
		case 2:
			showPlayer();
			break;
		case 3:
			updatePlayer();
			break;
		case 4:
			deletePlayerInfo();
			break;
		case 5:
			showPlayerList();
			break;
		case 6:
			save();
			break;
		default:
		}
		pauseProg();
	}
	
	/**
	 * Creates a player and adds him to the player array
	 * 
	 */
	private void addPlayer(){
		String cle = "";
		Joueur joueur;
		String nomJoueur = "";
		int nbrCartes = 0;
		
		
		System.out.println("Option sélectionnée : 1. Ajouter un joueur");
		System.out.println(" ");
		System.out.println("Entrez la clé d'identification du joueur :");
		
		try{
            cle = br.readLine();
            System.out.println("Entrez le nom du joueur :");
            nomJoueur = br.readLine();
            
            joueur = new Joueur(cle, nomJoueur);
            players.add(joueur);
            
            System.out.println("Combien de cartes? :");
            nbrCartes =  Integer.parseInt(br.readLine());
            
            addCards(nbrCartes, joueur);
            
            System.out.println("L'enregistrement du joueur a réussi.");
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        } catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Asks the user the informations of the cards and adds them to the player.
	 * 
	 * @param nbrCartes - the number of cards the user wants to add.
	 * @param joueur - the player that the user wants to add cards to.
	 */
	private void addCards(int nbrCartes, Joueur joueur){
		String titreCarte = "";
		String equipeCarte = "";
		int anneeCarte = 0;
		try{
			for (int i = 0; i < nbrCartes; i++){
				int num = i+1;
	        	System.out.println("Entrez le titre de la carte " + num + " :");
	            titreCarte = br.readLine();
	        	
	        	System.out.println("Entrez l’équipe de la carte " + num + " :");
	            equipeCarte = br.readLine();
	        	
	        	System.out.println("Entrez l’année de parution de la carte " + num + " :");
	            anneeCarte = Integer.parseInt(br.readLine());
	        	joueur.addCarte(new Carte(titreCarte, equipeCarte, anneeCarte));
	        } 
		}catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        } catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	
	}
	/**
	 * Shows the player.
	 * 
	 */
	private void showPlayer() {

		System.out.println("Option sélectionné: 2. Afficher l'information d'un joueur");
		
		showInfoPlayer();
	}
	/**
	 * Shows the information of the player chosen by the user.
	 * 
	 */
	private int showInfoPlayer(){
		String cle = "";
		int position = 0;
		
		System.out.println(" ");
		System.out.println("Entrez la clé d'identification du joueur:");
		
		try{
            cle = br.readLine();
            for(position = 0; position < players.size(); position++){
            	if(players.get(position).getCle().equals(cle)){
            		break;
            	}
            }
           if(position == players.size()){
        	   System.out.println("Le joueur n'existe pas");
           }
           else{
        	   System.out.println("Voici l'information sauvegardé de: " + players.get(position).getNomJoueur());
        	   int nbrCartes = players.get(position).getNombreCartes();
        	   System.out.println("Le joueur a " + nbrCartes + " cartes enregistrées");
        	   
        	   ArrayList<Carte> cartes = players.get(position).getCartes();
        	   for (int j = 0; j < nbrCartes; j++){
        		   int num = j+1;
        		System.out.println("Carte " + num + " :");
               	System.out.println("Titre : " + cartes.get(j).getTitreCarte());
               	System.out.println("Équipe : " + cartes.get(j).getNomEquipe());
               	System.out.println("Année de parution :  " + cartes.get(j).getAnneeSortie());
               } 
           } 
        } catch (IOException e) {
			e.printStackTrace();
		} 
		return position;
	}
	/**
	 * Updates the information of the player that the user chose,.
	 * 
	 */
	private void updatePlayer(){
		int nbrCartes = 0;
		int position = 0;
		String nomJoueur = "";
		
		System.out.println("Option sélectionné: 3. Mise à jour de l'information d'un joueur");
		
		position = showInfoPlayer();
		
		try{
			if(position != players.size()){
				System.out.println(" ");
				System.out.println("Maintenant entrée les données à modifier:");
	          	System.out.println("Entrez le nom du joueur:");
	          	nomJoueur = br.readLine();
	          	players.get(position).setNomJoueur(nomJoueur);
	          	System.out.println("Combien de cartes:");
	          	nbrCartes =  Integer.parseInt(br.readLine());
	
	          	addCards(nbrCartes, players.get(position));
			}
		}catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        } catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} 
	}
	/**
	 * Deletes the information of the player and the player himself that the user chose.
	 * 
	 */
	private void deletePlayerInfo(){
		int position = 0;
		String reponse ="";
		String nomJoueur = "";
		
		System.out.println("Option sélectionné: 4. Effacer l'information d'un joueur");
		
		position = showInfoPlayer();
		
		try{
			if(position != players.size()){
	           System.out.println("Voulez vous effacer l'information de ce joueur ? (O/N)");
	           reponse =  br.readLine();
	           nomJoueur = players.get(position).getNomJoueur();
	           if (reponse.equals("O") || reponse.equals("o")){
	        	   players.remove(position);
	        	   System.out.println("L'information du joueur " + nomJoueur + " a été efface du système.");
	           }
	           else {
	        	   System.out.println("L'information du joueur " + nomJoueur + " n'a pas été efface du système.");
	           }
			}
		}catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        } catch (IOException e) {
			e.printStackTrace();
		} 
	}
	/**
	*@TODO
	*/
	private void showPlayerList(){
		
		String reponse = "";
		File oldStorageFile = storageFile;
		
		System.out.println("Option sélectionné: 5. Liste de joueurs \n");
		System.out.println("Voulez-vous creer la liste des joueurs dans un fichier ou l'afficher sur l'ecran ? (F/E): ");
		
		try{
			reponse = br.readLine();
			
			switch(reponse){
			case "E": 
				for(Joueur j: players)
				{
					System.out.println("Joueur : " + j.getCle());
					System.out.println("Voici l'information sauvegardé de : " + j.getNomJoueur());
					System.out.println("Le joueur a " + j.getNombreCartes() + " cartes enregistrées");
					ArrayList<Carte> cartes = j.getCartes();
					for (int i = 0; i < j.getNombreCartes(); i++){
		        		   int num = i+1;
		        		System.out.println("Carte " + num + " :");
		               	System.out.println("Titre : " + cartes.get(i).getTitreCarte());
		               	System.out.println("Équipe : " + cartes.get(i).getNomEquipe());
		               	System.out.println("Année de parution :  " + cartes.get(i).getAnneeSortie());
		               }
					System.out.println("\n");
				}
				break;
			case "F":
				try{
				System.out.println("Entrez le nom du fichier : ");
				reponse = br.readLine();
				storageFile = new File(reponse);
				if(!storageFile.exists()){
					storageFile.createNewFile();
				}
				savePlayersToStorage();
				System.out.println("Liste des joueurs à l'endroit suivant : " + storageFile.getPath());
				storageFile = oldStorageFile;
				}
				catch (FailedToSaveInventoryException e)
				{
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Choix invalide, veuillez entrée E ou F");
				break;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		
		
	}
	/**
	*@TODO
	*/
	private void save(){
		
	}
	/**
	*@TODO
	*/
	private void exit(){
		
	}

	public static void pauseProg(){
		System.out.println("Press enter to continue...");
		try {
			br.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}