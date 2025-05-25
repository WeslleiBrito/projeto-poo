package model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import dao.DAO;
import error.ErroBancoDeDadosException;
import util.InstanciarMoeda;

/**
 * Classe responsável por gerenciar operações relacionadas ao cofrinho,
 * como adicionar e retirar moedas, consultar saldo e acessar o histórico de transações.
 * @author Wesllei Brito
 */
public class Cofrinho {

    private final DAO dao = new DAO();
    private InstanciarMoeda instanciar = new InstanciarMoeda();
    private static final int TIPO_TRANSACAO_ENTRADA = 1;
    private static final int TIPO_TRANSACAO_SAIDA = 2;

    /**
     * Adiciona uma moeda ao cofrinho e registra a transação no histórico.
     *
     * @param moeda Objeto Moeda a ser adicionada.
     * @throws IllegalArgumentException se a moeda for nula, inválida ou de valor inferior a 0.01.
     */
    public void adicionar(Moeda moeda) throws ErroBancoDeDadosException {
        if (moeda == null) {
            throw new IllegalArgumentException("Nenhuma moeda enviada.");
        }

        TipoMoeda dadosMoeda = dao.buscarTipoMoedaPorID(moeda.getCodigoMoeda());
        if (dadosMoeda == null) {
            throw new IllegalArgumentException("Tipo de moeda inválido ou inexistente.");
        }

        if (moeda.getValor() < 0.01) {
            throw new IllegalArgumentException("O valor de depósito mínimo é de 0.01.");
        }

        moeda.setCambio(dadosMoeda.getCambio());
        moeda.setNome(dadosMoeda.getNome());

        HistoricoTransacao transacao = criarTransacao(TIPO_TRANSACAO_ENTRADA, moeda, getSaldo());

        dao.salvarMoeda(moeda);
        dao.salvarTransacao(transacao);
    }

    /**
     * Retira um valor específico de um tipo de moeda.
     *
     * @param codigoTipoMoeda Código do tipo da moeda a ser retirada.
     * @param valor Valor total a ser retirado.
     * @throws IllegalArgumentException se o tipo de moeda for inválido ou saldo insuficiente.
     */
    public void retirarValorDeUmaTipoDeMoeda(int codigoTipoMoeda, double valor) throws ErroBancoDeDadosException{
        TipoMoeda dadosMoeda = dao.buscarTipoMoedaPorID(codigoTipoMoeda);
        if (dadosMoeda == null) {
            throw new IllegalArgumentException("Tipo de moeda inválido ou inexistente.");
        }

        List<Moeda> listaMoeda = new ArrayList<>(selecionarMoedasPorId(codigoTipoMoeda));
        double saldoDisponivel = listaMoeda.stream().map(m -> m.getValor()).reduce(0.0, Double::sum);
        listaMoeda.sort(Comparator.comparingDouble(Moeda::getValor));

        if (valor > saldoDisponivel) {
            throw new IllegalArgumentException("Saldo insuficiente da moeda " + dadosMoeda.getNome());
        }

        double restante = valor;
        Iterator<Moeda> iterator = listaMoeda.iterator();
        List<Integer> deletar = new ArrayList<>();
        EditarMoeda editar = new EditarMoeda(-1, 0);

        while (iterator.hasNext() && restante > 0) {
            Moeda m = iterator.next();
            double valMoeda = m.getValor();

            if (valMoeda <= restante) {
                restante -= valMoeda;
                deletar.add(m.getCodigo());
            } else {
                editar.setId(m.getCodigo());
                editar.setValor(valMoeda - restante);
                restante = 0;
            }
        }

        Moeda moedaRegistro = this.instanciar.instanciar(dadosMoeda.getCodigo(), valor);
        moedaRegistro.setNome(dadosMoeda.getNome());
        moedaRegistro.setCambio(dadosMoeda.getCambio());

        HistoricoTransacao transacao = criarTransacao(TIPO_TRANSACAO_SAIDA, moedaRegistro, getSaldo());

        dao.removerMoedasPorId(deletar);
        dao.editarMoeda(editar);
        dao.salvarTransacao(transacao);
    }

