package common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;

import model.models.Item;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;

public class HashMapWriter {

	ArrayList<ProductContainer> keyList;
	ArrayList<HashMap<ProductBarcode, SortedSet<Item>>> valueList;

	public HashMapWriter(
			HashMap<ProductContainer, HashMap<ProductBarcode, SortedSet<Item>>> bigMap) {
		this.keyList = new ArrayList<ProductContainer>();
		this.valueList = new ArrayList<HashMap<ProductBarcode, SortedSet<Item>>>();
		Collection<ProductContainer> keySet = bigMap
				.keySet();
		Iterator<ProductContainer> iter = keySet
				.iterator();
		while (iter.hasNext()) {
			ProductContainer key = iter.next();
			this.keyList.add(key);
			this.valueList.add(bigMap.get(key));
		}
	}

	public HashMapWriter(
			ArrayList<ProductContainer> keyList,
			ArrayList<HashMap<ProductBarcode, SortedSet<Item>>> valueList) {
		this.keyList = keyList;
		this.valueList = valueList;
	}

	public ArrayList<ProductContainer> getKeyList() {
		return this.keyList;
	}

	public ArrayList<HashMap<ProductBarcode, SortedSet<Item>>> getValueList() {
		return this.valueList;
	}

	public void loadMap(
			HashMap<ProductContainer, HashMap<ProductBarcode, SortedSet<Item>>> map) {
		assert this.keyList != null;
		assert this.valueList != null;
		for (int c = 0; c < this.keyList.size(); c++) {
			map.put(this.keyList.get(c),
					this.valueList.get(c));
		}
	}
}
