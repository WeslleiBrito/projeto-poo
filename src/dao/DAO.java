package dao;

import database.Database;
import model.EditarMoeda;
import model.HistoricoTransacao;
import model.Moeda;
import model.TipoMoeda;
import model.TipoTransacao;
import util.InstanciarMoeda;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;


public class DAO {
	
	private InstanciarMoeda instanciarMoeda = new InstanciarMoeda();
	
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
	
	public Moeda buscarMoedaPorID(Integer codigo) {
		String sql = "SELECT moeda.id as id_moeda, tipo_moeda.id as id_tipo_moeda, moeda.valor, tipo_moeda.nome, tipo_moeda.cambio"
				+ " FROM moeda INNER JOIN tipo_moeda ON moeda.tipo_moeda_id = tipo_moeda.id WHERE ID = ?";
		   

	    try (Connection conn = Database.conectar();
	         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

	    	stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	
            	Moeda m = instanciarMoeda.instanciar(rs.getInt("id_tipo_moeda"));
            	
            	m.setCodigo(rs.getInt("id_moeda"));
            	m.setCodigoMoeda(rs.getInt("id_tipo_moeda"));
            	m.setNome(rs.getString("nome"));
            	m.setCambio(rs.getDouble("cambio"));
            	m.setValor(rs.getDouble("valor"));
            	
            	return m;
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

	        stmt.setInt(1, moeda.getCodigoMoeda());
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
	
	public List<HistoricoTransacao> buscarHistoricoTransacoes() {
		
		List<HistoricoTransacao> listaHistorico = new ArrayList<>();
		
		
		String sql = "SELECT historico_transacao.id as id_transacao, tipo_moeda_id, tipo_transacao_id,"
				+ " valor, saldo_anterior, saldo_pos, data,"
				+ "tipo_moeda.nome as nome_moeda, tipo_transacao.nome as nome_transacao"
				+ " FROM historico_transacao "
				+ "INNER JOIN tipo_moeda ON historico_transacao.tipo_moeda_id = tipo_moeda.id "
				+ "INNER JOIN tipo_transacao ON historico_transacao.tipo_transacao_id = tipo_transacao.id";
		
		try (Connection conn = Database.conectar();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {

		        while (rs.next()) {
		        	int idHistoricoTransacao = rs.getInt("id_transacao");
		        	int idTipoMoeda = rs.getInt("tipo_moeda_id");
		        	int tipoTrasacao = rs.getInt("tipo_transacao_id");;
		            String nomeMoeda = rs.getString("nome_moeda");
		            double valorTransacao = rs.getDouble("valor");
		            double saldoAtual = rs.getDouble("saldo_pos");
		            double saldoAnterior = rs.getDouble("saldo_anterior");
		            ZonedDateTime dataTrasacao = converteFuso(rs.getString("data"));
		            
		            HistoricoTransacao h = new HistoricoTransacao(
		            		idHistoricoTransacao, 
		            		idTipoMoeda, 
		            		tipoTrasacao, 
		            		nomeMoeda, 
		            		valorTransacao, 
		            		saldoAtual, 
		            		saldoAnterior, 
		            		dataTrasacao
		           );
		           

	            	listaHistorico.add(h);
		        }
		        
		    } catch (SQLException e) {
		        System.err.println("Erro ao buscar Histórico de transações: " + e.getMessage());
		    }

		    return listaHistorico;
		
	}
	
	public List<Moeda> buscarMoedas() {
		
		List<Moeda> moedas = new ArrayList<>();
		
		String sql = "SELECT moeda.id as id_moeda, tipo_moeda.id as id_tipo_moeda, moeda.valor, tipo_moeda.nome, tipo_moeda.cambio"
				+ " FROM moeda INNER JOIN tipo_moeda ON moeda.tipo_moeda_id = tipo_moeda.id";
		
		try (Connection conn = Database.conectar();
		         PreparedStatement stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery()) {

		        while (rs.next()) {

		            
		            Moeda m = instanciarMoeda.instanciar(rs.getInt("id_tipo_moeda"));
	            	
	            	m.setCodigo(rs.getInt("id_moeda"));
	            	m.setCodigoMoeda(rs.getInt("id_tipo_moeda"));
	            	m.setNome(rs.getString("nome"));
	            	m.setCambio(rs.getDouble("cambio"));
	            	m.setValor(rs.getDouble("valor"));
		          

		            moedas.add(m);
		        }
		        
		    } catch (SQLException e) {
		        System.err.println("Erro ao buscar Moedas: " + e.getMessage());
		    }

		    return moedas;
	}
	
	public void atualizarValorMoeda(EditarMoeda codigoRegistroMoeda) {

		String sql = "UPDATE moeda SET valor = ? WHERE id = ?";
		
		try (Connection conn = Database.conectar();
		         PreparedStatement stmt = conn.prepareStatement(sql)) {

		        stmt.setDouble(1, codigoRegistroMoeda.getValor());
		        stmt.setInt(2, codigoRegistroMoeda.getId());
		        
		        stmt.executeUpdate();
		        
		    } catch (SQLException e) {
		        System.out.println("Erro ao atualizar valor da moeda: " + e.getMessage());
		    }
	}
	
	private ZonedDateTime converteFuso(String dataString) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		LocalDateTime localDateTime = LocalDateTime.parse(dataString, formatter);
		
		ZonedDateTime data = localDateTime.atZone(ZoneId.of("America/Sao_Paulo"));
		
		return data;
	}
	
	
}
