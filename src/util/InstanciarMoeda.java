package util;
import model.Dolar;
import model.Euro;
import model.Moeda;
import model.Real;

public class InstanciarMoeda {
	
		public Moeda instanciar (int idTipoMoeda, double valor) {
			
			return switch (idTipoMoeda) {
	        case 1 -> new Real(valor);
	        case 2 -> new Dolar(valor);
	        case 3 -> new Euro(valor);
	        default -> throw new IllegalArgumentException("Unexpected value: " + idTipoMoeda);
	    };
	}
}
