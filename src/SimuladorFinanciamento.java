import java.util.InputMismatchException;
import java.util.Scanner;

public class SimuladorFinanciamento {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Entrada de dados com validação e tratamento de exceções
        System.out.print("Digite seu nome: ");
        String nomeCliente = scanner.nextLine();
        double valorImovel = lerDouble(scanner, "Digite o valor do imóvel (maior que zero): ", 0, Double.MAX_VALUE);
        double entrada = lerDouble(scanner, "Digite o valor da entrada (deve ser pelo menos 10% do valor do imóvel): ", valorImovel * 0.1, valorImovel);
        int prazoAnos = lerInt(scanner, "Digite o prazo do financiamento (em anos, maior que zero): ", 1, Integer.MAX_VALUE);
        double taxaJurosAnual = lerDouble(scanner, "Digite a taxa de juros anual (% positiva): ", 0, Double.MAX_VALUE);
        double rendaMensal = lerDouble(scanner, "Digite sua renda mensal (maior que zero): ", 0, Double.MAX_VALUE);

        // Cálculo do financiamento
        double valorFinanciado = valorImovel - entrada;
        int prazoMeses = prazoAnos * 12;
        double taxaJurosMensal = (taxaJurosAnual / 100) / 12;

        // Sistema Price
        double parcelaPrice = (valorFinanciado * taxaJurosMensal) /
                (1 - Math.pow(1 + taxaJurosMensal, -prazoMeses));
        double totalPagoPrice = parcelaPrice * prazoMeses;
        double jurosTotaisPrice = totalPagoPrice - valorFinanciado;

        // Sistema SAC
        double amortizacao = valorFinanciado / prazoMeses;
        double totalPagoSAC = 0;
        StringBuilder tabelaSAC = new StringBuilder();
        tabelaSAC.append("Parcela | Valor | Amortização | Juros\n");

        for (int i = 0; i < prazoMeses; i++) {
            double saldoDevedor = valorFinanciado - (i * amortizacao);
            double jurosMensal = saldoDevedor * taxaJurosMensal;
            double parcelaSAC = amortizacao + jurosMensal;
            totalPagoSAC += parcelaSAC;
            tabelaSAC.append(String.format("%d | %.2f | %.2f | %.2f\n", i + 1, parcelaSAC, amortizacao, jurosMensal));
        }

        double jurosTotaisSAC = totalPagoSAC - valorFinanciado;

        // Validação da parcela com a renda mensal do cliente
        double percentualParcelaPrice = (parcelaPrice / rendaMensal) * 100;
        double percentualParcelaSAC = (amortizacao + (valorFinanciado * taxaJurosMensal) / rendaMensal) * 100;

        // Exibição dos resultados
        System.out.println("\nResultado da Simulação:");
        System.out.printf("Cliente: %s\n", nomeCliente);
        System.out.printf("Valor do Imóvel: R$ %.2f\n", valorImovel);
        System.out.printf("Valor Financiado: R$ %.2f\n", valorFinanciado);
        System.out.printf("Total pago (Price): R$ %.2f (Juros: R$ %.2f)\n", totalPagoPrice, jurosTotaisPrice);
        System.out.printf("Total pago (SAC): R$ %.2f (Juros: R$ %.2f)\n", totalPagoSAC, jurosTotaisSAC);

        System.out.println("\nTabela de Parcelas (Price):");
        System.out.printf("Parcela | Valor\n");
        for (int i = 0; i < prazoMeses; i++) {
            System.out.printf("%d | %.2f\n", i + 1, parcelaPrice);
        }

        System.out.println("\nTabela de Parcelas (SAC):");
        System.out.println(tabelaSAC.toString());

        if (percentualParcelaPrice > 30) {
            System.out.println("A parcela do sistema Price ultrapassa 30% da sua renda mensal. Verifique se é viável para você.");
        } else {
            System.out.println("A parcela do sistema Price está dentro de 30% da sua renda mensal.");
        }

        if (percentualParcelaSAC > 30) {
            System.out.println("A parcela inicial do sistema SAC ultrapassa 30% da sua renda mensal. Verifique se é viável para você.");
        } else {
            System.out.println("A parcela inicial do sistema SAC está dentro de 30% da sua renda mensal.");
        }

        scanner.close();
    }

    private static double lerDouble(Scanner scanner, String mensagem, double min, double max) {
        double valor;
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextDouble();
                if (valor > min && valor <= max) {
                    return valor;
                }
                System.out.println("Valor inválido. Tente novamente.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número válido.");
                scanner.next(); // Limpar buffer do scanner
            }
        }
    }

    private static int lerInt(Scanner scanner, String mensagem, int min, int max) {
        int valor;
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextInt();
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Valor inválido. Tente novamente.");
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número inteiro válido.");
                scanner.next(); // Limpar buffer do scanner
            }
        }
    }
}


