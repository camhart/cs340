package gui.main;

import gui.common.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class ReportsMenu extends JMenu {

	private IMainController _controller;
	private JMenuItem _expiredMenuItem;
	private JMenuItem _supplyMenuItem;
	private JMenuItem _removedMenuItem;
	private JMenuItem _productMenuItem;
	private JMenuItem _noticesMenuItem;

	public ReportsMenu(GUI parent) {
		super("Reports");

		addMenuListener(new MenuListener() {
			@Override
			public void menuCanceled(MenuEvent e) {
				return;
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				return;
			}

			@Override
			public void menuSelected(MenuEvent e) {
				enableMenuItems();
			}
		});

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(
					ActionEvent evt) {
				if (evt.getSource() == ReportsMenu.this._expiredMenuItem) {
					printExpiredReport();
				} else if (evt.getSource() == ReportsMenu.this._supplyMenuItem) {
					printSupplyReport();
				} else if (evt.getSource() == ReportsMenu.this._removedMenuItem) {
					printRemovedReport();
				} else if (evt.getSource() == ReportsMenu.this._productMenuItem) {
					printProductReport();
				} else if (evt.getSource() == ReportsMenu.this._noticesMenuItem) {
					printNoticesReport();
				}
			}
		};

		this._expiredMenuItem = new JMenuItem(
				"Expired Items");
		this._expiredMenuItem.setFont(View
				.createFont(this._expiredMenuItem
						.getFont(),
						View.MenuFontSize));
		this._expiredMenuItem
				.addActionListener(actionListener);
		add(this._expiredMenuItem);

		this._removedMenuItem = new JMenuItem(
				"Removed Items");
		this._removedMenuItem.setFont(View
				.createFont(this._removedMenuItem
						.getFont(),
						View.MenuFontSize));
		this._removedMenuItem
				.addActionListener(actionListener);
		add(this._removedMenuItem);

		this._supplyMenuItem = new JMenuItem(
				"N-Month Supply");
		this._supplyMenuItem.setFont(View
				.createFont(this._supplyMenuItem
						.getFont(),
						View.MenuFontSize));
		this._supplyMenuItem
				.addActionListener(actionListener);
		add(this._supplyMenuItem);

		this._productMenuItem = new JMenuItem(
				"Product Statistics");
		this._productMenuItem.setFont(View
				.createFont(this._productMenuItem
						.getFont(),
						View.MenuFontSize));
		this._productMenuItem
				.addActionListener(actionListener);
		add(this._productMenuItem);

		this._noticesMenuItem = new JMenuItem(
				"Notices");
		this._noticesMenuItem.setFont(View
				.createFont(this._noticesMenuItem
						.getFont(),
						View.MenuFontSize));
		this._noticesMenuItem
				.addActionListener(actionListener);
		add(this._noticesMenuItem);
	}

	public void setController(
			IMainController value) {
		this._controller = value;
	}

	//
	//
	//

	private void enableMenuItems() {
		this._expiredMenuItem
				.setEnabled(this._controller
						.canPrintExpiredReport());
		this._supplyMenuItem
				.setEnabled(this._controller
						.canPrintSupplyReport());
		this._removedMenuItem
				.setEnabled(this._controller
						.canPrintRemovedReport());
		this._productMenuItem
				.setEnabled(this._controller
						.canPrintProductReport());
		this._noticesMenuItem
				.setEnabled(this._controller
						.canPrintNoticesReport());
	}

	private void printExpiredReport() {
		this._controller.printExpiredReport();
	}

	private void printSupplyReport() {
		this._controller.printSupplyReport();
	}

	private void printRemovedReport() {
		this._controller.printRemovedReport();
	}

	private void printProductReport() {
		this._controller.printProductReport();
	}

	private void printNoticesReport() {
		this._controller.printNoticesReport();
	}

}
