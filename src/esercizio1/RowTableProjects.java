package esercizio1;

import javafx.beans.property.*;

public class RowTableProjects {

	private final SimpleIntegerProperty id_project;
	private final SimpleStringProperty nome;
	private SimpleStringProperty progress;
	private final SimpleIntegerProperty budget;
	private SimpleIntegerProperty stake;
	private final SimpleStringProperty azienda;
	
	public RowTableProjects(int ID_Project, String name_project, String progress, int total_budget, int stake, String name_owner) {
		
		this.id_project = new SimpleIntegerProperty(ID_Project);
		this.nome = new SimpleStringProperty(name_project);
		this.progress = new SimpleStringProperty(progress);
		this.budget = new SimpleIntegerProperty(total_budget);
		this.stake = new SimpleIntegerProperty(stake);
		this.azienda = new SimpleStringProperty(name_owner);
		
		
	}
	
	public Integer getId_project() {return id_project.get();}
	public String getNome() {return nome.get();}
	public String getProgress() {return progress.get();}
	public Integer getBudget() {return budget.get();}
	public Integer getStake() {return stake.get();}
	public String getAzienda() {return azienda.get();}
	
}