    /**
     * Retira um valor do saldo total, considerando todas as moedas.
     *
     * @param valor Valor a ser retirado em Real.
     * @throws IllegalArgumentException se o saldo for insuficiente.
     */
    public void retirarValorSaldo(double valor) throws ErroBancoDeDadosException {
        if (valor > getSaldo()) {
            throw new IllegalArgumentException("Saldo total insuficiente para a retirada.");
        }

        double restante = valor;
        List<Moeda> listaMoedas = new ArrayList<>(dao.buscarMoedas());
        listaMoedas.sort(Comparator.comparingDouble(Moeda::getValor));
        Iterator<Moeda> iterator = listaMoedas.iterator();
        List<Integer> deletar = new ArrayList<>();
        EditarMoeda moedaEditada = new EditarMoeda(-1, 0);

        while (iterator.hasNext() && restante > 0) {
            Moeda m = iterator.next();

            if (m.converter() <= restante) {
                restante -= m.converter();
                deletar.add(m.getCodigo());
            } else {
                double novoValor = m.getValor() - (restante / m.getCambio());
                moedaEditada.setId(m.getCodigo());
                moedaEditada.setValor(novoValor);
                restante = 0;
            }
        }

        Moeda moedaRegistro = new Real(valor);
        String nomeMoeda = dao.buscarTipoMoedaPorID(moedaRegistro.getCodigoMoeda()).getNome();
        moedaRegistro.setNome(nomeMoeda);

        HistoricoTransacao transacao = criarTransacao(TIPO_TRANSACAO_SAIDA, moedaRegistro, getSaldo());

        dao.removerMoedasPorId(deletar);
        dao.editarMoeda(moedaEditada);
        dao.salvarTransacao(transacao);
    }

    /**
     * Retorna o saldo total convertido em Real.
     *
     * @return Valor total do saldo em Reais.
     */
    public double getSaldo() throws ErroBancoDeDadosException {
        return dao.buscarMoedas().stream().map(Moeda::converter).reduce(0.0, Double::sum);
    }

    /**
     * Retorna o histórico completo de transações.
     *
     * @return Lista de objetos HistoricoTransacao.
     */
    public List<HistoricoTransacao> getHistorico() throws ErroBancoDeDadosException {
        return dao.buscarHistoricoTransacoes();
    }

    /**
     * Retorna todos os tipos de moedas disponíveis.
     *
     * @return Lista de objetos TipoMoeda.
     */
    public List<TipoMoeda> getTipoMoedas() throws ErroBancoDeDadosException {
        return dao.buscarTipoMoeda();
    }

    /**
     * Retorna todas as moedas de um tipo específico.
     *
     * @param codigoMoeda Código do tipo de moeda.
     * @return Lista de moedas do tipo informado.
     */
    public List<Moeda> selecionarMoedasPorId(int codigoMoeda) throws ErroBancoDeDadosException {
        return dao.buscarMoedas().stream()
            .filter(moeda -> moeda.getCodigoMoeda() == codigoMoeda)
            .toList();
    }

    /**
     * Cria uma transação com os dados informados.
     *
     * @param tipoTransacao Tipo da transação (entrada ou saída).
     * @param moeda Objeto moeda usado na transação.
     * @param saldoAnterior Saldo anterior à transação.
     * @return Objeto HistoricoTransacao com os dados da operação.
     */
    private HistoricoTransacao criarTransacao(int tipoTransacao, Moeda moeda, double saldoAnterior) {
        double saldoAtual = tipoTransacao == TIPO_TRANSACAO_ENTRADA
            ? saldoAnterior + moeda.converter()
            : saldoAnterior - moeda.converter();

        return new HistoricoTransacao(
            null,
            moeda.getCodigoMoeda(),
            tipoTransacao,
            moeda.getNome(),
            moeda.getValor(),
            saldoAtual,
            saldoAnterior,
            ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
        );
    }

}
