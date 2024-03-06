package model;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Produto {
    private static int idGenerator = 0;
    private String nome;
    private double preco;
    private Categoria categoria;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;
    private int quantidade;

    private int productID;

    public Produto(){
    }

    public Produto(String nome, double preco, Categoria categoria) {
        this(nome, preco, 0, null, null, categoria);
    }

    public Produto(String nome, double preco, int quantidade, LocalDate dataFabricacao, LocalDate dataValidade, Categoria categoria) {
        if ((dataFabricacao != null && dataValidade != null) && dataFabricacao.isAfter(dataValidade)) {
            throw new IllegalArgumentException("Data de fabricação não pode ser maior que a data de validade");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
        this.categoria = categoria;
        this.productID = idGenerator;
        idGenerator++;
    }

    public int getProductID(){
        return productID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        this.preco = preco;
    }

    public LocalDate getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(LocalDate dataFabricacao) {
        if (dataValidade != null && dataFabricacao.isAfter(dataValidade)) {
            throw new IllegalArgumentException("Data de fabricação não pode ser maior que a data de validade");
        }
        this.dataFabricacao = dataFabricacao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        if (dataFabricacao != null && dataFabricacao.isAfter(dataValidade)) {
            throw new IllegalArgumentException("Data de fabricação não pode ser maior que a data de validade");
        }
        this.dataValidade = dataValidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double valorTotalEmEstoque() {
        return preco * quantidade;
    }

    public void adicionarProdutos(int quantidade) {
        this.quantidade += quantidade;
    }

    public void removerProdutos(int quantidade) {
        if (this.quantidade < quantidade) {
            throw new IllegalArgumentException("Não é possível remover mais produtos do que o estoque");
        }
        this.quantidade -= quantidade;
    }

    public String toString() {
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return  String.format("""
                Nome: %s
                Tipo: %s
                Preço: %s
                Quantidade: %d
                Valor total em estoque: %s
                Data de fabricação: %s
                Data de validade: %s
                ID: %d
                """,
                nome, categoria, fmt.format(preco), quantidade, fmt.format(valorTotalEmEstoque()), fmtDate.format(dataFabricacao), fmtDate.format(dataValidade), productID);
    }
}
