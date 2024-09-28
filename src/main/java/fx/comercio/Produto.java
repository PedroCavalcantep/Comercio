package fx.comercio;

public class Produto {
    private String codProd;
    private String descricao;
    private double precoCompra;
    private double precoVenda;
    private String codBarra;
    private String ncm;
    private int qtd;

    // Construtor padrão
    public Produto() {
    }

    // Construtor existente
    public Produto(String codProd, String descricao, double precoCompra, double precoVenda, String codBarra, String ncm, int qtd) {
        this.codProd = codProd;
        this.descricao = descricao;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.codBarra = codBarra;
        this.ncm = ncm;
        this.qtd = qtd;
    }

    // Novo construtor que você precisa
    public Produto(String descricao, double precoCompra, double precoVenda) {
        this.descricao = descricao;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
    }

    // Getters e Setters (se necessário)
    public String getCodProd() {
        return codProd;
    }

    public void setCodProd(String codProd) {
        this.codProd = codProd;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
}
