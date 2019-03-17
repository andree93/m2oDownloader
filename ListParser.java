package m2oReloadedDownloader;

import org.jsoup.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListParser {
    public static final String USER_AGENT ="Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Thunderbird/45.8.0";
    public static final String REGEX_MP3 = "(.*(https://media\\.m2o\\.it/.*\\.mp3).*)";
    private static final String REGEX_VALIDAZIONE_URL_LISTA_PUNTATE="(https://www\\.m2o\\.it/programmi/[a-zA-Z0-9]*+.*)";
    private static final String SELETTORE_STRINGA_PAGINA_SUCCESSIVA="next page-numbers";
    private static final String CLASSI_CONTENITORE_URL_EPISODI_E_DESCRIZIONE="title small red";
    private static final String JSOUP_SELECT_HREF="abs:href";
    private static final String A_TAG="a";
    private static final String HREF="href";
    private static final String IFRAME="iframe";
    private String url ="";
    private Elements elementi =null;
    private Document doc;
    private List<Episodio> episodi;

    public ListParser() {
        super();
        episodi = new ArrayList<Episodio>(12);
    }


    public ListParser(String url) {
        super();
        this.url = url;
        episodi = new ArrayList<Episodio>(12);
    }

    public static boolean checkUrl(String url) {

        return Pattern.matches(REGEX_VALIDAZIONE_URL_LISTA_PUNTATE, url);
    }

    public List<Episodio> getEpisodi(){
        return episodi;
    }


    public Elements getElementi() {
        return elementi;
    }


    public void setElementi(Elements elementi) {
        this.elementi = elementi;
    }


    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void insertEpisodesUrls(String firstPage, int maxEp) { //paramentri: URL Pagina 1 della lista episodi del programma, numero di link diretti (.mp3) richiesti
        getEpisodi().clear();
        String nextPage = firstPage;
        parseListUrls(firstPage, maxEp);
        while (nextPage != null && episodi.size()<maxEp) { //se il link della prossima pagina non è null (quindi esiste una pagina successiva) e se gli episodi nella lista non sono piu di quelli che ha richiesto l'utente
            nextPage = getNextPage(nextPage);  //scarico pagina successiva
            parseListUrls(nextPage, maxEp); //inserisco i link della PAGINA di ogni episodio contenuto nella pagina elaborata nello step precedente
            System.out.println("Links trovati: "+getEpisodi().size()); //conteggio e stampa link - solo per test
        }

    }

    public void parseListUrls(String url, int max) {
        Document doc = null;
        if (url != null) { 
            try {
                doc = Jsoup.connect(url).get(); //parsing documento HTML
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements links = doc.getElementsByClass(CLASSI_CONTENITORE_URL_EPISODI_E_DESCRIZIONE); //estrazione degli elementi tramite tag. Ogni elemento contiene link e descrizione ed è salvato in un oggetto di tipo Elements, iterabile
            for (int i=0; (i<links.size()&&i<max); i++){ //itero tra gli elementi con un ciclo for each
                Element link = links.get(i); //ottengo un singolo elemento per ogni iterazione
                Episodio eps = new Episodio(extractURLmp3fromPlayerPage(link.select(A_TAG).attr(JSOUP_SELECT_HREF)),link.text());  // L'oggetto episodio viene creato con URL e Nome estratti tramite i rispettivi tag elencati tra le costanti. L'URL al file .mp3 è costruito tramite il metodo "extractURLmp3fromPlayerPage", che a sua volta prende in ingresso il link alla pagina dell'episodio
                getEpisodi().add(eps); //viene aggiunto un nuovo oggetto Episodio all'arraylist
            }
        }
    }


    public static String getNextPage(String url) {
        Document doc = null;
        String nextUrl = null;
        if (url != null) {
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element el = doc.getElementsByClass(SELETTORE_STRINGA_PAGINA_SUCCESSIVA).first();
            if (el != null) {
                nextUrl = el.absUrl(HREF);
            }
        }
        return nextUrl;
    }



    public static String extractURLmp3fromPlayerPage(String url) {
        String u = url;
        Document doc = null;
        try {
            doc = Jsoup.connect(u).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element urlelement = doc.selectFirst(IFRAME); //estraggo IFRAME contenente il link al file .mp3

        return buildMp3Url(urlelement.toString()); //dall'iframe, estrae la stringa contenente il link diretto al file .mp3
    }


    private static String buildMp3Url(String url) { //tramite regex, estrare il link diretto al file mp3 da una stringa
        String mp3url = null;
        Pattern p = Pattern.compile(REGEX_MP3);
        Matcher m = p.matcher(url);
        while(m.find()) {
            mp3url=m.group(2);
        }
        return mp3url;
    }



    public class Episodio {
        private String title;
        private String url;

        public Episodio(String url, String title) {
            this.setUrl(url);
            this.setTitle(title);
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
    }

}
