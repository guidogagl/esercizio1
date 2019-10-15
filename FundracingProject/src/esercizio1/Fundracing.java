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
	
	protected TextField tf_companyName = new TextField();
	protected Button login = new Button("Login");
	protected Label table_title = new Label("NetworkProjects");
	protected Label l_password=new Label("Password");
	protected Label l_agencyName=new Label("Agenzia");
	protected TextArea description = new TextArea();
	protected TextField name_project = new TextField("Project Name");
	protected TextField total_budget = new TextField("Total Budget");
	protected TextField stake = new TextField("Stake");
	protected TextField tf_password=new TextField();
	protected Button update = new Button("Update");
	protected Button insert = new Button("Insert");
	protected Button delete = new Button("Delete");
	private Boolean logged = false;
	protected String agencyName = "";
	private TableProjects table = new TableProjects();
	private int selectedProjectId = 0;
	private int selectedStake=0;
	private int selectedTotalBudget=0;
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
		
		login.setOnAction((ActionEvent ev1)->{
			
			String urlLogo = "";
			
			agencyName = tf_companyName.getText();
			String password=tf_password.getText();
			Vector<String> result = deposito.getAgency(agencyName,password);
			
			//Se il nome dell'azienda è presente nel db e la password è corretta
			if(!result.isEmpty()) {
				table.updateProjects(deposito.getProjects(agencyName));
				logged = true;
				insert.setDisable(false);
				delete.setDisable(false);
				description.setEditable(true);
				name_project.setEditable(true);
				total_budget.setEditable(true);
				stake.setEditable(true);
				update.setDisable(false);
				name_agency.setText(result.get(0)); 
				address_agency.setText(result.get(3));
				site_agency.setText(result.get(4));
				urlLogo = result.get(1);
				image = new Image(urlLogo);
				iv1.setImage(image);
				
			} //Se il nome dell'azienda non è presente nel db
			else {
				JOptionPane.showMessageDialog(null, "Il nome dell'azienda è errato oppure la password è scorretta!");
			}
			
        });
		
		Interface interfaccia = new Interface(login, tf_companyName, table_title, table, 
				description, name_project, total_budget, insert, delete, iv1, stake, update,
				name_agency, address_agency, site_agency,tf_password,l_agencyName,l_password);
		
		
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
			
			update.setOnAction((ActionEvent ev1)->{
				int stakeInsered=Integer.parseInt(stake.getText());
				int totalStakes=deposito.getSommaStakes(selectedProjectId);
				//se ho già raggiunto l'obiettivo
				if(totalStakes>=selectedTotalBudget) {
					JOptionPane.showMessageDialog(null, "Ti ringraziamo per la tua generosità, ma abbiamo già raggiunto l'obiettivo prefissato!");	
				} //se non ho raggiunto l'obiettivo e voglio aggiungere soldi
				else  {
					int newStake=0;
					//se voglio mettere più soldi di quelli necessari,metto solo quelli che mi servono per raggiungere il budget prefisso
					if(stakeInsered>selectedTotalBudget)
						newStake=(selectedStake+(selectedTotalBudget-totalStakes)); //quanto ho messo più quanto manca per il max
					else //altrimenti il nuovo stake sarà semplicemente quello inserito
						newStake=stakeInsered;
					deposito.updateStake(newStake,agencyName,selectedProjectId);
					table.updateProjects(deposito.getProjects(agencyName));
					stake.setText("");
				} //policy:non posso diminuire il finanziamento fatto
				/*else {
					JOptionPane.showMessageDialog(null, "Non puoi diminuire il finanziamento che hai messo!");	
				}*/
			});
		
		
		
		
		Group root = new Group(tf_companyName,tf_password, login, table_title, table, description,
				name_project, total_budget, insert, delete, iv1, stake, update, 
				name_agency, address_agency, site_agency,l_agencyName,l_password);
		
		
	
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
                selectedTotalBudget=res.getBudget();
                selectedStake=res.getStake();
                description.setText(deposito.getDescriptionProject(selectedProjectId));
                stake.setText(Integer.toString(res.getStake()));
            }  
         });  
            return row;  
        }  
        }); 
	}
	
}




