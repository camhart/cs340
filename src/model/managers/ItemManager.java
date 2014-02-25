package model.managers;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import model.models.Item;
import model.models.Product;
import model.models.barcode.ItemBarcode;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;
import model.models.productContainer.StorageUnit;
import controller.notify.ItemNotifier;
import controller.notify.ProductNotifier;

/**
 * ItemManager contains all of the Item objects used in the Home Inventory
 * Tracker program. It takes care of adding, removing, moving Items between
 * StorageContainers, and validating Item objects before they are added
 * 
 * @invariant Singleton
 * @author Cameron Hartmann
 * @author Chris McNeill
 * @author Adam Rogers
 * @author Joel Denning
 * @author Group1
 */
public class ItemManager extends Observable
		implements Saveable {

	private static ItemManager manager = null;

	// private //package protected for testing
	private HashMap<ItemBarcode, Item> itemBarcodeMap;
	// private //package protected for testing
	private HashMap<ProductBarcode, SortedSet<Item>> productBarcodeMap;
	// private //package protected for testing
	private HashMap<ProductContainer, SortedSet<Item>> productContainerMap;

	private HashMap<ProductContainer, HashMap<ProductBarcode, SortedSet<Item>>> map;

	private ItemManager() {
		setItemBarcodeMap(new HashMap<ItemBarcode, Item>());
		setProductBarcodeMap(new HashMap<ProductBarcode, SortedSet<Item>>());
		setProductContainerMap(new HashMap<ProductContainer, SortedSet<Item>>());

		this.map = new HashMap<ProductContainer, HashMap<ProductBarcode, SortedSet<Item>>>();
	}

	/**
	 * getInstance() either initializes or returns the ItemManager Singleton
	 * depending on whether it has been created yet.
	 * 
	 * @pre true
	 * @post valid ItemManager Singleton instance returned
	 * @return ItemManager Singleton instance of ItemManager
	 */
	public static ItemManager getInstance() {
		if (manager == null) {
			manager = new ItemManager();
		}
		return manager;
	}

	/**
	 * add(Item newItem) adds newItem to the ItemManager's Items stored. This
	 * happens after validation and creation of the Item. It notifies its
	 * observers that the item was added.
	 * 
	 * @pre newItem is a valid newItem
	 * @pre newItem's productContainer must me set to the proper location.
	 * @post newItem added successfully to ItemManager Map's, the
	 *       productContainer can be changed if a the item's same Product exists
	 *       in a sub productGroup to the items original container.
	 * @param newItem
	 *            The Item object to be added to the ItemManager's set of Items
	 * @return
	 */
	public void add(Item newItem) {
		assert Item.isValid(
				newItem.getItemBarcode(),
				newItem.getEntryDate()) : "Invalid Item attempted to get added";

		System.out.print("Add Item...");

		// If the StorageUnit contains a product group in the product that's
		// where the item needs put.
		ProductContainer destination = newItem
				.getContainer()
				.getStorageUnit()
				.findProductContainer(
						newItem.getProduct()
								.getBarcode());
		if ((destination != null)
				&& !destination.equals(newItem
						.getContainer())) {
			System.out
					.print("Product found in target storage unit, \n\tsetting destination to "
							+ destination
							+ "...\n");
			newItem.setProductContainer(destination);
		} else {
			System.out
					.print("No product found in "
							+ newItem
									.getContainer()
									.getStorageUnit());
		}

		// BIGMAP
		HashMap<ProductBarcode, SortedSet<Item>> productMap = this.map
				.get(newItem.getContainer());

		if (productMap == null) {
			// System.out.println("New map made!");
			productMap = new HashMap<ProductBarcode, SortedSet<Item>>();
			// System.out.println(map.get(newItem.getContainer()) + " vs " +
			// productMap.toString());
			this.map.put(newItem.getContainer(),
					productMap);
			// System.out.println(map.get(newItem.getContainer()) + " vs " +
			// productMap.toString());
		}
		SortedSet<Item> theItems = productMap
				.get(newItem.getProduct()
						.getBarcode());
		if (theItems == null) {
			// System.out.println("New treeSet Made!");
			theItems = new TreeSet<Item>();
			productMap.put(newItem.getProduct()
					.getBarcode(), theItems);
		}
		theItems.add(newItem);
		//
		// System.out.println("bigmap: " + map.keySet().size());
		// System.out.println("theItems: " + theItems.size());

		// Add item to item barcode map
		getItemBarcodeMap()
				.put(newItem.getItemBarcode(),
						newItem);

		// Add item to product barcode map
		if (getProductBarcodeMap()
				.containsKey(
						newItem.getProduct()
								.getBarcode())) {
			SortedSet<Item> itemList = getProductBarcodeMap()
					.get(newItem.getProduct()
							.getBarcode());

			if (itemList == null) {
				itemList = new TreeSet<Item>();
			}
			itemList.add(newItem);

			// productBarcodeMap.put(newItem.getProduct().getBarcode(),
			// itemList); //not necessary?

		} else {
			SortedSet<Item> itemList = new TreeSet<Item>();
			itemList.add(newItem);
			getProductBarcodeMap().put(
					newItem.getProduct()
							.getBarcode(),
					itemList);
		}

		// Add item to product container map
		if (getProductContainerMap().containsKey(
				newItem.getContainer())) {
			SortedSet<Item> itemList = getProductContainerMap()
					.get(newItem.getContainer());
			itemList.add(newItem);
		} else {
			SortedSet<Item> itemList = new TreeSet<Item>();
			itemList.add(newItem);
			getProductContainerMap().put(
					newItem.getContainer(),
					itemList);
		}

		// Add item to list in the container and product to it's list
		if (!newItem.getContainer()
				.containsProduct(
						newItem.getProduct()
								.getBarcode())) {
			// System.out.println();
			newItem.getContainer().addProduct(
					newItem.getProduct());
		}
		newItem.getContainer().addItem(newItem);
		setChanged();
	}

	/**
	 * consume(ItemBarcode barcode) sets the container property of the item with
	 * unique ItemBarcode barcode to null, thus signifying that it has been
	 * removed from its container, or "consumed". It notifies its observers that
	 * the item was consumed.
	 * 
	 * @pre barcode is not null and exists in items
	 * @post item with the given barcode will have it's ItemBarcode set to
	 *       ProductContainer.CONSUMED_ITEMS (null).
	 * @param barcode
	 *            The ItemBarcode object used to find the item being removed.
	 * @return
	 */
	// public void consume(ItemBarcode barcode) {
	public void consume(Item item) {
		assert item != null;
		assert ItemManager
				.getInstance()
				.itemExists(item.getItemBarcode()) : "Item barcode doesn't exist.";
		// Item item = getItemBarcodeMap().get(barcode);

		// BIGMAP
		HashMap<ProductBarcode, SortedSet<Item>> productMap = this.map
				.get(item.getContainer());
		if (productMap == null) {
			assert false : "This shouldn't be empty... "
					+ "how are we consuming an item that doesn't exist?";
			// productMap = new HashMap<ProductBarcode, SortedSet<Item>>();
		}
		SortedSet<Item> theItems = productMap
				.get(item.getProduct()
						.getBarcode());
		if (theItems == null) {
			assert false : "This shouldn't be empty... "
					+ "how are we consuming an item that doesn't exist?";
			// theItems = new TreeSet<Item>();
		}
		theItems.remove(item);

		HashMap<ProductBarcode, SortedSet<Item>> consumedMap = this.map
				.get(ProductContainer.CONSUMED_ITEMS);
		if (consumedMap == null) {
			consumedMap = new HashMap<ProductBarcode, SortedSet<Item>>();
			this.map.put(
					ProductContainer.CONSUMED_ITEMS,
					consumedMap);
		}

		SortedSet<Item> bmConsumedItems = consumedMap
				.get(item.getProduct()
						.getBarcode());
		if (bmConsumedItems == null) {
			bmConsumedItems = new TreeSet<Item>();
			consumedMap.put(item.getProduct()
					.getBarcode(),
					bmConsumedItems);
		}
		bmConsumedItems.add(item);
		//

		// Grab the container the item current sits in
		SortedSet<Item> containerItems = getProductContainerMap()
				.get(item.getContainer());
		assert containerItems != null : "containerItems == null";

		// Remove the item from that container
		containerItems.remove(item);

		// Remove the item from the list contained in the container itself
		item.getContainer().removeItem(item);
		// Consume the item, this sets it's exit time and set product container
		// to CONSUMED_ITEMS
		item.consume();

		// Grab the list of consumedItems
		SortedSet<Item> consumedItems = getProductContainerMap()
				.get(ProductContainer.CONSUMED_ITEMS);
		if (consumedItems == null) {
			consumedItems = new TreeSet<Item>();
			getProductContainerMap()
					.put(ProductContainer.CONSUMED_ITEMS,
							consumedItems);
		}

		consumedItems.add(item);

		// Now lets remove and add back to the other map to keep the sorting
		// right
		// (it sorts by entryDate and exitTime, consume sets exit time).
		SortedSet<Item> itemByProduct = getProductBarcodeMap()
				.get(item.getProduct()
						.getBarcode());
		itemByProduct.remove(item);
		itemByProduct.add(item);

		assert consumedItems != null;
		setChanged();
		notifyObservers(new ItemNotifier());

		ProductManager.getInstance()
				.setChangedOverride();
		ProductManager.getInstance()
				.notifyObservers(
						new ProductNotifier());
	}

	/**
	 * Test method only. Used to test consumed items.
	 * 
	 * @pre true
	 * @post true
	 * @return SortedSet of consumed items, ordered by expiration date.
	 */
	public SortedSet<Item> getConsumedItems() {
		return getProductContainerMap().get(
				ProductContainer.CONSUMED_ITEMS);
	}

	/**
	 * @pre true
	 * @post true
	 * @return SortedSet of consumed items, ordered by expiration date.
	 */
	public SortedSet<Item> getItemByContainer(
			ProductContainer productContainer) {
		return getProductContainerMap().get(
				productContainer);
	}

	public void editItem(Item item,
			Date newEntryDate) {

		// we need to remove the item from each, edit the item, then put it
		// back. This will keep it sorted... or we can sort at the end.

		this.itemBarcodeMap.get(
				item.getItemBarcode())
				.setEntryDate(newEntryDate);

		editItemInSet(item,
				this.productBarcodeMap.get(item
						.getProduct()
						.getBarcode()),
				newEntryDate);

		editItemInSet(item,
				this.productContainerMap.get(item
						.getContainer()),
				newEntryDate);

		editItemInSet(
				item,
				getItemsByContainerAndProduct(
						item.getContainer(), item
								.getProduct()
								.getBarcode()),
				newEntryDate);

		setChanged();
		notifyObservers(new ItemNotifier());
	}

	private void editItemInSet(Item item,
			Set<Item> set, Date newEntryDate) {
		// Item itemByReference = null;
		// Iterator<Item> iter = set.iterator();
		// while(iter.hasNext()) {
		// Item cur = iter.next();
		// //set.remove(cur);
		// if(cur.equals(item)) {
		// itemByReference = cur;
		// //cur.setEntryDate(newEntryDate);
		// }
		// //set.add(cur);
		// }
		set.remove(item);
		item.setEntryDate(newEntryDate);
		set.add(item);
	}

	/**
	 * Used to transfer items. It notifies its observers that the item was
	 * transferred.
	 * 
	 * @pre destination is a valid storage unit
	 * @pre Item is a valid item
	 * @post the item is moved to the target storage unit
	 * @param toMove
	 * @param targetStorageUnit
	 */
	public void transferItem(Item toMove,
			StorageUnit targetStorageUnit) {
		assert StorageUnit
				.isValid(targetStorageUnit
						.getName()) : "Invalid storageunit name";
		assert Item.isValid(
				toMove.getItemBarcode(),
				toMove.getEntryDate());
		assert ItemManager.getInstance()
				.itemExists(
						toMove.getItemBarcode());

		StorageUnit originalStorageUnit = toMove
				.getContainer().getStorageUnit();

		if (originalStorageUnit
				.equals(targetStorageUnit)) {
			// trying to transfer an item to it's own container
			return;
		} else {
			// see if toMove's product is contained within target storage unit
			ProductContainer targetProductContainer = targetStorageUnit
					.findProductContainer(toMove
							.getProduct()
							.getBarcode());

			// if it's not, then set targetProductContainer to targetStorageUnit
			// and add the product to it.
			if (targetProductContainer == null) {
				// add toMove's product to storageUnit
				targetStorageUnit
						.addProduct(toMove
								.getProduct());
				toMove.getProduct()
						.addToContainer(
								targetStorageUnit);
				// ProductManager.getInstance().add(toMove.getProduct());
				ProductManager
						.getInstance()
						.addToContainer(
								toMove.getProduct(),
								targetStorageUnit);
				targetProductContainer = targetStorageUnit;
			}

			// //delete Product from old location if necessary
			// Set<Item> numberOfItems =
			// this.getItemsByContainerAndProduct(toMove.getContainer(),
			// toMove.getProduct().getBarcode());
			// if(numberOfItems.size() == 1) {
			// //delete the product
			// ProductManager.getInstance().deleteFromContainer(toMove.getProduct(),
			// toMove.getContainer());
			// toMove.getContainer().removeProduct(toMove.getProduct());
			// toMove.getProduct().removeFromContainer(toMove.getContainer());
			// }

			moveItem(toMove,
					targetProductContainer);

			ProductManager.getInstance()
					.setChangedOverride();
			ProductManager
					.getInstance()
					.notifyObservers(
							new ProductNotifier());
			setChanged();
			this.notifyObservers(new ItemNotifier());
		}
	}

	/**
	 * move(Item toMove, ProductContainer destination) sets the container
	 * property of the toMove to destination. It notifies its observers that the
	 * item was moved.
	 * 
	 * @pre toMove exists
	 * @pre toMove is a valid non null item
	 * @pre destination is a valid non null product container
	 * @post item gets moved according to specs
	 * @param toMove
	 *            The Item object being moved.
	 * @param targetProductContainer
	 *            The ProductContainer that toMove is being moved to.
	 * @return
	 */
	public void move(
			Item toMove,
			ProductContainer targetProductContainer) {
		assert ItemManager.getInstance()
				.itemExists(
						toMove.getItemBarcode()) : "Item doesn't exist";
		assert Item.isValid(
				toMove.getItemBarcode(),
				toMove.getEntryDate());
		assert ProductContainer
				.isValid(targetProductContainer
						.getName());

		StorageUnit targetStorageUnit = targetProductContainer
				.getStorageUnit();
		StorageUnit originalStorageUnit = toMove
				.getContainer().getStorageUnit();

		// Complete list of items to move
		ArrayList<Item> moveTheseOnes = new ArrayList<Item>();

		if (targetStorageUnit
				.equals(originalStorageUnit)) {
			// case 3

			Set<Item> subItems = getItemsByContainerAndProduct(
					toMove.getContainer(), toMove
							.getProduct()
							.getBarcode());
			if (subItems != null) {
				moveTheseOnes.addAll(subItems);
			}

			toMove.getContainer().removeProduct(
					toMove.getProduct());
			toMove.getProduct()
					.removeFromContainer(
							toMove.getContainer());
			ProductManager
					.getInstance()
					.deleteFromContainer(
							toMove.getProduct(),
							toMove.getContainer());

			targetProductContainer
					.addProduct(toMove
							.getProduct());
			toMove.getProduct().addToContainer(
					targetProductContainer);
			ProductManager
					.getInstance()
					.addToContainer(
							toMove.getProduct(),
							targetProductContainer);

		} else {
			moveTheseOnes.add(toMove);
			ProductContainer targetOldContainer = targetStorageUnit
					.findProductContainer(toMove
							.getProduct()
							.getBarcode());
			if (targetOldContainer == null) {
				// Target Container doesn't have the product in any of its
				// sub-containers
				targetProductContainer
						.addProduct(toMove
								.getProduct());
				toMove.getProduct()
						.addToContainer(
								targetProductContainer);
				ProductManager
						.getInstance()
						.addToContainer(
								toMove.getProduct(),
								targetProductContainer);
			} else if (!targetProductContainer
					.equals(targetOldContainer)) {
				// Move the selected item only
				Set<Item> subItems = getItemsByContainerAndProduct(
						targetOldContainer,
						toMove.getProduct()
								.getBarcode());
				if (subItems != null) {
					moveTheseOnes
							.addAll(subItems);
				}

				ProductManager
						.getInstance()
						.deleteFromContainer(
								toMove.getProduct(),
								targetOldContainer);
				toMove.getProduct()
						.removeFromContainer(
								targetOldContainer);
				targetOldContainer
						.removeProduct(toMove
								.getProduct());

				targetProductContainer
						.addProduct(toMove
								.getProduct());
				toMove.getProduct()
						.addToContainer(
								targetProductContainer);
				ProductManager
						.getInstance()
						.addToContainer(
								toMove.getProduct(),
								targetProductContainer);
			}
			//
			// if (targetOldContainer != null) {
			// // case
			// // Grab list of items from targetOldContainer
			// if (targetOldContainer != null) {
			// Set<Item> subItems = this.getItemsByContainerAndProduct(
			// targetOldContainer, toMove.getProduct()
			// .getBarcode());
			// //if (subItems != null) //changed this to prevent using a
			// checkstyle point ;D
			// moveTheseOnes.addAll(subItems == null ? new ArrayList() :
			// subItems);
			// }
			//
			// // Delete product from targetOldContainer
			// ProductManager.getInstance().deleteFromContainer(
			// toMove.getProduct(), targetOldContainer);
			//
			// // Remove the link from product -> container list
			// toMove.getProduct().removeFromContainer(targetOldContainer);
			// // Remove the product from TargetOldContainer list
			// targetOldContainer.removeProduct(toMove.getProduct());
			//
			// // Add the product to the new container
			// ProductManager.getInstance().addToContainer(
			// toMove.getProduct(), targetProductContainer);
			//
			// }
			//
		}

		for (Item i : moveTheseOnes) {
			moveItem(i, targetProductContainer);
		}

		ProductManager.getInstance()
				.setChangedOverride();
		ProductManager.getInstance()
				.notifyObservers(
						new ProductNotifier());
		setChanged();

		//
		//
		//
		//
		//
		//
		// System.out.print("a->");
		// if(targetStorageUnit.containsProduct(toMove.getProduct().getBarcode()))
		// {
		// System.out.print("b->");
		// ProductContainer oldContainer =
		// targetStorageUnit.findProductContainer(toMove.getProduct().getBarcode());
		// ProductManager.getInstance().addToContainer(toMove.getProduct(),
		// targetProductContainer);
		// if(!oldContainer.equals(targetProductContainer)) {
		// System.out.print("c->");
		//
		// Set<Item> itemsToMove =
		// this.getItemsByContainerAndProduct(oldContainer,
		// toMove.getProduct().getBarcode());
		// if(!itemsToMove.contains(toMove))
		// itemsToMove.add(toMove);
		//
		// Item[] itemArray = itemsToMove.toArray(new Item[0]);
		// for(Item i : itemArray) {
		// this.moveItem(i, targetProductContainer);
		// }
		// // Iterator<Item> iter = itemsToMove.iterator();
		// // while(iter.hasNext()) {
		// // Item curItem = iter.next();
		// // this.moveItem(curItem, targetProductContainer);
		// // }
		// ProductManager.getInstance().deleteFromContainer(toMove.getProduct(),
		// oldContainer);
		//
		// } else {
		// System.out.print("cc->");
		//
		// this.moveItem(toMove, targetProductContainer); //what if toMove is in
		// a different
		// }
		// } else {
		// System.out.print("bb->");
		// ProductManager.getInstance().addToContainer(toMove.getProduct(),
		// targetProductContainer);
		// this.moveItem(toMove, targetProductContainer);
		// }
		// // // look for the product within the storage unit
		// // if
		// (targetStorageUnit.containsProduct(toMove.getProduct().getBarcode()))
		// {
		// // // delete the product from the old container
		// // ProductManager.getInstance().deleteFromContainer(
		// // toMove.getProduct(), toMove.getContainer());
		// //
		// // // move all of the items to the new destination
		// //// SortedSet<Item> itemsToMove = getProductBarcodeMap().get(
		// //// toMove.getProduct().getBarcode());
		// // SortedSet<Item> itemsToMove =
		// this.getItemsByContainerAndProduct(pc, productBarcode)
		// // Iterator<Item> iter = itemsToMove.iterator();
		// // while (iter.hasNext()) {
		// // Item currentItem = iter.next();
		// // // if(currentItem.getProduct())
		// // moveItem(currentItem, targetProductContainer);
		// // }
		// // } else {
		// // // add the product to the new product container
		// // targetProductContainer.addProduct(toMove.getProduct());
		// // moveItem(toMove, targetProductContainer);
		// // }
		// System.out.println("Moved item[s] to " + targetProductContainer);
		// ProductManager.getInstance().setChangedOverride();
		// ProductManager.getInstance().notifyObservers(new ProductNotifier());
		// this.setChanged();
	}

	public void productMoveItems(
			Set<Item> itemsToMove,
			ProductContainer targetContainer,
			ProductContainer oldContainer) {

		Item toMove = itemsToMove.iterator()
				.next();
		Set<Item> itemsToMove2 = getItemsByContainerAndProduct(
				oldContainer, toMove.getProduct()
						.getBarcode());

		Item[] itemArray = itemsToMove2
				.toArray(new Item[0]);
		for (Item i : itemArray) {
			moveItem(i, targetContainer);
		}

		setChanged();
		this.notifyObservers(new ItemNotifier());
	}

	/**
	 * Used to move an individual item to one destination.
	 * 
	 * @pre toMove is valid
	 * @pre destination is valid
	 * @post Item gets moved, all references to the item get updated.
	 * @param toMove
	 * @param destination
	 */
	// could be private once testing is over
	public void moveItem(Item toMove,
			ProductContainer destination) {
		assert Item.isValid(
				toMove.getItemBarcode(),
				toMove.getEntryDate());
		assert ItemManager.getInstance()
				.itemExists(
						toMove.getItemBarcode());
		assert ProductContainer
				.isValid(destination.getName());

		// BIGMAP
		// System.out.println("looking up " + toMove.getContainer().getName() +
		// " - " + map.keySet());
		HashMap<ProductBarcode, SortedSet<Item>> productMap = this.map
				.get(toMove.getContainer());
		if (productMap == null) {
			assert false : "This shouldn't be empty... container->";
			// productMap = new HashMap<ProductBarcode, SortedSet<Item>>();
		}
		SortedSet<Item> theItems = productMap
				.get(toMove.getProduct()
						.getBarcode());
		if (theItems == null) {
			assert false : "This shouldn't be empty... container->product->";
			// theItems = new TreeSet<Item>();
		}
		theItems.remove(toMove);

		HashMap<ProductBarcode, SortedSet<Item>> newProductMap = this.map
				.get(destination);
		if (newProductMap == null) {
			newProductMap = new HashMap<ProductBarcode, SortedSet<Item>>();
			this.map.put(destination,
					newProductMap);
		}

		SortedSet<Item> newItemSet = newProductMap
				.get(toMove.getProduct()
						.getBarcode());
		if (newItemSet == null) {
			newItemSet = new TreeSet<Item>();
			newProductMap.put(toMove.getProduct()
					.getBarcode(), newItemSet);
		}
		newItemSet.add(toMove);

		// Remove item from product container map
		SortedSet<Item> itemList = getProductContainerMap()
				.get(toMove.getContainer());
		itemList.remove(toMove);

		// remove item from product container list
		toMove.getContainer().removeItem(toMove);

		// get the destination list from product container map
		SortedSet<Item> destinationList = getProductContainerMap()
				.get(destination);
		if (destinationList == null) {
			destinationList = new TreeSet<Item>();
		}
		// add item to destination list / product container map
		destinationList.add(toMove);
		getProductContainerMap().put(destination,
				destinationList);

		// add item to list of products in product container
		destination.addItem(toMove);

		// set the product container of the item to the destination container
		toMove.setProductContainer(destination);
	}

	// /**
	// * @pre true
	// * @post true
	// * @param toMove
	// * The item that needs to be moved
	// * @param destination
	// * The destination ProductContainer
	// * @return true if you can move the object, false otherwise
	// */
	// public boolean canMove(Item toMove, ProductContainer destination) {
	// return false;
	// }

	/**
	 * isValid(String description, ItemBarcode barcode, Date entryDate) Checks
	 * the given input according to the validation rules of Item. description
	 * must be non-empty, barcode must be unique, and entryDate must be between
	 * 1/1/2000 and the date at which isValid is called.
	 * 
	 * @pre true
	 * @post true
	 * @param description
	 *            The description of the Item object.
	 * @param barcode
	 *            The ItemBarcode for the Item to be created.
	 * @param entryDate
	 *            The Date object signifying when the Item was added to its
	 *            container.
	 * @return boolean. True if the input is valid for creating an item. False
	 *         if the input is not valid.
	 */
	@SuppressWarnings("deprecation")
	public boolean isValid(ItemBarcode barcode,
			Date entryDate) {
		if ((barcode != null)
				&& (entryDate != null)) {
			if (!getItemBarcodeMap().containsKey(
					barcode)) {
				if (entryDate.after(new Date(
						2000 - 1900, 1, 1, 0, 0))
						&& entryDate
								.before(new Date(
										System.currentTimeMillis() + 10))
						&& Item.isValid(barcode,
								entryDate)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determines if you can delete a given product. In order to delete a
	 * product it must not contain any items.
	 * 
	 * @pre productBarcode is not null, valid product barcode
	 * @post true
	 * @param productBarcode
	 * @return true if you can delete the product
	 */
	public boolean canDeleteProduct(
			ProductContainer pc,
			ProductBarcode productBarcode) {
		assert ProductBarcode
				.isValid(productBarcode
						.toString());
		// return getProductBarcodeMap().get(productBarcode) == null
		// || getProductBarcodeMap().get(productBarcode).size() == 0;
		Set<Item> items = getItemsByContainerAndProduct(
				pc, productBarcode);
		if ((items == null)
				|| (items.size() == 0)) {
			return true;
		}

		return false;
	}

	/**
	 * @pre true
	 * @post Generates a unique, UPC-A valid, 12 digit (starts with #4 for
	 *       local) item barcode.
	 * @return item barcode
	 */
	public ItemBarcode generateItemBarcode() {
		int itemBarcodeValue = getItemBarcodeMap()
				.keySet().size();
		StringBuilder sb = new StringBuilder();
		sb.append("4");
		for (int c = 0; c < (12 - 2 - Integer
				.toString(itemBarcodeValue)
				.length()); c++) {
			sb.append("0");
		}
		sb.append(itemBarcodeValue);
		int oddValues = 0;
		int evenValues = 0;
		for (int c = 0; c < sb.length(); c++) {
			if ((c % 2) == 0) {
				oddValues += Integer
						.parseInt(Character.toString(sb
								.charAt(c)));
			} else {
				evenValues += Integer
						.parseInt(Character.toString(sb
								.charAt(c)));
			}
		}
		int checkSum = oddValues * 3;
		checkSum += evenValues;
		checkSum = checkSum % 10;
		checkSum = 10 - checkSum;
		// possibly wrong
		if (checkSum == 10) {
			checkSum = 0;
		}
		// end
		sb.append(checkSum);
		return new ItemBarcode(sb.toString());
	}

	/**
	 * itemExists(ItemBarcode barcode) checks the ItemManager's set of Items to
	 * see if an item with barcode barcode exists
	 * 
	 * @pre barcode is non null valid barcode
	 * @post true
	 * @param barcode
	 *            The ItemBarcode to check.
	 * @return boolean. True if an Item with barcode exists. False if no Item
	 *         with barcode exists.
	 */
	public boolean itemExists(ItemBarcode barcode) {
		// assert ItemBarcode.isValid(barcode);
		if (getItemBarcodeMap().containsKey(
				barcode)
				&& (getItemBarcodeMap().get(
						barcode).getContainer() != ProductContainer.CONSUMED_ITEMS)) {
			return true;
		}
		return false;
	}

	/**
	 * This class must remain static in order to remain Serializable. (the only
	 * other option if we want to remove static is to make ItemManager
	 * Serializable) Used for saving ItemManager data
	 * 
	 * @author Cam
	 */
	private static class DataObject implements
			Serializable {
		private HashMap<ItemBarcode, Item> itemBarcodeMap;
		private HashMap<ProductBarcode, SortedSet<Item>> productBarcodeMap;
		private HashMap<ProductContainer, SortedSet<Item>> productContainerMap;
		// ArrayList<ProductContainer> keyList;
		private HashMap<ProductContainer, HashMap<ProductBarcode, SortedSet<Item>>> bigMap;

		// private ArrayList<HashMap<ProductBarcode, SortedSet<Item>>>
		// valueList;

		private DataObject(
				HashMap<ItemBarcode, Item> itemBarcodeMap,
				HashMap<ProductBarcode, SortedSet<Item>> productBarcodeMap2,
				HashMap<ProductContainer, SortedSet<Item>> productContainerMap2,
				HashMap<ProductContainer, HashMap<ProductBarcode, SortedSet<Item>>> bigMap) {
			this.itemBarcodeMap = itemBarcodeMap;
			this.productBarcodeMap = productBarcodeMap2;
			this.productContainerMap = productContainerMap2;
			this.bigMap = bigMap;
			// HashMapWriter hmw = new HashMapWriter(bigMap);
			// this.keyList = hmw.getKeyList();
			// this.valueList = hmw.getValueList();

		}
	}

	/**
	 * Writes the data object to the object output stream
	 * 
	 * @param objectOut
	 *            objectOut object that you write your object data to.
	 * @pre object out is not null
	 * @post true
	 */
	@Override
	public void writeObjects(
			ObjectOutputStream objectOut) {
		assert objectOut != null;
		try {
			DataObject data = new DataObject(
					getItemBarcodeMap(),
					getProductBarcodeMap(),
					getProductContainerMap(),
					this.map);

			File mapFile = new File(
					"savedMap.txt");
			if (!mapFile.exists()) {
				mapFile.createNewFile();
			}
			PrintWriter fout = new PrintWriter(
					mapFile);
			fout.write(this.map.toString());
			fout.close();

			objectOut.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the object from the ObjectInputStream and initializes it properly.
	 * 
	 * @pre objectIn is not null
	 * @post object is read in, values placed in item manager
	 * @return
	 */
	@Override
	public void readObjects(
			ObjectInputStream objectIn) {
		assert objectIn != null;
		try {
			DataObject data = (DataObject) objectIn
					.readObject();
			setItemBarcodeMap(data.itemBarcodeMap);
			setProductBarcodeMap(data.productBarcodeMap);
			setProductContainerMap(data.productContainerMap);
			// HashMapWriter hmw = new HashMapWriter(data.keyList,
			// data.valueList);

			// hmw.loadMap(this.map);
			// System.out.println("bigmap: " + data.bigMap);
			this.map = data.bigMap;

			File mapFile = new File(
					"loadedMap.txt");
			if (!mapFile.exists()) {
				mapFile.createNewFile();
			}
			PrintWriter fout = new PrintWriter(
					mapFile);
			fout.write(this.map.toString());
			fout.close();
			// System.out.println("bigmap: " + this.map);

		} catch (Exception e) { // ClassNotFoundException | IOException
			// e.printStackTrace();
			System.out
					.println("Incompatible data files.");
		}
	}

	/**
	 * Specifies the file to read/write the data for this manager to.
	 * 
	 * @pre true
	 * @post true
	 * @return the name of the file to write the object data to
	 */
	@Override
	public String getDataFileName() {
		return "item";
	}

	/**
	 * Used for testing purposes to see which items are in the list of known
	 * items
	 * 
	 * @return
	 */
	public String printTest() {
		Set<ItemBarcode> itemBarcodes = getItemBarcodeMap()
				.keySet();
		Iterator<ItemBarcode> iter = itemBarcodes
				.iterator();
		StringBuilder output = new StringBuilder();
		while (iter.hasNext()) {
			output.append(iter.next());
			output.append(", ");
		}
		return output.toString();
	}

	/**
	 * Test method
	 */
	public HashMap<ProductContainer, SortedSet<Item>> getContainerMap() {
		return getProductContainerMap();
	}

	/**
	 * test method
	 * 
	 * @return
	 */
	public int getNumberItems() {
		return getItemBarcodeMap().keySet()
				.size();
	}

	/**
	 * test method
	 */
	public void clear() {
		getItemBarcodeMap().clear();
		getProductContainerMap().clear();
		getProductBarcodeMap().clear();
		this.map.clear();
	}

	public Set<Item> getItemsByProduct(
			Product product) {
		SortedSet<Item> items = this.productBarcodeMap
				.get(product.getBarcode());
		return items;
	}

	public HashMap<ProductContainer, SortedSet<Item>> getProductContainerMap() {
		return this.productContainerMap;
	}

	public void setProductContainerMap(
			HashMap<ProductContainer, SortedSet<Item>> productContainerMap) {
		this.productContainerMap = productContainerMap;
	}

	public HashMap<ItemBarcode, Item> getItemBarcodeMap() {
		return this.itemBarcodeMap;
	}

	public void setItemBarcodeMap(
			HashMap<ItemBarcode, Item> itemBarcodeMap) {
		this.itemBarcodeMap = itemBarcodeMap;
	}

	public HashMap<ProductBarcode, SortedSet<Item>> getProductBarcodeMap() {
		return this.productBarcodeMap;
	}

	public void setProductBarcodeMap(
			HashMap<ProductBarcode, SortedSet<Item>> productBarcodeMap) {
		this.productBarcodeMap = productBarcodeMap;
	}

	public Set<Item> getItemsByContainerAndProduct(
			ProductContainer pc,
			ProductBarcode productBarcode) {
		HashMap<ProductBarcode, SortedSet<Item>> pMap = this.map
				.get(pc);
		if ((pMap == null)
				|| (pMap.get(productBarcode) == null)) {
			return new HashSet<Item>();
		}
		return pMap.get(productBarcode);
	}

	public Item getItemByBarcode(
			ItemBarcode itemBarcode) {
		return this.itemBarcodeMap
				.get(itemBarcode);
	}

	public HashMap<ProductBarcode, Integer> getProductCountMap() {
		HashMap<ProductBarcode, Integer> countMap = new HashMap<ProductBarcode, Integer>();
		Iterator<ProductBarcode> iter = this.productBarcodeMap
				.keySet().iterator();
		while (iter.hasNext()) {
			// Product curProduct =
			// ProductManager.getInstance().getProductByBarcode(iter.next());
			ProductBarcode curBarcode = iter
					.next();
			Set<Item> curItemSet = this.productBarcodeMap
					.get(curBarcode);
			int count = 0;
			if (curItemSet != null) {
				for (Item i : curItemSet) {
					if (i.getContainer() != ProductContainer.CONSUMED_ITEMS) {
						count++;
					}
				}
				countMap.put(curBarcode, count);
			} else {
				countMap.put(curBarcode, 0);
			}
		}
		return countMap;
	}

	public void setChangedOverride() {
		setChanged();
	}

	public void updateContainerChange(
			ProductContainer newContainer,
			String oldName) {
		// productContainerMap
		// map
		System.out.println("updating stuff");
		Set<ProductContainer> pcKeySet = this.productContainerMap
				.keySet();
		Iterator<ProductContainer> iter = pcKeySet
				.iterator();
		while (iter.hasNext()) {
			ProductContainer cur = iter.next();
			if ((cur != null)
					&& cur.getName().equals(
							oldName)) {
				SortedSet<Item> items = this.productContainerMap
						.remove(cur);
				this.productContainerMap.put(
						newContainer, items);
			}
		}

		this.map.keySet();
		Iterator<ProductContainer> iter2 = pcKeySet
				.iterator();
		while (iter2.hasNext()) {
			ProductContainer cur = iter2.next();
			if ((cur != null)
					&& cur.getName().equals(
							oldName)) {
				// SortedSet<Item> items = productContainerMap.remove(cur);
				HashMap<ProductBarcode, SortedSet<Item>> curMap = this.map
						.remove(cur);
				this.map.put(newContainer, curMap);
			}
		}
		setChanged();
		this.notifyObservers(new ItemNotifier());
	}
}
