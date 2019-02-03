import java.util.ArrayList;
import java.util.List;


class CareTaker {
	
	private List<Memento> mementoList;
	private int index;

	public CareTaker() {
		mementoList = new ArrayList<Memento>();
		index = 0;
	}
	
	public int getIndex() {
		return index;
	}

	public void add(Memento state) {
		if (state != null) {
			mementoList.add(state);
			index++;
			// = mementoList.size() - 1;
		}
	}

	public Memento getPrev() {
		if (mementoList.isEmpty() || index <= 0) {
			return null;
		}
		Memento m = mementoList.get(index-1);
		mementoList.remove(index-1);
		index--;
		return m;
	}
	
}
