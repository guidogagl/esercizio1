package esercizio1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.*;
import java.time.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javafx.scene.image.*;

import javafx.application.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.util.*;



public class Fundracing extends Application{
	
	protected TextField tf_companyName = new TextField();
	protected Button submit = new Button("Submit");
	protected Label table_title = new Label("NetworkProjects");
	protected TextArea description = new TextArea();
	protected TextField name_project = new TextField("Project Name");
	protected TextField total_budget = new TextField("Total Budget");
	protected TextField stake = new TextField("Stake");
	protected Button update = new Button("Update");
	protected Button insert = new Button("Insert");
	protected Button delete = new Button("Delete");
	private Boolean logged = false;
	protected String agencyName = "";
	
	
	public void start(Stage stage) {
		
		dbHandler handler = new dbHandler();
		
		Image image = new Image("file:///C:\\Users\\Utente\\eclipse-workspace\\FundracingProject\\src\\fundracing_package\\pippo.jpg");
				
		ImageView iv1 = new ImageView();
        iv1.setImage(image);
		
		
		TableProjects table = new TableProjects();
		
		table.updateProjects(handler.getProject());
		
		Interface interfaccia = new Interface(submit, tf_companyName, table_title, table, 
				description, name_project, total_budget, insert, delete, iv1, stake, update);
		
		
		
		submit.setOnAction((ActionEvent ev1)->{
			
			agencyName = tf_companyName.getText();
			
			if(handler.getAgency(agencyName) != null) {
				
				logged = true;
				insert.setDisable(false);
				delete.setDisable(false);
				description.setEditable(true);
				name_project.setEditable(true);
				total_budget.setEditable(true);
				stake.setEditable(true);
				update.setDisable(false);
			}
			
        });
		
		
		insert.setOnAction((ActionEvent ev2)->{
					
					String desc = description.getText(); 
					String name = name_project.getText();
					String budget = total_budget.getText();
					
					if(!desc.equals("") && !name.equals("") && !budget.equals("")) {
						
						Vector<String> vector = new Vector<String>(5);
			
						vector.add("1");
						vector.add(name);
						vector.add(budget);
						vector.add(desc);
						vector.add(agencyName);
						
						System.out.println("Capacity: " + vector.capacity());
						for(int i = 0; i < vector.capacity(); i++) {
							System.out.println(vector.get(i));
						}
						
						if(handler.insertProject(vector)) {
							System.out.println("Inserimento avvenuto correttamente");
						}
						
						vector.clear();
					}
		        });
		
		
		
		
		
		Group root = new Group(tf_companyName, submit, table_title, table, description,
				name_project, total_budget, insert, delete, iv1, stake, update);
		
		
		
		Scene scene = new Scene(root, 750, 650);
        stage.setTitle("My Fundracing Project");
        stage.setScene(scene);
        stage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}