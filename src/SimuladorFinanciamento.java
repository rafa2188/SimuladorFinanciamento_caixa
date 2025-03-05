import java.util.InputMismatchException;
import java.util.Scanner;

public class SimuladorFinanciamento {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Entrada de dados com validação e tratamento de exceções
        double valorImovel = lerDouble(scanner, "Digite o valor do imóvel (maior que zero): ", 0, Double.MAX_VALUE);
        double entrada = lerDouble(scanner, "Digite o valor da entrada (deve ser menor que o valor do imóvel): ", 0, valorImovel);
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
        double primeiraParcelaSAC = amortizacao + (valorFinanciado * taxaJurosMensal);
        double ultimaParcelaSAC = amortizacao + ((valorFinanciado - (amortizacao * (prazoMeses - 1))) * taxaJurosMensal);
        double totalPagoSAC = 0;
        for (int i = 0; i < prazoMeses; i++) {
            double jurosMensal = (valorFinanciado - (i * amortizacao)) * taxaJurosMensal;
            totalPagoSAC += amortizacao + jurosMensal;
        }
        double jurosTotaisSAC = totalPagoSAC - valorFinanciado;

        // Exibir resultados
        System.out.println("\nResultado da Simulação:");
        System.out.printf("Valor financiado: R$ %.2f\n", valorFinanciado);
        System.out.println("\nSistema Price:");
        System.out.printf("Parcela fixa: R$ %.2f\n", parcelaPrice);
        System.out.printf("Total pago: R$ %.2f\n", totalPagoPrice);
        System.out.printf("Juros totais: R$ %.2f\n", jurosTotaisPrice);

        System.out.println("\nSistema SAC:");
        System.out.printf("Primeira parcela: R$ %.2f\n", primeiraParcelaSAC);
        System.out.printf("Última parcela: R$ %.2f\n", ultimaParcelaSAC);
        System.out.printf("Total pago: R$ %.2f\n", totalPagoSAC);
        System.out.printf("Juros totais: R$ %.2f\n", jurosTotaisSAC);

        // Verificação de renda
        if (parcelaPrice > rendaMensal * 0.3) {
            System.out.println("\nA parcela do sistema Price compromete mais de 30% da sua renda!");
        } else {
            System.out.println("\nA parcela do sistema Price cabe na sua renda.");
        }

        if (primeiraParcelaSAC > rendaMensal * 0.3) {
            System.out.println("A primeira parcela do sistema SAC compromete mais de 30% da sua renda!");
        } else {
            System.out.println("A primeira parcela do sistema SAC cabe na sua renda.");
        }

        scanner.close();
    }

    private static double lerDouble(Scanner scanner, String mensagem, double min, double max) {
        double valor;
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextDouble();
                if (valor > min && valor < max) {
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

