package m2oReloadedDownloader;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Main {
	static HashMap<String, String> programmi;
    public static void main(String[] args) {
    	String home=System.getProperty("user.home"); //rilevo la home directory dell'utente
    	programmi = new HashMap<>(); //hashmap nomeprogramma : relativo link
    	programmi.put("WAVES", "https://www.m2o.it/programmi/waves/puntate/"); //Attualmente non funzionante, solo per questo programma è necessario utilizzare un diverso selettore
    	programmi.put("SOUNDZRISE", "https://www.m2o.it/programmi/soundzrisei-suoni-dellalba/puntate/");
    	programmi.put("GDC", "https://www.m2o.it/programmi/g-d-c/puntate/");
    	programmi.put("HOUSEHISTORY", "https://www.m2o.it/programmi/house-history/puntate/");
    	programmi.put("REALTRUST", "https://www.m2o.it/programmi/real-trust/puntate/");
    	programmi.put("TRANCEEVOLUTION", "https://www.m2o.it/programmi/trance-evolution/puntate/");
    	programmi.put("SIGNALHILLS", "https://www.m2o.it/programmi/signal-hills/puntate/");
    	programmi.put("INDAHOUSE", "https://www.m2o.it/programmi/in-da-silva-house/puntate/");
    	programmi.put("TechnoCulture", "https://www.m2o.it/programmi/techno-culture/puntate/");
    	programmi.put("ToTheClub", "https://www.m2o.it/programmi/to-the-club/puntate/"); 
    	

    	ExecutorService ex = Executors.newCachedThreadPool(); //creo instanza ServiceExecutor
    	for (String nome : programmi.keySet()){ //per ogni programma creo un thread che preleva i link da salvare
    		String prpath=home+"\\"+nome+".txt"; //creo nome path file txt in base al nome del programma
    		ex.execute(new SaveURLS(prpath, programmi.get(nome), nome)); //mando in esecuzione il thread
    	}
    	ex.shutdown();
    }
    
}