package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Usuario;

public class UsuarioDAO {
    
    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }
    
    public Usuario insert(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (usuario, senha) VALUES(?, ?);";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, usuario.getUsuario());
        statement.setString(2, usuario.getSenha());
        statement.execute();

        ResultSet resultSet = statement.getGeneratedKeys();

        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            usuario.setId(id);
        }

        return usuario;
    }
    
    public void update(Usuario usuario) throws SQLException {
        String sql = "update usuario set usuario = ?, senha = ? where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql); 
        
        statement.setString(1, usuario.getUsuario());
        statement.setString(2, usuario.getSenha());
        statement.setInt(3, usuario.getId());
        statement.execute(); 
    }
    
    public void insertOrUpdate(Usuario usuario) throws SQLException {
        if (usuario.getId() > 0) {
            update(usuario);
        } else {
            insert(usuario);
        }
    }
    
    public void delete(Usuario usuario) throws SQLException {
        String sql = "delete from usuario where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql); 
        
        statement.setInt(1, usuario.getId());
        statement.execute(); 
    }
    
    private ArrayList<Usuario> source(PreparedStatement statement) throws SQLException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        
        while (resultSet.next()) { //percorrendo todas as linhas.
            int id = resultSet.getInt("id"); //pegando os dados de cada coluna.
            String usuario = resultSet.getString("usuario");
            String senha = resultSet.getString("senha");
            
            Usuario usuarioComDadosDoBanco = new Usuario(id, usuario, senha); //transformando as linhas em usuários.
            usuarios.add(usuarioComDadosDoBanco); //trazendo os usuários para o arraylist.
        };
        return usuarios;
    }
    
    public ArrayList<Usuario> selectAll() throws SQLException {
        String sql = "select * from usuario";
        PreparedStatement statement = connection.prepareStatement(sql);
        
        return source(statement);
    }
    
    public Usuario selectById(Usuario usuario) throws SQLException {
        String sql = "select * from usuario where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        
        statement.setInt(1, usuario.getId());
        statement.execute();
        
        return source(statement).get(0); //retornando a primeira posição da lista de usuários.
    }
    
    public boolean validateUserAuthentication(Usuario usuarioAutenticado) throws SQLException {
        String sql = "select * from usuario where usuario = ? and senha = ?";
        PreparedStatement statement = connection.prepareStatement(sql); //preparando a query.
        
        statement.setString(1, usuarioAutenticado.getUsuario()); //prevenindo SQL inject.
        statement.setString(2, usuarioAutenticado.getSenha()); //prevenindo SQL inject.
        
        statement.execute(); //executando no banco.
        ResultSet resultSet = statement.getResultSet(); //pegando o resultando e atribuindo a uma variável
        
        return resultSet.next(); //enquanto existir resultado da query, o resultado será true.
    }
}
