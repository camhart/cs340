package gui.main;

import gui.common.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class SessionMenu extends JMenu {

	private IMainController _controller;
	private GUI _parent;
	private JMenuItem _exitMenuItem;

	public SessionMenu(GUI parent) {
		super("Session");

		this._parent = parent;

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
				if (evt.getSource() == SessionMenu.this._exitMenuItem) {
					exit();
				}
			}
		};

		this._exitMenuItem = new JMenuItem("Exit");
		this._exitMenuItem.setFont(View
				.createFont(this._exitMenuItem
						.getFont(),
						View.MenuFontSize));
		this._exitMenuItem
				.addActionListener(actionListener);
		add(this._exitMenuItem);
	}

	public void setController(
			IMainController value) {
		this._controller = value;
	}

	//
	//
	//

	private void enableMenuItems() {
		this._exitMenuItem
				.setEnabled(this._controller
						.canExit());
	}

	void exit() {
		this._controller.exit();
		this._parent.dispose();
		System.exit(0);
	}

}
