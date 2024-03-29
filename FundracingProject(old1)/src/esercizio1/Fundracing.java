package esercizio1;

import java.awt.BorderLayout;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	
	protected TextField tf_companyName = new TextField("Company Name");
	protected TextField tf_password = new TextField("Password");
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
	private String selectedProjectAgency="";
	private int selectedTotalBudget = 0;
	private int selectedStake = 0;
	private Label name_agency = new Label("");
	private Label address_agency = new Label("");
	private Label site_agency = new Label("");
	private DepositoDati deposito = new DepositoDati();
	private JLabel label;
	private Image image;
	private ImageView iv1 = new ImageView();
	
	
	public void start(Stage stage) {
		
		
		table.updateProjects(deposito.getProjectsWithoutStake());
		selectTableRow();
		
		
		submit.setOnAction((ActionEvent ev1)->{
			
			String urlLogo = "";
			
			agencyName = tf_companyName.getText();
			
			//Se il nome dell'azienda � presente nel db
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
				
				urlLogo = deposito.getAgency(agencyName).get(1);
				
				
				image = new Image(urlLogo);
				//iv1 = new ImageView();
		        iv1.setImage(image);
				
			} //Se il nome dell'azienda non � presente nel db
			else {
				JOptionPane.showMessageDialog(null, "Il nome dell'azienda � errato!");
			}
			
        });
		
		
		/*update.setOnAction((ActionEvent ev1)->{
			
			if(selectedStake >= selectedTotalBudget) {
				JOptionPane.showMessageDialog(null, "Ti ringraziamo per la tua generosit�, ma abbiamo gi� raggiunto l'obbiettivo prefissato!");
			}else if(Integer.parseInt(stake.getText()) > selectedStake) {
				deposito.updateStake(Integer.parseInt(stake.getText()),agencyName,selectedProjectId);
				table.updateProjects(deposito.getProjects(agencyName));
				stake.setText("");
			}else {
				JOptionPane.showMessageDialog(null, "Non puoi diminuire il finanziamento che hai messo!");
			}
		});*/
		
		
		update.setOnAction((ActionEvent ev1)->{
			int stakeInsered=Integer.parseInt(stake.getText());
			//se ho gi� raggiunto l'obiettivo
			if(selectedStake>=selectedTotalBudget) {
				JOptionPane.showMessageDialog(null, "Ti ringraziamo per la tua generosit�, ma abbiamo gi� raggiunto l'obiettivo prefissato!");	
			} //se non ho raggiunto l'obiettivo e voglio aggiungere soldi
			else if(stakeInsered>selectedStake) {
				int difference=0;
				//se voglio mettere pi� soldi di quelli necessari,metto solo quelli che mi servono per raggiungere il budget prefisso
				if(stakeInsered>selectedTotalBudget)
					difference=selectedTotalBudget-selectedStake;
				else //altrimenti metto la differenza tra newStake e oldStake(poich� non aggiorniamo lo stake nel database,ma aggiungiamo nuovo finanziamento
					difference=stakeInsered-selectedStake;
				deposito.updateStake(difference,agencyName,selectedProjectId);
				table.updateProjects(deposito.getProjects(agencyName));
				stake.setText("");
			} //policy:non posso diminuire il finanziamento fatto
			else {
				JOptionPane.showMessageDialog(null, "Non puoi diminuire il finanziamento che hai messo!");	
			}
		});
	
		
		
		Interface interfaccia = new Interface(submit, tf_companyName, table_title, table, 
				description, name_project, total_budget, insert, delete, iv1, stake, update,
				name_agency, address_agency, site_agency, tf_password);
		
		
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
				
				//Se sono il proprietario
				if(deposito.sonoProprietario(selectedProjectId, agencyName)) {
					deposito.deleteProject(selectedProjectId);
					table.updateProjects(deposito.getProjects(agencyName));
				}//Se non sono il proprietario ma voglio levare il mio stake
				else if(deposito.sonoProprietario(selectedProjectId, agencyName)==false &&
						deposito.myStake(agencyName, selectedProjectId) == true) {
					deposito.deleteMyStakes(selectedProjectId, agencyName);
					table.updateProjects(deposito.getProjects(agencyName));
				}//Se cerco di eliminare il progetto o lo stake di un altro
				else {
					JOptionPane.showMessageDialog(null, "Puoi eliminare solo i tuoi progetti o finanziamenti!");
				}
			});
		
		
		
		Group root = new Group(tf_companyName, submit, table_title, table, description,
				name_project, total_budget, insert, delete, iv1, stake, update, 
				name_agency, address_agency, site_agency, tf_password);
		
		
	
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
                RowTableProjects res = table.getItems().get(index);
                selectedProjectId = res.getId_project();
                selectedProjectAgency=res.getAzienda();
                selectedTotalBudget = res.getBudget();
                description.setText(deposito.getDescriptionProject(selectedProjectId));
            }  
         });  
            return row;  
        }  
        }); 
	}
	
}




