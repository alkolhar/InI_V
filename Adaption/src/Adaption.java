import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Adaption {

	public static boolean isWeekend(int day) {
		return (day == Calendar.SATURDAY) || (day == Calendar.SUNDAY);
	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance(Locale.US);

		// Wochentag finden
		int day = cal.get(Calendar.DAY_OF_WEEK);
		String className = "";
		if (isWeekend(day)) {
			className = "Weekend";
		} else {
			className = "Week";
		}

		// Klasse laden
		Class<?> c = null;
		try {
			c = Class.forName(className);
			System.out.println(c + " geladen!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Instanz der Klasse erzeugen
		Object t = null;
		try {
			t = c.getDeclaredConstructor().newInstance();
			System.out.println(c + " instanziert!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Methodenname finden
		String methodName = "";
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
		methodName = sdf.format(cal.getTime());
		System.out.println("Methoden name: '" + methodName + "'");

		// Methode finden
		Method m = null;
		try {
			m = c.getMethod(methodName, String.class);
			System.out.println("Methode gefunden: " + m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Methode aufrufen
		String text = "";
		try {
			text = (String) m.invoke(t, "Alex");
			System.out.println(text);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
