package umlParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

public class PlantUmlGenerator {

	public void createUML(String fName) {
		File src = new File(fName);
		SourceFileReader read;
		List<GeneratedImage> list;
		try {
			read = new SourceFileReader(src);
			list = read.getGeneratedImages();
			File pngImage = list.get(0).getPngFile();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
