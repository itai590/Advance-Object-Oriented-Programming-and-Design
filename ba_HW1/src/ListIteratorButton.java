
/**
 * @HW 1
 * @author Itai
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class ListIteratorButton extends CommandButton {

	private LinkedHashMap<Address, Address> Map = new LinkedHashMap<>();
	private ListIterator<Address> lit;
	private boolean firstClick = true;

	public ListIteratorButton(AddressBookPane pane, RandomAccessFile r) {
		super(pane, r);
		this.setText("Iter");

		try {
			lit = listIterator();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Execute() {
		// Iterator<Address> it = null;
		if (firstClick) {

			/* copy to map */
			while (lit.hasNext()) {
				Address tmp = lit.next();
				Map.put(tmp, tmp);
			}

			/* delete all elements from file */
			while (lit.hasPrevious()) {
				lit.previous();
				lit.remove();
			}

			/* copy elements from map to file */
			for (Map.Entry<Address, Address> entry : Map.entrySet()) {
				lit.add(entry.getValue());
			}

			// it = Map.values().iterator();

			firstClick = false;

		} else {
			TreeSet<Address> tree = new TreeSet<Address>(new Comparator<Address>() {

				@Override
				public int compare(Address o1, Address o2) {
					int ans = o1.getStreet().compareTo(o2.getStreet());
					return ans == 0 ? 1 : ans;
				}
			});

			// copy map to tree
			tree.addAll(Map.values());

			/* delete all elements from file */
			while (lit.hasPrevious()) {
				lit.previous();
				lit.remove();
			}

			/* copy elements from tree to file */
			for (Address entry : tree) {
				lit.add(entry);
			}

			// it = tree.iterator();
		}

		// updateFile(it);

		/* show first element at the javaFx UI */
		try {
			readAddress(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateFile(Iterator<Address> it) {
		/* remove all elements from the file */
		while (lit.hasPrevious()) {
			lit.previous();
			lit.remove();
		}

		while (it.hasNext()) {
			lit.add(it.next());
		}

	}

	public ListIterator<Address> listIterator(int index) throws IOException, FileNotFoundException {
		return new ListIter(index);
	}

	public ListIterator<Address> listIterator() throws IOException, FileNotFoundException {
		return new ListIter(0);
	}

	public class ListIter implements ListIterator<Address> {
		private int current = 0;
		private int last = -1;
		private long numElements;

		public ListIter(int current) throws IOException {
			this.current = current;
			this.numElements = raf.length() / CHAR_SIZE / RECORD_SIZE;
		}

		@Override
		public void add(Address e) {
			if (current < 0 || current > CHAR_SIZE * RECORD_SIZE)
				throw new IndexOutOfBoundsException();
			ArrayList<Address> list;
			try {
				list = (ArrayList<Address>) readFileToArrayList();
				list.add(current, e);
				writeArrayListToFile(list);
				numElements++;
				current++;
				last = -1;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public boolean hasNext() {
			return (current < numElements);
		}

		@Override
		public boolean hasPrevious() {
			return (current > 0);
		}

		@Override
		public Address next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Address tmp = null;
			try {
				tmp = (Address) readAddress(current * CHAR_SIZE * RECORD_SIZE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			last = current;
			current++;
			return tmp;
		}

		@Override
		public int nextIndex() {
//			if (!hasNext())
//				return (int) numElements;
//			else
//				return current++;
			return current;
		}

		@Override
		public Address previous() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			current--;
			Address tmp = null;
			try {
				tmp = (Address) readAddress(current * CHAR_SIZE * RECORD_SIZE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			last = current;
			return tmp;
		}

		@Override
		public int previousIndex() {
			return current--;
		}

		@Override
		public void remove() {
			if (last == -1)
				throw new IllegalStateException();

			ArrayList<Address> list;
			try {
				list = readFileToArrayList();
				list.remove(last);
				writeArrayListToFile(list);
				numElements--;
				current = last;
				last = -1;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void set(Address e) {
			if (last == -1)
				throw new IllegalStateException();
			try {
				raf.seek(last * CHAR_SIZE * RECORD_SIZE);
				writeAddress(e, last * CHAR_SIZE * RECORD_SIZE);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		private void writeArrayListToFile(ArrayList<Address> list) throws IOException {
			raf.seek(0);
			raf.setLength(0);
			for (Address element : list) {
				writeAddress(element, raf.getFilePointer());
			}
		}

		private ArrayList<Address> readFileToArrayList() throws IOException {
			ArrayList<Address> tmp = new ArrayList<>();
			raf.seek(0);
			while (raf.getFilePointer() < raf.length())
				tmp.add(readAddress(raf.getFilePointer()));

			return tmp;
		}

	}
}
