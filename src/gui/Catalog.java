package gui;

import javafx.beans.property.SimpleStringProperty;

public class Catalog {
	private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty id = new SimpleStringProperty(); 
	public Catalog(String id, String name) {
		this.id.set(id);
		this.name = new SimpleStringProperty(name);
	}
	Catalog() {
		//do nothing
	}
	public String getName() {
		return name.get();
	}
	public void setName(String n) {
		name.set(n);
	}
	
	public String getId() {
		return id.get();
	}
	public void setId(String id) {
		this.id.set(id);
	}
	
	public boolean equals(Object ob) {
		if(ob == null) return false;
		if(this == ob) return true;
		if(getClass() != ob.getClass()) return false;
		Catalog c = (Catalog)ob;
		return name.get().equals(c.name.get());
	}
	public int hashCode() {
		int result = 17;
		result += 31 * result + name.get().hashCode();
		return result;
	}
}
