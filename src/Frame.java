
public class Frame {
	int frame;
	Page currentPage;
	public Frame() {
		frame = 0;
	}
	
	public Frame(int frame, Page currentPage) {
		this.frame = frame;
		this.currentPage = currentPage;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public Page getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Page currentPage) {
		this.currentPage = currentPage;
	}
	
}
