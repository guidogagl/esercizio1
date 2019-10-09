package fundracing_package;

import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

public class TableProjects extends TableView<Project>{
    private ObservableList<Project> projectsList;
    
    public TableProjects() {
    	setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    	
    	TableColumn ID_Project = new TableColumn("ID_Project"); 
    	ID_Project.setCellValueFactory(new PropertyValueFactory<>("ID_Project")); 
    	
    	TableColumn name_project = new TableColumn("name_project"); 
    	name_project.setCellValueFactory(new PropertyValueFactory<>("name_project")); 
    	
    	TableColumn progress = new TableColumn("progress"); 
    	progress.setCellValueFactory(new PropertyValueFactory<>("progress")); 
    	
    	TableColumn total_budget = new TableColumn("total_budget"); 
    	total_budget.setCellValueFactory(new PropertyValueFactory<>("total_budget")); 
    	
    	TableColumn stake = new TableColumn("stake"); 
    	stake.setCellValueFactory(new PropertyValueFactory<>("stake"));
    	
    	TableColumn name_owner = new TableColumn("name_owner"); 
    	name_owner.setCellValueFactory(new PropertyValueFactory<>("name_owner"));
    	
    	
    	projectsList = FXCollections.observableArrayList();
    	setItems(projectsList);
        getColumns().addAll(ID_Project, name_project, progress, total_budget, stake, name_owner); 
        
    }
    
    public void updateProjects(List<Project> projects) {
    	projectsList.clear();
    	projectsList.addAll(projects);
    }
}
