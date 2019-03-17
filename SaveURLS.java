package m2oReloadedDownloader;

import java.util.List;

public class SaveURLS implements Runnable {
	
	
	String pathName="";
	String urlPageEpisode="";
	String eplinks="";
	String progName="";

	@Override
	public void run() {
		ListParser lp = new ListParser();
		lp.insertEpisodesUrls(urlPageEpisode, Integer.MAX_VALUE);
		List<ListParser.Episodio> episodi = lp.getEpisodi();
        for (ListParser.Episodio ep : episodi){
        	eplinks+="\n"+ep.getUrl();
        	eplinks+="\t"+ep.getTitle();
        }
        IOutils.writeToTxtFile(eplinks, pathName+"\\", true);
        System.out.println("\nFine estrazione links: "+progName);
		
	}

	public SaveURLS(String pathName, String urlPageEpisode, String progName) {
		super();
		this.pathName = pathName;
		this.urlPageEpisode = urlPageEpisode;
		this.progName = progName;
	}

}
