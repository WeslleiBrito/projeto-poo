package models;

import java.time.LocalDateTime;

public class HistoricoTransacao {

    private String nomeMoeda;
    private double valorTransacao; // valor original da moeda
    private double valorConvertido; // valor da transação em reais
    private LocalDateTime dataTransacao;
    private boolean tipoTransacao; // true = entrada, false = saída
    private double saldoAnterior;
    private double saldoAtual;

    public HistoricoTransacao(Moeda moeda, boolean tipoTransacao, double saldoAtual) {
        this.nomeMoeda = moeda.getNomeMoedaSingular();
        this.valorTransacao = moeda.getValor();
        this.valorConvertido = moeda.converter();
        this.dataTransacao = LocalDateTime.now();
        this.tipoTransacao = tipoTransacao;
        this.saldoAtual = saldoAtual;

        if (tipoTransacao) {
            this.saldoAnterior = saldoAtual - valorConvertido;
        } else {
            this.saldoAnterior = saldoAtual + valorConvertido;
        }
    }

    public String getNomeMoeda() {
        return nomeMoeda;
    }

    public double getValorTransacao() {
        return valorTransacao;
    }

    public double getValorConvertido() {
        return valorConvertido;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public boolean isTipoTransacao() {
        return tipoTransacao;
    }

    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }

    @Override
    public String toString() {
        return "Transação [" +
                "Tipo: " + (tipoTransacao ? "Entrada" : "Saída") +
                ", Moeda: " + nomeMoeda +
                ", Valor original: " + valorTransacao +
                ", Data: " + dataTransacao +
                ", Saldo anterior: " + saldoAnterior +
                ", Saldo atual: " + saldoAtual + "]";
    }
}
