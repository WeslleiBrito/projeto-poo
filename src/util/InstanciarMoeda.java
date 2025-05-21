package util;
import model.Dolar;
import model.Euro;
import model.Moeda;
import model.Real;

public class InstanciarMoeda {
	
		public Moeda instanciar (int idTipoMoeda) {
			
			return switch (idTipoMoeda) {
	        case 1 -> new Real();
	        case 2 -> new Dolar();
	        case 3 -> new Euro();
	        default -> throw new IllegalArgumentException("Unexpected value: " + idTipoMoeda);
	    };
	}
}
