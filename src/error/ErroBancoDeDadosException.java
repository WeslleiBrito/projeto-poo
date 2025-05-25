package error;

import java.sql.SQLException;

public class ErroBancoDeDadosException extends SQLException {
    
    private static final long serialVersionUID = 1L;

    public ErroBancoDeDadosException(String mensagem) {
        super(mensagem);
    }

    public ErroBancoDeDadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
