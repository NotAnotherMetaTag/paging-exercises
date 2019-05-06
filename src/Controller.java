import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Controller {

	static Scanner console = new Scanner(System.in);
	static int input;
	static int storeSize = 0;
	static Queue<Integer> pageRefArray = new LinkedList<Integer>();
	static int lruFaultCount = 0;
	static int fifoFaultCount = 0;
	static Frame frame = new Frame();
	static ArrayList<Frame> mainStore = new ArrayList<Frame>();
	static int frameCounter = 0;

	public static void main (String[] args) {

		System.out.println("Select Paging Algorithm");
		System.out.println("1. First In First Out");
		System.out.println("2. Least Recently Used");
		input = console.nextInt();
		System.out.println("Enter main store size in pages (1-10)");
		storeSize = console.nextInt();
		loadRefArray();
		
		System.out.println("Initialized Page Reference String");
		if(input == 1) {
			fifo(storeSize);
		}
		else if(input == 2) {
			lru(storeSize);
		}
	}
	
	private static void loadRefArray() {
		pageRefArray.add(1);
		pageRefArray.add(2);
		pageRefArray.add(3);
		pageRefArray.add(4);
		pageRefArray.add(2);
		pageRefArray.add(1);
		pageRefArray.add(5);
		pageRefArray.add(6);
		pageRefArray.add(2);
		pageRefArray.add(1);
		pageRefArray.add(2);
		pageRefArray.add(3);
		pageRefArray.add(7);
		pageRefArray.add(6);
		pageRefArray.add(3);
		pageRefArray.add(2);
		pageRefArray.add(1);
		pageRefArray.add(2);
		pageRefArray.add(3);
		pageRefArray.add(6);
	}

	private static int lru (int storeSize) {
		//need a counter for when each value was last changed
		
		while(!pageRefArray.isEmpty()) {
		int newPage = pageRefArray.poll();
		
		//check if frame is empty
		if(!mainStore.isEmpty()) {		
			//check if page[] in mainStore contains next page
			if(isPageFault(newPage, false)) {
				//page fault
				lruFaultCount++;
				//not all frames are in use
				if(mainStore.size() < storeSize) {
					Frame newFrame = makeNewFrame(newPage);
					incrementLastUsed();
					mainStore.add(newFrame);
					printFault(lruFaultCount);
				} else { //all frames are in use, time to calculate last recently used
					int lru = calculateLRU();
					Page newPageObj = new Page(newPage, 0);
					//by incrementing before setting, i can simplify the increment logic.
					incrementLastUsed();
					mainStore.get(lru).setCurrentPage(newPageObj);
					
					printFault(lruFaultCount);
				}
			} else { //no page fault
				//the lastUsed value was updated in isPageFault
				incrementLastUsed();
			}
		}
		else { //frame is empty, page fault -- should only happen once per run
			lruFaultCount++;
			//incrementLastUsed();
			Frame newFrame = makeNewFrame(newPage);
			mainStore.add(newFrame);
			printFault(lruFaultCount);

		}
		}
		return lruFaultCount;
	}

	private static int fifo(int storeSize) {
		//need a counter for when each value was last changed
		
				while(!pageRefArray.isEmpty()) {
				int newPage = pageRefArray.poll();
				
				//check if frame is empty
				if(!mainStore.isEmpty()) {		
					//check if page[] in mainStore contains next page
					if(isPageFault(newPage, true)) {
						//page fault
						fifoFaultCount++;
						//not all frames are in use
						if(mainStore.size() < storeSize) {
							Frame newFrame = makeNewFrame(newPage);
							incrementLastUsed();
							mainStore.add(newFrame);
							printFault(fifoFaultCount);
						} else { //all frames are in use, time to calculate FIFO
							int fifo = calculateFIFO();
							Page newPageObj = new Page(newPage, 0);
							//by incrementing before setting, i can simplify the increment logic.
							incrementLastUsed();
							mainStore.get(fifo).setCurrentPage(newPageObj);
							
							printFault(fifoFaultCount);
						}
					} else { //no page fault
						incrementLastUsed();
					}
				}
				else { //frame is empty, page fault -- should only happen once per run
					fifoFaultCount++;
					//incrementLastUsed();
					Frame newFrame = makeNewFrame(newPage);
					mainStore.add(newFrame);
					printFault(fifoFaultCount);

				}
				}
		return fifoFaultCount;
	}
	
	private static Frame makeNewFrame(int newPage) {
		Page newPageObj = new Page(newPage, 0);
		Frame newFrame = new Frame(frameCounter, newPageObj);
		frameCounter++; 
		return newFrame;
	}
	
	private static Boolean isPageFault(int newPage, boolean isFIFO) {
		for(int i=0; i<mainStore.size(); i++) {	
			//the memory already had this page. no page fault.
			if(newPage == mainStore.get(i).getCurrentPage().getPageValue()) {
				//reset last used timer
				if(isFIFO) {
					incrementLastUsed();
					return false;
				} else if (!isFIFO) {
				incrementLastUsed();
				mainStore.get(i).getCurrentPage().setLastUsed(0);
				return false;
				}
			}
			}
		return true;
	}

	private static int calculateLRU() {
		//returns index of least recently used frame
		int leastRecent = 0;
		int currentItem = 0;
		int indexOfLRU = 0;
		for(int i=0; i<mainStore.size(); i++) {
			currentItem = mainStore.get(i).getCurrentPage().getLastUsed();
			if (currentItem > leastRecent) {
				leastRecent = currentItem;
				indexOfLRU = mainStore.get(i).getFrame();
			} else if (currentItem < leastRecent){
				//indexOfLRU = frame# of leastRecent;
				indexOfLRU = mainStore.get(indexOfLRU).getFrame();
			}
		}
		return indexOfLRU;
	}

	private static int calculateFIFO() {
		int firstIn = 0;
		int currentItem = 0;
		int indexOfFIFO = 0;
		for(int i=0; i<mainStore.size(); i++) {
			currentItem = mainStore.get(i).getCurrentPage().getLastUsed();
			if (currentItem > firstIn) {
				firstIn = currentItem;
				indexOfFIFO = mainStore.get(i).getFrame();
			}
		}
		
		return indexOfFIFO;
	}
	
	private static void incrementLastUsed() {
		for(int i=0; i<mainStore.size(); i++) {
			int lastUsed = mainStore.get(i).getCurrentPage().getLastUsed();
			lastUsed++;
			mainStore.get(i).getCurrentPage().setLastUsed(lastUsed);
		}
	}
	
	private static void printFault(int fault) {
		System.out.println("Fault # " + fault);
		System.out.println("Mainstore Contents");
		System.out.println("Frame     Page");
		for(int i=0; i< mainStore.size(); i++) {
			System.out.println(i +"           "+mainStore.get(i).getCurrentPage());
		}
		if(mainStore.size() < storeSize) {
			for(int i = mainStore.size(); i<storeSize; i++) {
				System.out.println(i +"           EMPTY");
			}
		}
	}
}