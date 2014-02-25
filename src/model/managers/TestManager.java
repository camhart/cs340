package model.managers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestManager implements Saveable {

	private ArrayList<String> strings;

	public TestManager() {
		this.strings = new ArrayList<String>();
		this.strings.add("Cameron,");
		this.strings.add(" Joel,");
		this.strings.add(" Chris,");
		this.strings.add("and Adam");
		this.strings.add("are");
		this.strings.add("the");
		this.strings.add("coolest.");

	}

	public List<String> getStrings() {
		return this.strings;
	}

	@Override
	public void writeObjects(
			ObjectOutputStream objectOut) {
		try {
			objectOut.writeObject(this.strings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readObjects(
			ObjectInputStream objectIn) {
		try {
			this.strings = (ArrayList<String>) objectIn
					.readObject();
		} catch (Exception e) { // ClassNotFoundException | IOException
			e.printStackTrace();
		}
	}

	@Override
	public String getDataFileName() {
		return "test";
	}

}
