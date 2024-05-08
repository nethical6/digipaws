package nethical.digipaws.utils.data;

public class BlockableView {
	private String viewId;
	private String title;
	private String description;
	
	private boolean isEnabled =false;
	
	private int counter = 10; // Stores data for immediate blocker
	private String commonName ="idk";

	
	public BlockableView(String viewId, String title, String description, boolean isEnabled, String commonName) {
		this.viewId = viewId;
		this.title = title;
		this.description = description;
		this.isEnabled = isEnabled;
        this.commonName=commonName;
	}
	
	// Getters and setters
	public String getViewId() {
		return viewId;
	}
    
    public String getCommonName(){
        return commonName;
    }
	public void resetCounter(){
		counter=10;
	}
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	
	public String getTitle() {
		return title;
	}
	
	
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public void decrementCounter() {
		if(counter==0){
			return;
		}
		counter--;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public int getCount() {
		return counter;
	}
	public void resetDetails(){
		counter=10;
	}
	
	
	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
	
	
}