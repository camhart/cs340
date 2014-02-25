package model.managers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import model.models.Item;
import model.models.Product;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;
import model.models.productContainer.ProductGroup;
import model.models.productContainer.StorageUnit;

public class SerializationManager extends
		SessionManager {

	private List<Saveable> managers;

	private static SerializationManager manager;

	private SerializationManager() {
		this.managers = new ArrayList<Saveable>();
		this.managers.add(ItemManager
				.getInstance());
		this.managers.add(ProductGroupManager
				.getInstance());
		this.managers.add(ProductManager
				.getInstance());
		this.managers.add(StorageUnitManager
				.getInstance());
	}

	public static SerializationManager getInstance() {
		if (manager == null) {
			manager = new SerializationManager();
		}
		return manager;
	}

	@Override
	public void saveState() {
		for (int c = 0; c < this.managers.size(); c++) {
			File hitFile = new File(
					"data"
							+ System.getProperty("file.separator")
							+ this.managers
									.get(c)
									.getDataFileName()
							+ ".data");

			if (!hitFile.getParentFile().exists()) {
				hitFile.getParentFile().mkdirs();
			}
			ObjectOutputStream hitOut = null;
			try {
				if (!hitFile.exists()) {
					hitFile.createNewFile();
				}
				if (hitFile.canWrite()) {
					OutputStream outputStream = new BufferedOutputStream(
							new FileOutputStream(
									hitFile));
					hitOut = new ObjectOutputStream(
							outputStream);
					this.managers.get(c)
							.writeObjects(hitOut);
				}
			} catch (IOException e) {

			} finally {
				if (hitOut != null) {
					try {
						hitOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	@Override
	public void loadState() {
		for (int c = 0; c < this.managers.size(); c++) {
			File hitFile = new File(
					"data"
							+ System.getProperty("file.separator")
							+ this.managers
									.get(c)
									.getDataFileName()
							+ ".data");

			if (!hitFile.getParentFile().exists()) {
				hitFile.getParentFile().mkdirs();
			}

			ObjectInputStream hitIn = null;
			try {
				if (hitFile.exists()
						&& hitFile.canRead()) {
					InputStream inputStream = new BufferedInputStream(
							new FileInputStream(
									hitFile));
					hitIn = new ObjectInputStream(
							inputStream);
					this.managers.get(c)
							.readObjects(hitIn);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (hitIn != null) {
					try {
						hitIn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void addStorageUnit(
			StorageUnit storageUnit) {

	}

	@Override
	public void editStorageUnit(
			StorageUnit oldStorageUnit,
			StorageUnit newStorageUnit) {

	}

	@Override
	public void deleteStorageUnit(
			StorageUnit storageUnit) {

	}

	@Override
	public void addItem(Item item) {

	}

	@Override
	public void consumeItem(Item item) {

	}

	@Override
	public void moveItem(Item item,
			ProductContainer destination) {

	}

	@Override
	public void addProduct(Product product) {

	}

	@Override
	public void editProduct(Product newProduct) {

	}

	@Override
	public void deleteProduct(
			ProductBarcode productBarcode) {

	}

	@Override
	public void addProductGroup(
			ProductGroup productGroup) {

	}

	@Override
	public void editProductGroup(String oldName,
			ProductContainer parent) {

	}

	@Override
	public void deleteProductGroup(
			ProductGroup productGroup) {

	}

	@Override
	public void deleteDataFiles() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveState(List<Saveable> managers) {
		saveState();

	}

	@Override
	public void loadState(List<Saveable> managers) {
		loadState();

	}
}
