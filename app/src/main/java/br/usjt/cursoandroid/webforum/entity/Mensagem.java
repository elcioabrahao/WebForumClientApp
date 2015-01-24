package br.usjt.cursoandroid.webforum.entity;

/**
 * Created by Elcio on 21/01/15.
 */
public class Mensagem {

    private int id;
    private int idCliente;
    private String nomeCliente;
    private String data;
    private String mensagem;

    public Mensagem() {
    }

    public Mensagem(int id, int idCliente, String nomeCliente, String data, String mensagem) {
        this.id = id;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.data = data;
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
