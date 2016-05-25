package gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
public class TableUtil {
	//returns a non-editable table column whose
	//column header is specified by colHeader
	//and whose field is specified by field
	//and whose min width is specified by minWidth
	public static <T> TableColumn<T, String> makeTableColumn(T underlyingClass, String colHeader, String field, int minWidth) {
		TableColumn<T, String> tableColumn 
		  = new TableColumn<>(colHeader);
		tableColumn.setMinWidth(minWidth);
		tableColumn.setCellValueFactory(
          new PropertyValueFactory<T, String>(field));
		tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		tableColumn.setEditable(false);
		return tableColumn;
	}
	
	public static <T> TableColumn<T, String> makeEditableTableColumn(TableView<T> table, T underlyingClass, String colHeader, String field, int minWidth) {
		Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory 
		        = new Callback<TableColumn<T,String>, TableCell<T,String>>() {
			@Override
	        public TableCell<T, String> call(TableColumn<T, String> p) {
	            return new EditingCell(field, underlyingClass, table);
	        }
	    };
		TableColumn<T, String> tableColumn 
		  = new TableColumn<>(colHeader);
		tableColumn.setMinWidth(minWidth);
		tableColumn.setCellValueFactory(
             new PropertyValueFactory<T, String>(field));
		tableColumn.setCellFactory(cellFactory);
		tableColumn.setEditable(true);
		/*
		tableColumn.setOnEditCommit(t -> {
			T instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			Class<T> cl = (Class<T>)underlyingClass.getClass();
			
			setValue(field, cl, t.getNewValue(), instance);
			System.out.println("value set");
			//TableUtil.selectByRow(table);	
		}); */	
		return tableColumn;
	}
	
	//not in use; shows how to make editable column and update another column
	public static <T> TableColumn<T, String> makeEditableTableColumnSideEffect(
			TableView<T> table, T underlyingClass, String colHeader, String field, int minWidth,
			   final BiFunction<String, String, String> f, String field2, String field3) {
		Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory 
		        = new Callback<TableColumn<T,String>, TableCell<T,String>>() {
			@Override
	        public TableCell<T, String> call(TableColumn<T, String> p) {
	            return new EditingCell(field, underlyingClass, table);
	        }
	    };
		TableColumn<T, String> tableColumn 
		  = new TableColumn<>(colHeader);
		tableColumn.setMinWidth(minWidth);
		tableColumn.setCellValueFactory(
             new PropertyValueFactory<T, String>(field));
		tableColumn.setCellFactory(cellFactory);
		tableColumn.setEditable(true);
		
		tableColumn.setOnEditCommit(t -> {
			T instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
			Class<T> cl = (Class<T>)underlyingClass.getClass();
			
			//set value that was just changed by user
			setValue(field, cl, t.getNewValue(), instance);
			
			//compute side effect			
			String fieldVal2 = getValue(field2, cl, instance);		
			setValue(field3, cl, f.apply(t.getNewValue(), fieldVal2), instance);
					
			TableUtil.selectByRow(table);
			TableUtil.refreshTable(table);			
		}); 
		
		
		return tableColumn;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> String getValue(String field, Class<T> cl, T instance) {
		String methodName = "get" + field.substring(0,1).toUpperCase() + field.substring(1);
		System.out.println(methodName);
		String retVal = null;
		try {
			
			Method method = cl.getDeclaredMethod(methodName);
			retVal = (String)method.invoke(instance);				
					
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return retVal;
		
	}
	@SuppressWarnings("unchecked")
	private static <T> void setValue(String field, Class<T> cl, String newValue, T instance) {
		String methodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);
		System.out.println(methodName);
		try {
			
			Method method = cl.getDeclaredMethod(methodName, String.class);
			method.invoke(instance, newValue);				
					
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	public static <T> void refreshTable(TableView<T> table) {
		ObservableList<T> items = table.getItems();	
		List<T> copy = new ArrayList<>();
		//using FXCollections.copy keeps original list tied to new list
		for(T c : items) {
			copy.add(c);
		}	
		items.removeAll(items);
		ObservableList<T> newItems 
		  = FXCollections.observableList(copy);	
		table.setItems(newItems);
	}
	
	public static <T> TableView.TableViewSelectionModel<T> selectByCell(TableView<T> table) {
		TableView.TableViewSelectionModel<T> selModel
			= table.getSelectionModel();
		selModel.setCellSelectionEnabled(true);
		return selModel;
	}
	public static <T> TableView.TableViewSelectionModel<T> selectByRow(TableView<T> table) {
		TableView.TableViewSelectionModel<T> selModel
			= table.getSelectionModel();
		selModel.setCellSelectionEnabled(false);
		return selModel;
	}
}
