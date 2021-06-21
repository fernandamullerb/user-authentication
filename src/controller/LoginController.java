package controller;

import dao.Conexao;
import dao.UsuarioDAO;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Usuario;
import view.LoginView;
import view.MenuView;

public class LoginController {
    
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
    }

    public void autentica() throws SQLException {
        
        //buscar usuário da view:
        String usuario = view.getjTextFieldUsuario().getText();
        String senha = view.getjPasswordFieldSenha().getText();
        
        Usuario usuarioAutenticado = new Usuario(usuario, senha);
        
        //verificar se o usuário existe no banco:
        Connection conexao = new Conexao().getConnection();
        UsuarioDAO usuarioDao = new UsuarioDAO(conexao);
        
        boolean existe = usuarioDao.validateUserAuthentication(usuarioAutenticado);
        
        //se existir, direciona para menu.
        if (existe) {
            MenuView telaMenu = new MenuView();
            telaMenu.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view, "Usuário ou senha inválidos.");
        }   
    }
}
