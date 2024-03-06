package view;

import Utils.ClearConsole;
import model.Categoria;
import model.Produto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<Produto> produtos = new ArrayList<Produto>();

    public static void main(String[] args) {
        produtos.add(new Produto("Arroz", 10.0, 10, LocalDate.now(), LocalDate.now().plusDays(10), Categoria.ALIMENTO));
        produtos.add(new Produto("Feijão", 8.0, 10, LocalDate.now(), LocalDate.now().plusDays(10), Categoria.ALIMENTO));
        produtos.add(new Produto("Sabão", 5.0, 10, LocalDate.now(), LocalDate.now().plusDays(10), Categoria.LIMPEZA));
        produtos.add(new Produto("Detergente", 3.0, 10, LocalDate.now(), LocalDate.now().plusDays(10), Categoria.LIMPEZA));

        int escolha;
        while (true) {
            System.out.println(" ---- Menu ---- ");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("3 - Atualizar Produto");
            System.out.println("4 - Excluir Produto");
            System.out.println("5 - Sair");

            System.out.print("-> ");
            try {
                escolha = sc.nextInt();
            } catch (Exception e) {
                sc.nextLine();
                escolha = 0;
            }

            switch (escolha) {
                case 1:
                    CadastrarProduto();
                    break;
                case 2:
                    ListarProdutos();
                    break;
                case 3:
                    AtualizarProduto();
                    break;
                case 4:
                    ExcluirProduto();
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida");
                    break;
            }
        }
    }

    private static void CadastrarProduto() {
        int quantidade;
        double preco;
        String nome;
        LocalDate dataFabricacao;
        LocalDate dataValidade;
        Categoria categoria;

        System.out.println(" ---- Cadastrar Produto ---- ");

        try {
            System.out.print("Digite o nome do produto: ");
            nome = sc.next();
            System.out.print("""
                    Informe a categoria do produto:
                       -LIMPEZA
                       -ALIMENTO
                       -BEBIDA
                       -HIGIENE
                       -ELETRONICO
                       -VESTUARIO
                       -OUTROS
                    ->\s""");
            categoria = Categoria.valueOf(sc.next().toUpperCase());
            System.out.print("Digite o preço do produto: ");
            preco = sc.nextDouble();
            System.out.print("Digite a quantidade do produto: ");
            quantidade = sc.nextInt();
            sc.nextLine();
            System.out.print("Digite a data de fabricação do produto (dd/MM/yyyy): ");
            dataFabricacao = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.print("Digite a data de validade do produto (dd/MM/yyyy): ");
            dataValidade = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            produtos.add(new Produto(nome, preco, quantidade, dataFabricacao, dataValidade, categoria));

            System.out.printf("\n--- Produto cadastrado com o id %d ---\n\n", produtos.getLast().getProductID());
        } catch (Exception e) {
            System.out.println("\n--- Erro ao cadastrar produto ---\n");
            System.out.flush();
        }
    }

    public static void ListarProdutos(){
        System.out.println(" ---- Produtos Cadastrados ---- ");
        if (produtos.isEmpty()) {
            System.out.println("!Nenhum produto cadastrado");
            return;
        }
        for (Produto produto : produtos) {
            System.out.println(produto);
        }
    }

    public static void ExcluirProduto(){
        System.out.println(" ---- Excluir Produto ---- ");
        if (produtos.isEmpty()) {
            System.out.println("!Nenhum produto cadastrado");
            return;
        }
        try {
            System.out.print("Digite o id do produto que deseja excluir: ");
            int id = sc.nextInt();
            for (Produto produto : produtos) {
                if (produto.getProductID() == id) {
                    produtos.remove(produto);
                    System.out.println("Produto excluído com sucesso\n");
                    return;
                }
            }
            System.out.println(" --- Produto não encontrado --- \n");
        } catch (Exception e) {
            sc.nextLine();
            System.out.println(" --- Erro ao excluir produto --- \n");
        }
    }

    public static void AtualizarProduto(){
        System.out.println(" ---- Editar Produto ---- ");
        if (produtos.isEmpty()) {
            System.out.println("!Nenhum produto cadastrado");
            return;
        }
        try {
            System.out.print("Digite o id do produto que deseja editar: ");
            int id = sc.nextInt();
            for (Produto produto : produtos) {
                if (produto.getProductID() == id) {
                    System.out.println("Produto encontrado.");
                    System.out.println("Informe oque deseja editar: ");
                    System.out.println("1 - Nome");
                    System.out.println("2 - Categoria");
                    System.out.println("3 - Preço");
                    System.out.println("4 - Quantidade");
                    System.out.println("5 - Data de Fabricação");
                    System.out.println("6 - Data de Validade");
                    System.out.println("7 - Cancelar");
                    System.out.print("-> ");
                    int escolha = sc.nextInt();

                    switch (escolha) {
                        case 1:
                            System.out.print("Digite o novo nome: ");
                            produto.setNome(sc.next());
                            break;
                        case 2:
                            System.out.print("Digite a nova categoria: ");
                            produto.setCategoria(Categoria.valueOf(sc.next().toUpperCase()));
                            break;
                        case 3:
                            System.out.print("Digite o novo preço: ");
                            produto.setPreco(sc.nextDouble());
                            break;
                        case 4:
                            System.out.print("Digite a nova quantidade: ");
                            produto.setQuantidade(sc.nextInt());
                            break;
                        case 5:
                            System.out.print("Digite a nova data de fabricação: ");
                            produto.setDataFabricacao(LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            break;
                        case 6:
                            System.out.print("Digite a nova data de validade: ");
                            produto.setDataValidade(LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            break;
                        case 7:
                            return;
                        default:
                            System.out.println("--- Opção inválida ---\n");
                            break;
                    }
                    System.out.println("Produto atualizado com sucesso\n");
                    return;
                }
            }
            System.out.println(" --- Produto não encontrado --- \n");
        } catch (Exception e) {
            sc.nextLine();
            System.out.println(" --- Erro ao editar produto --- \n");
        }
    }
}

