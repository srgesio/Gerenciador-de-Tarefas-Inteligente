package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Tarefa;
import Model.Usuario;

public class Agenda {
	
	private ArrayList<Tarefa> tarefas;
	Connection conexao;
	
	public Agenda(){
		this.conexao = null;
	}
	
	public boolean cadastrarTarefa(Tarefa umaTarefa, Usuario usuario){
		boolean retorno = false;
		String insert_tarefa = "INSERT INTO tarefa (titulo, descricao, tags, prioridade, data_tarefa, estado, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?);";
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(insert_tarefa);

			stmt.setString(1, umaTarefa.getTitulo());
			stmt.setString(2, umaTarefa.getDescricao());
			stmt.setString(3, umaTarefa.getTags());
			stmt.setInt(4, umaTarefa.getPrioridade());
			stmt.setString(5, umaTarefa.getData());
			stmt.setBoolean(6, umaTarefa.getEstado());
			stmt.setInt(7, usuario.getId());

			if (stmt.executeUpdate() > 0) {
				retorno = true;
			} else {
				retorno = false;
			}
			stmt.close();
			conexao.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	public boolean cadastrarMeta(Tarefa umaTarefa, Usuario usuario){
		boolean retorno = false;
		String insert_tarefa = "INSERT INTO meta (titulo, descricao, tags, prioridade, estado, id_usuario) VALUES (?, ?, ?, ?, ?, ?);";
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(insert_tarefa);

			stmt.setString(1, umaTarefa.getTitulo());
			stmt.setString(2, umaTarefa.getDescricao());
			stmt.setString(3, umaTarefa.getTags());
			stmt.setInt(4, umaTarefa.getPrioridade());
			stmt.setBoolean(5, umaTarefa.getEstado());
			stmt.setInt(6, usuario.getId());

			if (stmt.executeUpdate() > 0) {
				retorno = true;
			} else {
				retorno = false;
			}
			stmt.close();
			conexao.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	
	public void removerTarefa(String titulo, Usuario usuario){
		String delete_tarefa = "DELETE FROM tarefa WHERE titulo = ? AND id_usuario = ?";
		
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(delete_tarefa);
			stmt.setString(1, titulo);
			stmt.setInt(2, usuario.getId());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removerMeta(String titulo, Usuario usuario) {
		String delete_meta = "DELETE FROM meta WHERE titulo = ? AND id_usuario = ?";
		
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(delete_meta);
			stmt.setString(1, titulo);
			stmt.setInt(2, usuario.getId());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void editarTarefa(String titulo, Tarefa tarefa){
		String update_tarefa = "UPDATE tarefa SET titulo = ?, descricao = ?, tags = ?, prioridade = ?, data_tarefa = ? WHERE titulo = ?;";
		
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(update_tarefa);
			stmt.setString(1, tarefa.getTitulo());
			stmt.setString(2, tarefa.getDescricao());
			stmt.setString(3, tarefa.getTags());
			stmt.setInt(4, tarefa.getPrioridade());
			stmt.setString(5, tarefa.getData());
			stmt.setString(6, titulo);
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void editarMeta(String titulo, Tarefa tarefa){
		String update_meta = "UPDATE meta SET titulo = ?, descricao = ?, tags = ?, prioridade = ? WHERE titulo = ?;";
		
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(update_meta);
			stmt.setString(1, tarefa.getTitulo());
			stmt.setString(2, tarefa.getDescricao());
			stmt.setString(3, tarefa.getTags());
			stmt.setInt(4, tarefa.getPrioridade());
			stmt.setString(5, titulo);
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void concluirTarefa(String titulo, Usuario usuario){
		String update_tarefa = "UPDATE tarefa SET estado = true WHERE id_usuario = ? AND titulo = ?;";
		
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(update_tarefa);
			stmt.setInt(1, usuario.getId());
			stmt.setString(2, titulo);
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void concluirMeta(String titulo, Usuario usuario){
		String update_meta = "UPDATE meta SET estado = true WHERE id_usuario = ? AND titulo = ?;";
		
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(update_meta);
			stmt.setInt(1, usuario.getId());
			stmt.setString(2, titulo);
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Tarefa buscarTarefaPorTitulo(String titulo){
		String selected_tarefa = "SELECT * FROM meta WHERE titulo = ?;";
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(selected_tarefa);
			stmt.setString(1, titulo);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				String tituloBD = rs.getString("titulo");
				String descricao = rs.getString("descricao");
				String tags = rs.getString("descricao");
				int prioridade = rs.getInt("prioridade");
				String data = rs.getString("descricao");
				boolean concluida = rs.getBoolean("estado");
				return new Tarefa(id, tituloBD, descricao, tags, prioridade, data, concluida);
			}
			
			stmt.close();
			conexao.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Exibir as tarefas
	
	public ArrayList<Tarefa> exibirTodasTarefas(Usuario usuario){
		this.tarefas = new ArrayList<>();
		String selected_tarefa = "SELECT * FROM tarefa WHERE id_usuario = ?;";
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(selected_tarefa);
			stmt.setInt(1, usuario.getId());
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setId(rs.getInt("id"));
				tarefa.setTitulo(rs.getString("titulo"));
				tarefa.setDescricao(rs.getString("descricao"));
				tarefa.setTags(rs.getString("tags"));
				tarefa.setPrioridade(rs.getInt("prioridade"));
				tarefa.setData(rs.getString("data_tarefa"));
				tarefa.setEstado(rs.getBoolean("estado"));
				tarefa.setUsuario_id(usuario.getId());
				
				tarefas.add(tarefa);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tarefas;
	}
	
	public ArrayList<Tarefa> exibirTodasMeetas(Usuario usuario){
		this.tarefas = new ArrayList<>();
		String selected_meta = "SELECT * FROM meta WHERE id_usuario = ?;";
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(selected_meta);
			stmt.setInt(1, usuario.getId());
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setId(rs.getInt("id"));
				tarefa.setTitulo(rs.getString("titulo"));
				tarefa.setDescricao(rs.getString("descricao"));
				tarefa.setTags(rs.getString("tags"));
				tarefa.setPrioridade(rs.getInt("prioridade"));
				tarefa.setEstado(rs.getBoolean("estado"));
				tarefa.setUsuario_id(usuario.getId());
				
				tarefas.add(tarefa);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tarefas;
	}
	
	public ArrayList<Tarefa> exibirTarefasDiarias(){
		return tarefas;
	}
	
	public ArrayList<Tarefa> exibirTarefasNaoCumpridas(Usuario usuario){
		this.tarefas = new ArrayList<>();
		String selected_tarefa = "SELECT * FROM tarefa WHERE id_usuario = ? AND estado = false;";
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(selected_tarefa);
			stmt.setInt(1, usuario.getId());
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setId(rs.getInt("id"));
				tarefa.setTitulo(rs.getString("titulo"));
				tarefa.setDescricao(rs.getString("descricao"));
				tarefa.setTags(rs.getString("tags"));
				tarefa.setPrioridade(rs.getInt("prioridade"));
				tarefa.setData(rs.getString("data_tarefa"));
				tarefa.setEstado(rs.getBoolean("estado"));
				tarefa.setUsuario_id(usuario.getId());
				
				tarefas.add(tarefa);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tarefas;
	}
	
	public ArrayList<Tarefa> exibirTarefasConcluidas(Usuario usuario){
		this.tarefas = new ArrayList<>();
		String selected_tarefa = "SELECT * FROM tarefa WHERE id_usuario = ? AND estado = true;";
		try {
			conexao = FabricaDeConexao.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(selected_tarefa);
			stmt.setInt(1, usuario.getId());
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setId(rs.getInt("id"));
				tarefa.setTitulo(rs.getString("titulo"));
				tarefa.setDescricao(rs.getString("descricao"));
				tarefa.setTags(rs.getString("tags"));
				tarefa.setPrioridade(rs.getInt("prioridade"));
				tarefa.setData(rs.getString("data_tarefa"));
				tarefa.setEstado(rs.getBoolean("estado"));
				tarefa.setUsuario_id(usuario.getId());
				
				tarefas.add(tarefa);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tarefas;
	}
	
}
