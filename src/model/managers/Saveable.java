package model.managers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Saveable {
	public void writeObjects(
			ObjectOutputStream objectOut);

	public void readObjects(
			ObjectInputStream objectIn);

	public String getDataFileName();
}
