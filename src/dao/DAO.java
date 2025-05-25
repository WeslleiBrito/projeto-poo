package dao;

import database.Database;
import error.ErroBancoDeDadosException;
import model.EditarMoeda;
import model.HistoricoTransacao;
import model.Moeda;
import model.TipoMoeda;
import model.TipoTransacao;
import util.InstanciarMoeda;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe DAO responsável pela manipulação dos dados no banco relacionados a Moeda,
 * TipoMoeda, TipoTransacao e Histórico de Transações.
 * @author Wesllei Brito
 */
public class DAO {

    private final InstanciarMoeda instanciarMoeda = new InstanciarMoeda();

    /**
     * Retorna uma lista com todos os tipos de moeda cadastrados.
     */
    public List<TipoMoeda> buscarTipoMoeda() throws ErroBancoDeDadosException {
        String sql = "SELECT * FROM tipo_moeda";
        List<TipoMoeda> listaTipoMoeda = new ArrayList<>();

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TipoMoeda tipoMoeda = new TipoMoeda(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("cambio")
                );
                listaTipoMoeda.add(tipoMoeda);
            }
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao buscar todas as moedas.", e);
        }

        return listaTipoMoeda;
    }

    /**
     * Retorna um TipoMoeda pelo ID.
     */
    public TipoMoeda buscarTipoMoedaPorID(Integer codigo) throws ErroBancoDeDadosException {
        String sql = "SELECT * FROM tipo_moeda WHERE id = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TipoMoeda(codigo, rs.getString("nome"), rs.getDouble("cambio"));
            }
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao buscar a moeda pelo ID.", e);
        }

        return null;
    }

    /**
     * Retorna um TipoTransacao pelo ID.
     */
    public TipoTransacao buscarTipoTransacaoPorID(Integer codigo) throws ErroBancoDeDadosException {
        String sql = "SELECT * FROM tipo_transacao WHERE id = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TipoTransacao(codigo, rs.getString("nome"));
            }
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao buscar tipo de transação por ID.", e);
        }

        return null;
    }

    /**
     * Busca uma moeda pelo ID.
     */
    public Moeda buscarMoedaPorID(Integer codigo) throws ErroBancoDeDadosException {
        String sql = "SELECT moeda.id as id_moeda, tipo_moeda.id as id_tipo_moeda, moeda.valor, tipo_moeda.nome, tipo_moeda.cambio " +
                     "FROM moeda INNER JOIN tipo_moeda ON moeda.tipo_moeda_id = tipo_moeda.id WHERE moeda.id = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Moeda m = instanciarMoeda.instanciar(rs.getInt("id_tipo_moeda"), rs.getDouble("valor"));
                m.setCodigo(rs.getInt("id_moeda"));
                m.setCodigoMoeda(rs.getInt("id_tipo_moeda"));
                m.setNome(rs.getString("nome"));
                m.setCambio(rs.getDouble("cambio"));
                return m;
            }
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao buscar moeda por ID.", e);
        }

        return null;
    }

    /**
     * Insere uma nova moeda no banco.
     */
    public void salvarMoeda(Moeda moeda) throws ErroBancoDeDadosException {
        String sql = "INSERT INTO moeda (tipo_moeda_id, valor) VALUES (?, ?)";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moeda.getCodigoMoeda());
            stmt.setDouble(2, moeda.getValor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao salvar moeda.", e);
        }
    }

    /**
     * Remove moedas com os IDs fornecidos.
     */
    public void removerMoedasPorId(List<Integer> ids) throws ErroBancoDeDadosException {
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
            throw new ErroBancoDeDadosException("Erro ao remover moedas por ID.", e);
        }
    }

    /**
     * Insere um novo histórico de transação.
     */
    public void salvarTransacao(HistoricoTransacao transacao) throws ErroBancoDeDadosException {
        String sql = "INSERT INTO historico_transacao (valor, saldo_anterior, saldo_pos, tipo_transacao_id, tipo_moeda_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, transacao.getValorTransacao());
            stmt.setDouble(2, transacao.getSaldoAnterior());
            stmt.setDouble(3, transacao.getSaldoAtual());
            stmt.setInt(4, transacao.getTipoTransacao().getCodigo());
            stmt.setInt(5, transacao.getCodigoMoeda());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao salvar transação.", e);
        }
    }

    /**
     * Busca todas as transações do histórico.
     */
    public List<HistoricoTransacao> buscarHistoricoTransacoes() throws ErroBancoDeDadosException {
        List<HistoricoTransacao> listaHistorico = new ArrayList<>();

        String sql = "SELECT historico_transacao.id as id_transacao, tipo_moeda_id, tipo_transacao_id, " +
                     "valor, saldo_anterior, saldo_pos, data, " +
                     "tipo_moeda.nome as nome_moeda, tipo_transacao.nome as nome_transacao " +
                     "FROM historico_transacao " +
                     "INNER JOIN tipo_moeda ON historico_transacao.tipo_moeda_id = tipo_moeda.id " +
                     "INNER JOIN tipo_transacao ON historico_transacao.tipo_transacao_id = tipo_transacao.id";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HistoricoTransacao h = new HistoricoTransacao(
                        rs.getInt("id_transacao"),
                        rs.getInt("tipo_moeda_id"),
                        rs.getInt("tipo_transacao_id"),
                        rs.getString("nome_moeda"),
                        rs.getDouble("valor"),
                        rs.getDouble("saldo_pos"),
                        rs.getDouble("saldo_anterior"),
                        converteFuso(rs.getString("data"))
                );
                listaHistorico.add(h);
            }
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao buscar histórico de transações.", e);
        }

        return listaHistorico;
    }

    /**
     * Busca todas as moedas cadastradas.
     */
    public List<Moeda> buscarMoedas() throws ErroBancoDeDadosException {
        List<Moeda> moedas = new ArrayList<>();
        String sql = "SELECT moeda.id as id_moeda, tipo_moeda.id as id_tipo_moeda, moeda.valor, tipo_moeda.nome, tipo_moeda.cambio " +
                     "FROM moeda INNER JOIN tipo_moeda ON moeda.tipo_moeda_id = tipo_moeda.id";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Moeda m = instanciarMoeda.instanciar(rs.getInt("id_tipo_moeda"), rs.getDouble("valor"));
                m.setCodigo(rs.getInt("id_moeda"));
                m.setCodigoMoeda(rs.getInt("id_tipo_moeda"));
                m.setNome(rs.getString("nome"));
                m.setCambio(rs.getDouble("cambio"));
                moedas.add(m);
            }
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao buscar moedas.", e);
        }

        return moedas;
    }

    /**
     * Atualiza o valor de uma moeda existente.
     */
    public void editarMoeda(EditarMoeda moeda) throws ErroBancoDeDadosException {
        String sql = "UPDATE moeda SET valor = ? WHERE id = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, moeda.getValor());
            stmt.setInt(2, moeda.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ErroBancoDeDadosException("Erro ao atualizar valor da moeda.", e);
        }
    }

    /**
     * Converte a data recebida do banco para o fuso horário de São Paulo.
     */
    private ZonedDateTime converteFuso(String dataString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dataString, formatter);
        return localDateTime.atZone(ZoneId.of("America/Sao_Paulo"));
    }
}