package tcpeditor;

import tcpeditor.Model.Modus;

public class Starter {

	public static void main(String[] args) {
		Model model = new Model();
		model.setUrl("localhost");
		model.setPort(1234);
		model.setModus(Modus.SERVER);
		View view = new View(model);
		Controller controller = new Controller(model, view);
		model.addListener(view);
		view.addActionListener(controller);
		view.show();
	}

}
