package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GuiUtils {
	public static final String DATE_PATTERN = "MM/dd/yyyy"; 
	public static String formatPrice(double d) {
		return String.format("%.2f", d);
	}

	/** Assumes num1 num2 are String versions of doubles */
	public static String stringDoublesMultiply(String num1, String num2) {
		double d1 = Double.parseDouble(num1);
		double d2 = Double.parseDouble(num2);
		return (new Double(d1*d2)).toString();
	}

	public static StringProperty multiplyStringProps(StringProperty num1, StringProperty num2) {
		String retVal = stringDoublesMultiply(num1.get(), num2.get());
		return new SimpleStringProperty(retVal);
	}

	public static List<String> emptyStrings(int len) {
		List<String> eStrings = new ArrayList<>();
		for(int i = 0; i < len; ++i) {
			eStrings.add("");
		}
		return eStrings;
	}
	public static LocalDate localDateForString(String date) {  //pattern: "MM/dd/yyyy"
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
	}
	
	public static String localDateAsString(LocalDate date) {  //pattern: "MM/dd/yyyy"
		return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
	}
	

}
