import java.io.File;
import java.io.IOException;

public class ResourceLoarder {
	
	private String sourceDir;
	
	public static final String TARGETS_IMAGES_DIR = "assets/targets";
	
	public ResourceLoarder(String dir) {
		
		this.sourceDir = new File(dir).getAbsolutePath();
	}
	
	public void setSourceDir(String dir) {
		
		this.sourceDir = dir;
	}
	
	public String getSourceDir() {
		
		return this.sourceDir+"/";
	}
	
	public File getResource(String name) {
		
		return new File(getSourceDir()+name);
	}
}
