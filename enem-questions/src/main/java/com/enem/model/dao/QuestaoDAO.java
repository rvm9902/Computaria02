package main.java.com.enem.model.dao;

import main.java.com.enem.model.Questao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestaoDAO {
    private Connection connection;

    public QuestaoDAO() {
        // Configuração do banco de dados (SQLite como exemplo)
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:enem_questions.db");
            criarTabela();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS questao (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ano INTEGER NOT NULL, " +
                "cor_caderno TEXT NOT NULL, " +
                "numero_caderno INTEGER NOT NULL, " +
                "questao TEXT NOT NULL)";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean inserirQuestao(Questao questao) {
        String sql = "INSERT INTO questao (ano, cor_caderno, numero_caderno, questao) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, questao.getAno());
            pstmt.setString(2, questao.getCorCaderno());
            pstmt.setInt(3, questao.getNumeroCaderno());
            pstmt.setString(4, questao.getQuestao());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Questao> listarTodasQuestoes() {
        List<Questao> questoes = new ArrayList<>();
        String sql = "SELECT * FROM questao";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Questao questao = new Questao();
                questao.setId(rs.getInt("id"));
                questao.setAno(rs.getInt("ano"));
                questao.setCorCaderno(rs.getString("cor_caderno"));
                questao.setNumeroCaderno(rs.getInt("numero_caderno"));
                questao.setQuestao(rs.getString("questao"));
                questoes.add(questao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return questoes;
    }

    public void fecharConexao() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}