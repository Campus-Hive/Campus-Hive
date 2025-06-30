/**
 * <p> HelpArticleView Class </p>
 *
 * <p> Description: This class manages the display and functionality for help articles within 
 * the application. It handles loading articles from the database, displaying their details, 
 * and managing user interactions, such as navigating back to the previous screen. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
 */


package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HelpArticleView {

    @FXML private Label titleLabel;
    @FXML private Label levelLabel;
    @FXML private Label groupLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label keywordsLabel;
    @FXML private TextArea bodyTextArea;
    @FXML private TextArea linksListView;
    @FXML private Button backButton;

    private HelpArticle selectedArticle;

    public static class HelpArticle {
        private long id;
        private String title;
        private String description;
        private String body;
        private String keywords;
        private String links;
        private String level;
        private boolean isSensitive;

        public HelpArticle(long id, String title, String description, 
                           String body, String keywords, boolean isSensitive, 
                           String links, String level) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.body = body;
            this.keywords = keywords;
            this.links = links;
            this.level = level;
            this.isSensitive = isSensitive;
        }

        public long getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getBody() { return body; }
        public String getKeywords() { return keywords; }
        public String getLinks() { return links; }
        public String getLevel() { return level; }
        public boolean isSensitive() { return isSensitive; }
    }

    public void loadArticleById(long articleId) {
        HelpArticle article = fetchFullArticleDetails(articleId); // Fetch full details from DB
        if (article != null) {
            displayArticleDetails(article); // Display the details on UI components
        }
    }
    
    private HelpArticle fetchFullArticleDetails(long articleId) {
        String query = "SELECT * FROM articles WHERE id = ?";
        String url = "jdbc:h2:./data/articles/help_articles";
        String user = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, articleId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String body = resultSet.getString("body");
                String keywords = resultSet.getString("keywords");
                String level = resultSet.getString("level");
                String referenceLinks = resultSet.getString("referenceLinks");
                boolean isSensitive = resultSet.getBoolean("isSensitive");

                System.out.println("Fetched article: " + title);
                return new HelpArticle(articleId, title, description, body, keywords, isSensitive, referenceLinks, level);
            } else {
                System.out.println("No article found with ID: " + articleId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void displayArticleDetails(HelpArticle article) {
        String body = article.getBody();
        boolean isSensitive = article.isSensitive();

        try {
            String username = UserSession.getInstance().getUsername();
            boolean hasAccess = H2Database.checkSpecialView(username);

            System.out.println("User has access: " + hasAccess);
            System.out.println("Original Body: " + body);

            if (hasAccess) {
                if (isSensitive && body != null) {
                    System.out.println("Encrypted Body Before Decryption: " + body);
                    try {
                        body = EncryptionManager.decrypt(body, EncryptionManager.getKey());
                        System.out.println("Decrypted Body: " + body);
                    } catch (Exception ex) {
                        System.err.println(article.getId() + " - Decryption failed: " + ex.getMessage());
                        body = "Error decrypting article.";
                    }
                }
            } else {
                if (isSensitive) {
                    body = "This article is encrypted and requires special access.";
                }
            }

            System.out.println("Final body assigned to TextArea: " + body);
            bodyTextArea.setText(body);
        } catch (SQLException e) {
            e.printStackTrace();
            bodyTextArea.setText("Error loading article content. Please try again later.");
        }

        // Set other fields regardless of access
        titleLabel.setText(article.getTitle());
        levelLabel.setText("Level: " + article.getLevel());
        descriptionLabel.setText(article.getDescription());
        keywordsLabel.setText("Keywords: " + article.getKeywords());
        linksListView.setText(article.getLinks());
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GroupArticlePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
