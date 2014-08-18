package src.ddpsc.resumeDownloadApplet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import src.ddpsc.database.snapshot.Snapshot;

public class SnapshotZipFile
{
	CSV csv;
	Set<String> tiles;

	public SnapshotZipFile(File file) throws ZipException, IOException, MalformedCSVException, NoCSVFileException
	{
    	ZipFile zipFile = new ZipFile(file);
		Set<String> tiles = new HashSet<String>();
		CSV csv = null;
		
		
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			
			ZipEntry entry = entries.nextElement();
			
			// If it's the CSV file, load it
			boolean entryIsCSV = entry.getName().toLowerCase().contains(".csv");
			if (entryIsCSV) {
				
				List<String> lines = new ArrayList<String>();
				StringBuilder line = new StringBuilder("");
				
				InputStream input = zipFile.getInputStream(entry);
	    		while(input.available() > 0) {
	    			
	    			char letter = (char) input.read();
	    			if (letter == '\n') {
	    				lines.add(line.toString());
	    				line = new StringBuilder("");
	    			}
	    			else {
	    				line.append(letter);
	    			}
	    		}
	    		input.close();
	    		
	    		String labels = lines.get(0);
	    		List<String> values = lines.subList(1, lines.size());
	    		
	    		csv = new CSV(labels, values);
			}
			
			// If it's not the CSV, just save the filename as a tile (if the tile is not 0 bytes)
			else {
				if (entry.getSize() > 0) {
					String fullTilePath = entry.getName();
					String tileNameOnly = fullTilePath;
					tileNameOnly = tileNameOnly.replaceAll("snapshot\\d+/", "");
					tileNameOnly = tileNameOnly.replaceAll(".png", "");
					
					tiles.add(tileNameOnly);
				}
			}
		}
		
		zipFile.close();
		
		if (csv == null) {
			throw new NoCSVFileException("CSV file not found in zip '" + file.getName() + "'.");
		}
		this.csv = csv;
		this.tiles = tiles;
    }
	
	public CSV replacementCSV() throws MalformedCSVException
	{
		Set<String> missingSnapshots = missingSnapshots();
		CSV fixedCSV = new CSV(csv);
		
		for (String missingSnapshot : missingSnapshots)
			fixedCSV.remove(Snapshot.ID, missingSnapshot);
		
		return fixedCSV;
	}
	
	public String missingSnapshots_csv() throws MalformedCSVException
	{
		return CSV.collectionToCSV(missingSnapshots());
	}
	
	private Set<String> missingSnapshots() throws MalformedCSVException
	{
		Set<String> missingSnapshots = new HashSet<String>();
		
		for (int i = 0; i < csv.numRows(); i++) {
			String snapshotId = csv.get(Snapshot.ID, i);
			String tiles = csv.get(Snapshot.TILES, i); // tile image filenames separated by ";"
			List<String> tileNames = Arrays.asList(tiles.split(";"));
			
			for (String tileName : tileNames) {
				if ( ! this.tiles.contains(tileName)) // Check the current tile against this zip file's downloaded tiles
					missingSnapshots.add(snapshotId);
			}
		}
		
		return missingSnapshots;
	}
}





