package dao;

import database.Database;
import model.Dolar;
import model.Euro;
import model.HistoricoTransacao;
import model.Moeda;
import model.Real;
import model.TipoMoeda;
import model.TipoTransacao;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class DAO {

	public TipoMoeda buscarTipoMoedaPorID(Integer codigo) {
		
		String sql = "SELECT * FROM tipo_moeda WHERE ID = ?";
	   

	    try (Connection conn = Database.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

	    	stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                double cambio = rs.getDouble("cambio");
                return new TipoMoeda(codigo, nome, cambio);
            }

	    } catch (SQLException e) {
	        System.err.println("Erro ao buscar TipoMoeda: " + e.getMessage());
	    }

	    return null;
	}

	public TipoTransacao buscarTipoTransacaoPorID(Integer codigo) {
		
		String sql = "SELECT * FROM tipo_transacao WHERE ID = ?";
	   

	    try (Connection conn = Database.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

	    	stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                return new TipoTransacao(codigo, nome);
            }

	    } catch (SQLException e) {
	        System.err.println("Erro ao buscar TipoMoeda: " + e.getMessage());
	    }

	    return null;
	}
	
	public void salvarMoeda(Moeda moeda) {
	    String sql = """
	        INSERT INTO moeda (tipo_moeda_id, valor)
	        VALUES (?, ?)
	    """;

	    try (Connection conn = Database.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, moeda.getCodigo());
	        stmt.setDouble(2, moeda.getValor());
	        stmt.executeUpdate();

	    } catch (SQLException e) {
	        System.err.println("Erro ao salvar moeda: " + e.getMessage());
	    }
	}
	
	public void removerMoedasPorId(List<Integer> ids) {
	    if (ids == null || ids.isEmpty()) return;

	    String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
	    String sql = "DELETE FROM moeda WHERE id IN (" + placeholders + ")";

	    try (Connection conn = Database.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        for (int i = 0; i < ids.size(); i++) {
	            stmt.setInt(i + 1, ids.get(i));
	        }

	        stmt.executeUpdate();

	    } catch (SQLException e) {
	        System.err.println("Erro ao remover moedas por ID: " + e.getMessage());
	    }
	}
	
	public void salvarTransacao(HistoricoTransacao transacao) {
	    String sql = """
	        INSERT INTO historico_transacao (valor, saldo_anterior, saldo_pos, tipo_transacao_id, tipo_moeda_id)
	        VALUES (?, ?, ?, ?, ?)
	    """;

	    try (Connection conn = Database.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setDouble(1, transacao.getValorTransacao());
	        stmt.setDouble(2, transacao.getSaldoAnterior());
	        stmt.setDouble(3, transacao.getSaldoAtual());
	        stmt.setInt(4, transacao.getTipoTransacao().getCodigo());
	        stmt.setInt(5, transacao.getCodigoMoeda());

	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        System.err.println("Erro ao salvar transação: " + e.getMessage());
	    }
	}
	
	private Moeda instanciarMoeda(int codigoMoeda, double valor) {
        return switch (codigoMoeda) {
            case 1 -> new Real(1, valor);
            case 2 -> new Dolar(2, valor);
            case 3 -> new Euro(2, valor);
		default -> throw new IllegalArgumentException("Unexpected value: " + codigoMoeda);
        };
    }
	
	public List<Moeda> buscarMoedas() {
		
		List<Moeda> moedas = new ArrayList<>();
		
		String sql = "SELECT * FROM moeda";
		
		try (Connection conn = Database.conectar();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {

		        while (rs.next()) {
		
		            int id = rs.getInt("tipo_moeda_id");
		            double valor = rs.getDouble("valor");
		            Moeda moeda = instanciarMoeda(id, valor);
		          

		            moedas.add(moeda);
		        }

		    } catch (SQLException e) {
		        System.err.println("Erro ao buscar Moedas: " + e.getMessage());
		    }

		    return moedas;
	}

}
