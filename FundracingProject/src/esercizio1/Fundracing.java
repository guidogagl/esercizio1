package esercizio1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
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
	private TableProjects table = new TableProjects();
	private int selectedProjectId = 0;
	private Label name_agency = new Label("");
	private Label address_agency = new Label("");
	private Label site_agency = new Label("");
	
	
	public void start(Stage stage) {
		
		DepositoDati deposito = new DepositoDati();
		
		Image image = new Image("file:///C:\\Users\\Utente\\eclipse-workspace\\FundracingProject\\src\\fundracing_package\\pippo.jpg");
		ImageView iv1 = new ImageView();
        iv1.setImage(image);
		
		
		table.updateProjects(deposito.getProjectsWithoutStake());
		selectTableRow();
		
		Interface interfaccia = new Interface(submit, tf_companyName, table_title, table, 
				description, name_project, total_budget, insert, delete, iv1, stake, update,
				name_agency, address_agency, site_agency);
		
		
		
		submit.setOnAction((ActionEvent ev1)->{
			
			agencyName = tf_companyName.getText();
			
			if(!deposito.getAgency(agencyName).isEmpty()) {
				table.updateProjects(deposito.getProjects(agencyName));
				logged = true;
				insert.setDisable(false);
				delete.setDisable(false);
				description.setEditable(true);
				name_project.setEditable(true);
				total_budget.setEditable(true);
				stake.setEditable(true);
				update.setDisable(false);
				Vector<String> result = deposito.getAgency(agencyName);
				name_agency.setText(result.get(0)); 
				address_agency.setText(result.get(3));
				site_agency.setText(result.get(4));
			}
			
        });
		
		
		insert.setOnAction((ActionEvent ev2)->{
					
					String desc = description.getText(); 
					String name = name_project.getText();
					String budget = total_budget.getText();
					
					if(!desc.equals("") && !name.equals("") && !budget.equals("")) {
						
						Vector<String> vector = new Vector<String>(4);
			
						vector.add(name);
						vector.add(budget);
						vector.add(desc);
						vector.add(agencyName);
						
						deposito.insertProject(vector);
						
						description.clear();
						name_project.clear();
						total_budget.clear();
						
						vector.clear();
						
						table.updateProjects(deposito.getProjects(agencyName));
					}
		        });
		
			delete.setOnAction((ActionEvent ev1)->{
						
				deposito.deleteProject(selectedProjectId, agencyName);
				table.updateProjects(deposito.getProjects(agencyName));
			});
		
		
		
		Group root = new Group(tf_companyName, submit, table_title, table, description,
				name_project, total_budget, insert, delete, iv1, stake, update, name_agency, address_agency, site_agency);
		
		
		
		Scene scene = new Scene(root, 750, 650);
        stage.setTitle("My Fundracing Project");
        stage.setScene(scene);
        stage.show();
        
        
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	private void selectTableRow(){
        table.setRowFactory(new Callback<TableView<RowTableProjects>, TableRow<RowTableProjects>>() {  
        public TableRow<RowTableProjects> call(TableView<RowTableProjects> tableView2) {  
            final TableRow<RowTableProjects> row = new TableRow<>();  
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
                
            public void handle(MouseEvent event) {  
                final int index = row.getIndex(); 
                System.out.println("row selected: "+index);
               RowTableProjects res = table.getItems().get(index);
               selectedProjectId = res.getId_project();
               //System.out.println("ID: " + res.getId_project());
                /*if(!tabellaMovimenti.getSelectionModel().isSelected(index)){
                    String[] dataOra = RitornaDataOraCorrenti();
                    Evento e = new Evento(dataOra[0], dataOra[1], "SELEZIONE RIGA TABELLA");
                    EventoDiNavigazioneGUI.serializzaXML(e);
                }
                if (index >= 0 && index < tabellaMovimenti.getItems().size() && tabellaMovimenti.getSelectionModel().isSelected(index)  ) {
                    tabellaMovimenti.getSelectionModel().clearSelection();
                    event.consume();
                    cancella.setDisable(true);
                }*/ 
            }  
            });  
            return row;  
        }  
        }); 
	}
	
}




