package esercizio1;

import javafx.beans.property.*;

public class Project {

	private final SimpleIntegerProperty ID_Project;
	private final SimpleStringProperty name_project;
	private SimpleStringProperty progress;
	private final SimpleIntegerProperty total_budget;
	private SimpleIntegerProperty stake;
	private final SimpleStringProperty name_owner;
	
	public Project(int ID_Project, String name_project, String progress, int total_budget, int stake, String name_owner) {
		
		this.ID_Project = new SimpleIntegerProperty(ID_Project);
		this.name_project = new SimpleStringProperty(name_project);
		this.progress = new SimpleStringProperty(progress);
		this.total_budget = new SimpleIntegerProperty(total_budget);
		this.stake = new SimpleIntegerProperty(stake);
		this.name_owner = new SimpleStringProperty(name_owner);
		
		
	}
	
	public Integer getIDProject() {return ID_Project.get();}
	public String getNameProject() {return name_project.get();}
	public String getProgress() {return progress.get();}
	public Integer getTotalBudget() {return total_budget.get();}
	public Integer getStake() {return stake.get();}
	public String getNameOwner() {return name_owner.get();}
	
}
