package esercizio1;

import java.awt.event.KeyEvent;

import javafx.scene.image.*;

import javafx.event.*;
import javafx.scene.control.*;

public class Interface {

	Interface(Button submit, TextField tf_companyName, Label table_title, TableProjects table, 
			TextArea description, TextField name_project, TextField total_budget, Button insert, Button delete,
			ImageView iv1, TextField stake, Button update, Label name_agency, Label address_agency, Label site_agency){
		
		//Abbasso tutte le y di 20
		submit.setLayoutX(545);
		submit.setLayoutY(75);
		submit.setMinSize(70, 30);
		submit.setStyle("-fx-font-weight: bold;");
		
		name_agency.setLayoutX(350);
		name_agency.setLayoutY(40);
		name_agency.setStyle("-fx-font-weight: bold;  -fx-font-size: 18px;");
		
		address_agency.setLayoutX(300);
		address_agency.setLayoutY(70);
		address_agency.setStyle("-fx-font-weight: bold;");
		
		site_agency.setLayoutX(350);
		site_agency.setLayoutY(100);
		site_agency.setStyle("-fx-font-weight: bold;");
		
		tf_companyName.setLayoutX(500);
		tf_companyName.setLayoutY(35);
		
		table_title.setLayoutX(300);
		table_title.setLayoutY(160);
		table_title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
		
		table.setLayoutX(130);
		table.setLayoutY(200);
		table.setMaxHeight(140); 
		
		description.setLayoutX(130);
		description.setLayoutY(400);
		description.setMaxWidth(480);
		description.setMaxHeight(90);
		description.setEditable(false);
		description.setWrapText(true);
		
		//description.setOnKeyPressed(new EventHandler<KeyEvent>(){
			//public void handle(KeyEvent event) {
				
			//}
		//});
		description.setTextFormatter(new TextFormatter<String>(change -> 
        	change.getControlNewText().length() <= 500 ? change : null));

		
		
		
		name_project.setLayoutX(130);
		name_project.setLayoutY(510);
		name_project.setMinHeight(40);
		name_project.setMinWidth(200);
		name_project.setEditable(false);
		
		total_budget.setLayoutX(410);
		total_budget.setLayoutY(510);
		total_budget.setMinHeight(40);
		total_budget.setMinWidth(200);
		total_budget.setEditable(false);
		
		insert.setLayoutX(270);
		insert.setLayoutY(580);
		insert.setMinSize(70, 30);
		insert.setStyle("-fx-font-weight: bold;");
		insert.setDisable(true);
		
		delete.setLayoutX(400);
		delete.setLayoutY(580);
		delete.setMinSize(70, 30);
		delete.setStyle("-fx-font-weight: bold;");
		delete.setDisable(true);
		
		iv1.setFitHeight(100);
	    iv1.setFitWidth(100);
	    iv1.setLayoutX(50);
	    iv1.setLayoutY(20);
	    
	    stake.setLayoutX(130);
	    stake.setLayoutY(360);
	    stake.setEditable(false);
	    
	    update.setLayoutX(300);
	    update.setLayoutY(360);
	    update.setDisable(true);
	}
}
