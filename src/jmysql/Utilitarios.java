package jmysql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Utilitarios {

	static Scanner teclado = new Scanner(System.in);

	public static void Menu() {

		System.out.println("********** Sistema Cadastro Funcionários **********");
		System.out.println("Seja bem-vindo(a)! Selecione uma opção: ");
		System.out.println("1 - Listar Funcionários.");
		System.out.println("2 - Inserir dados do funcionário.");
		System.out.println("3 - Atualizar dados do funcionário.");
		System.out.println("4 - Deletar dados do funcionário.");
		System.out.println("5 - Sair do Sistema.");

		int opcao = Integer.parseInt(teclado.nextLine());

		switch (opcao) {
		case 1:
			listar();
			break;
		case 2:
			inserir();
			break;
		case 3:
			atualizar();
		case 4:
			deletar();
			break;
		case 5:
			System.out.println("Obrigada por utilizar nosso sistema!");
			System.exit(0); // Significa que encerrou sem erros.
			break;
		default:
			System.out.println("Opção inválida!");
			Utilitarios.Menu();
			break;

		}
	}

	public static Connection conectar() {
		String CLASSE_DRIVER = "com.mysql.jdbc.Driver";
		String USUARIO = "root";
		String SENHA = "alp1206pla";
		String URL_SERVIDOR = "jdbc:mysql://localhost:3306/java_mysql?useSSL=false";

		try {
			Class.forName(CLASSE_DRIVER); // Verificando se a classe Driver existe.
			return (Connection) DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA); // Se existir, retorna a URL,
																							// USUARIO e SENHA.

		} catch (Exception erro) {
			if (erro instanceof ClassNotFoundException) { // Verificando se o erro é instância de
															// "ClassNotFoundException".
				System.out.println("Verifique o driver de conexão.");
			} else {
				System.out.println("Verifique se o servidor está ativo.");
			}
			System.exit(-42);
			return null;

		}

	}

	public static void desconectar(Connection conectar) {
		if (conectar != null) { // Se a conexão estiver aberta, conecte.
			try {
				conectar.close();
			} catch (SQLException e) {
				System.out.println("Não foi possível fechar a conexão.");
				e.printStackTrace();
			}
		}

	}

	private static void listar() {

		String BUSCAR_FUNCIONARIOS = "SELECT * FROM funcionarios";

		try {

			Connection conn = conectar(); // Estabelecendo uma conexão.
			PreparedStatement funcionarios = (PreparedStatement) conn.prepareStatement(BUSCAR_FUNCIONARIOS); // Preparando
																												// para
																												// executar.
			ResultSet resultado = funcionarios.executeQuery(); // Executando uma pesquisa no banco.

			/*
			 * 
			 * Aqui verifica se existe dados na tabela, reotornado a quantidade de linhas
			 * que existe.
			 * 
			 */
			resultado.last(); // Seguindo pro final da lista.
			int qtd = resultado.getRow(); // Retornando o número de linhas.
			resultado.beforeFirst();// Retorna pro início.

			if (qtd > 0) { // Significa que existe pelo menos um funcionário cadastrado.
				System.out.println("Listando Funcionários");
				System.out.println("************************");

				while (resultado.next()) {
					System.out.println("ID: " + resultado.getInt(1));
					System.out.println("Nome: " + resultado.getString(2));
					System.out.println("CPF: " + resultado.getString(3));
					System.out.println("Idade: " + resultado.getInt(4));
					System.out.println("Cargo: " + resultado.getString(5));
					System.out.println("Setor: " + resultado.getString(6));
					System.out.println("Projeto Atuante: " + resultado.getString(7));
					System.out.println(" ");
				}

			} else {
				System.out.println("Não existe funcionário cadastrado.");
			}

			funcionarios.close(); // Fechando o PreparedStatement.
			desconectar(conn); // Encerranbdo a conexão.
			Utilitarios.Menu();

		} catch (Exception erro) {
			erro.printStackTrace();
			System.err.println("Erro Listando Funcionários.");
			System.exit(-42); // Significa que encerrou com algum erro.

		}

	}

	private static void inserir() {

		String INSERIR_FUNCIONARIOS = "INSERT INTO funcionarios (nome, cpf, idade, cargo, setor, projeto_atuante) VALUES (?, ?, ?, ?, ?, ?)";

		System.out.println("Informe o nome do funcionário: ");
		String nome = teclado.nextLine();

		System.out.println("Informe o CPF: ");
		String cpf = teclado.nextLine();

		System.out.println("Informe a idade: ");
		int idade = Integer.parseInt(teclado.nextLine());

		System.out.println("Informe o cargo: ");
		String cargo = teclado.nextLine();

		System.out.println("Informe o setor: ");
		String setor = teclado.nextLine();

		System.out.println("Informe o nome do projeto que o(a) funcionário(a) está atuando: ");
		String projeto_atuante = teclado.nextLine();

		try {

			Connection conn = conectar();
			PreparedStatement inserir = (PreparedStatement) conn.prepareStatement(INSERIR_FUNCIONARIOS);

			inserir.setString(1, nome);
			inserir.setString(2, cpf);
			inserir.setInt(3, idade);
			inserir.setString(4, cargo);
			inserir.setString(5, setor);
			inserir.setString(6, projeto_atuante);

			inserir.executeUpdate(); // Executando a String INSERIR_FUNCIONARIOS
			inserir.close(); // Fechado o PreparedStatement
			desconectar(conn); // Desconectando o servidor.

			System.out.println("O(a) funcionário(a) " + nome + " foi cadastrado(a) com sucesso.");
			Utilitarios.Menu();

		} catch (Exception erro) {
			erro.printStackTrace();
			System.err.println("Erro ao inserir funcionários.");
			System.exit(-42);

		}

	}

	private static void atualizar() {

		/*
		 * String buscando pelo ID. Primeiro é preciso buscar o funcionário que deseja
		 * fazer a atualização, no caso aqui é pelo ID, para depois realizar a
		 * atualização.
		 */
		String BUSCAR_POR_ID = "SELECT * FROM funcionarios WHERE id=?";

		System.out.println("Informe o código do funcionário: ");
		int id = Integer.parseInt(teclado.nextLine());

		try {

			Connection conn = conectar();
			PreparedStatement buscar = (PreparedStatement) conn.prepareStatement(BUSCAR_POR_ID);

			buscar.setInt(1, id);

			ResultSet res = buscar.executeQuery(); // Resultado da Consulta

			res.last();
			int qtd = res.getRow();
			res.beforeFirst();

			// Se for maior que zero, é porque achou a linha que está o ID.
			if (qtd > 0) {
				// Recebendo os dados para a atualização
				System.out.println("Informe o nome do funcionário: ");
				String nome = teclado.nextLine();

				System.out.println("Informe o CPF: ");
				String cpf = teclado.nextLine();

				System.out.println("Informe a idade: ");
				int idade = Integer.parseInt(teclado.nextLine());

				System.out.println("Informe o cargo: ");
				String cargo = teclado.nextLine();

				System.out.println("Informe o setor: ");
				String setor = teclado.nextLine();

				System.out.println("Informe o nome do projeto que o(a) funcioário(a) está atuando: ");
				String projeto_atuante = teclado.nextLine();

				// String de atualização
				String ATUALIZAR = "UPDATE funcionarios SET nome=?, cpf=?, idade=?, cargo=?, setor=?, projeto_atuante=? WHERE id=?";

				PreparedStatement upd = (PreparedStatement) conn.prepareStatement(ATUALIZAR);

				upd.setString(1, nome);
				upd.setString(2, cpf);
				upd.setInt(3, idade);
				upd.setString(4, cargo);
				upd.setString(5, setor);
				upd.setString(6, projeto_atuante);
				upd.setInt(7, id);

				upd.executeUpdate(); // Executando a String atualizar.
				upd.close();
				desconectar(conn);

				System.out.println("O(a) funcionário(a) " + nome + " foi atualizado(a) com sucesso.");
				Utilitarios.Menu();

			} else {
				System.out.println(
						"Não existe o funcionário com ID informado. Deseja voltar ao menu iniciar? 1 - SIM ou 2 - NÃO");
				int resposta = Integer.parseInt(teclado.nextLine());
				if (resposta == 1) {
					Utilitarios.Menu();

				} else if (resposta == 2) {
					System.out.println("Obrigada por utilizar nosso sistema!");
					System.exit(0); // Saindo do sistema com sucesso, sem erros.
				}

			}

		} catch (Exception erro) {
			erro.printStackTrace();
			System.err.println("Erro ao atualizar o funcionário.");
			System.exit(-42);

		}

	}

	private static void deletar() {

		// Inialmente é preciso realizar a busca do funcionário, pelo ID, que deseja
		// deletar.
		String BUSCAR_FUNCIONARIO = "SELECT * FROM funcionarios WHERE id=?";

		// Em seguida, realizar o delete.
		String DELETAR = "DELETE FROM  funcionarios WHERE id=?";

		System.out.println("Informe o ID do funcionário que desejar deletar: ");
		int id = Integer.parseInt(teclado.nextLine());

		try {
			Connection conn = conectar();
			PreparedStatement buscar = (PreparedStatement) conn.prepareStatement(BUSCAR_FUNCIONARIO);

			buscar.setInt(1, id);

			ResultSet res = buscar.executeQuery();

			res.last();
			int qtd = res.getRow();
			res.beforeFirst();

			/*
			 * Se o funcionário com o ID informado existir, realiza outro PreparedStatement
			 * para realizar a execução do delete.
			 */
			if (qtd > 0) {
				PreparedStatement deletar = (PreparedStatement) conn.prepareStatement(DELETAR);
				deletar.setInt(1, id);

				deletar.executeUpdate();
				deletar.close();
				desconectar(conn);

				System.out.println("O(a) funcionário(a) foi deletado(a) com sucesso!");
				Utilitarios.Menu();

			} else {
				System.out.println("Não existe o funcionário com o ID informado");
				System.out.println("Deseja retornar ao Menu iniciar? 1 - SIM ou 2 - NÃO");
				int resposta = Integer.parseInt(teclado.nextLine());
				if (resposta == 1) {
					Utilitarios.Menu();
				} else if (resposta == 2) {
					System.out.println("Obrigada por Utilizar nosso sistema!");
					System.exit(0);// Saindo sem erros.

				}
			}

		} catch (Exception erro) {
			erro.printStackTrace();
			System.err.println("Erro ao deletar o funcionário.");
			System.exit(-42);

		}

	}

}
