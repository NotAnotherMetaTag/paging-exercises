
public class Page {

	int pageValue;
	//lastUsed is used interchangeable with lastUpdated for FIFO
	int lastUsed;
	
	public Page() {
		pageValue = 0;
		lastUsed = 0;
	}
	
	public Page(int pageValue, int lastUsed) {
		this.pageValue = pageValue;
		this.lastUsed = lastUsed;
	}
	
	public int getPageValue() {
		return pageValue;
	}

	public void setPage(int page) {
		this.pageValue = page;
	}

	public int getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(int lastUsed) {
		this.lastUsed = lastUsed;
	}
	
	public void incrementLastUsed() {
		this.lastUsed = lastUsed++;
	}
	
	public String toString() {
		return ""+pageValue;
	}
	
}
