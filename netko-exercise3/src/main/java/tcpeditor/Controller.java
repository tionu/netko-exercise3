package tcpeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

	private Model model;
	private View view;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		view.writeToModel();
		if(e.getActionCommand().equals("SEND")) {
			model.sendData();
		} else if(e.getActionCommand().equals("START")) {
			model.startServer();
		}
	}

}
