package database;

import java.sql.*;



public class Database {
    private static final String URL = "jdbc:sqlite:cofrinho.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    
    private static boolean tabelaEstaVazia(String nomeTabela) {
        String sql = "SELECT 1 FROM " + nomeTabela + " LIMIT 1";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return !rs.next();

        } catch (SQLException e) {
            System.out.println("Erro ao verificar se a tabela está vazia: " + e.getMessage());
            return true; 
        }
    }
    
    
    public static void inicializar() {
    	
    	String sqlTipoMoeda = """
        		CREATE TABLE IF NOT EXISTS tipo_moeda (
        			id INTEGER PRIMARY KEY AUTOINCREMENT,
        			nome TEXT NOT NULL UNIQUE,
        			cambio REAL NOT NULL
        		);
		""";
    	
    	String sqlInsertTipoMoeda = """
    			INSERT INTO tipo_moeda (nome, cambio) VALUES 
				('Real', 1),
				('Dólar', 5),
				('Euro', 6);
		""";
    	
    	String sqlTipoTransacao = """
    			CREATE TABLE IF NOT EXISTS tipo_transacao (
				    id INTEGER PRIMARY KEY AUTOINCREMENT,
				    nome TEXT NOT NULL UNIQUE
    			);
		""";
    	
    	
    	String sqlInsertTipoTransacao = """
    			INSERT INTO tipo_transacao (nome) VALUES 
				('Entrada'),
				('Retirada');
		""";
    	
        String sqlMoeda = """
            CREATE TABLE IF NOT EXISTS moeda (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo_moeda_id INTEGER NOT NULL,
                valor REAL NOT NULL,
                FOREIGN KEY (tipo_moeda_id) REFERENCES tipo_moeda(id) ON DELETE CASCADE ON UPDATE CASCADE
            );
        """;

        String sqlHistorico = """
        	    CREATE TABLE IF NOT EXISTS historico_transacao (
        	        id INTEGER PRIMARY KEY AUTOINCREMENT,
        	        valor REAL NOT NULL,
        	        saldo_anterior REAL NOT NULL,
        	        saldo_pos REAL NOT NULL,
        	        data TEXT DEFAULT CURRENT_TIMESTAMP,
        	        tipo_transacao_id INTEGER NOT NULL,
        	        tipo_moeda_id INTEGER NOT NULL,
        	        FOREIGN KEY (tipo_transacao_id) REFERENCES tipo_transacao(id) ON DELETE CASCADE ON UPDATE CASCADE,
        	        FOREIGN KEY (tipo_moeda_id) REFERENCES tipo_moeda(id) ON DELETE CASCADE ON UPDATE CASCADE
        	    );
        	""";

        
        
        
        
        try (Connection conn = conectar();
            Statement stmt = conn.createStatement()) {
        		stmt.execute("PRAGMA foreign_keys = ON");
	        	stmt.execute(sqlTipoMoeda);
	        	stmt.execute(sqlTipoTransacao);
	        	stmt.execute(sqlMoeda);
	            stmt.execute(sqlHistorico);
	            
	            if(tabelaEstaVazia("tipo_moeda")) {
	            	
	            	stmt.execute(sqlInsertTipoMoeda);
	            }
	            
	            if(tabelaEstaVazia("tipo_transacao")) {
	            	
	            	stmt.execute(sqlInsertTipoTransacao);
	            }

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
    
    
}
