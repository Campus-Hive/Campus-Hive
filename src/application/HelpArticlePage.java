/**
 * <p> HelpArticlePage Class </p>
 *
 * <p> Description: This class manages the help articles, providing functionalities for creating,
 * editing, and deleting articles, as well as filtering and searching through them. It interfaces
 * with a database to persist article data. </p>
 *
 * <p> Copyright: Campus Hive © 2024 </p>
 * 
 * @author Th01 (Abhinav Ranish, Aditya Singh, Bharath Gowda, Pranjal Shrivastava, Shorya Raj)
 * 
 * @version 4.00 2024-12-06 Fourth version of this JavaFX project was made (Phase 4)
 */


package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.Node;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class HelpArticlePage implements Initializable {

    @FXML private MenuItem newArticleMenuItem;
    @FXML private MenuItem backupMenuItem;
    @FXML private MenuItem restoreMenuItem;
    @FXML private MenuItem restoreAllMenuItem;
    @FXML private Button BackButton;

    @FXML private ComboBox<String> levelFilter;
    @FXML private TextField searchBox;
    @FXML private ListView<HelpArticle> articleList;
    @FXML private Button newArticleButton;

    @FXML private Button viewByGroupButton;

    @FXML private HBox articleGroup;
    @FXML private CheckBox sqlFiddleCheckBox;
    @FXML private CheckBox h2CheckBox;
    @FXML private CheckBox javaCheckBox;
    @FXML private CheckBox javaFXCheckBox;
    @FXML private CheckBox eclipseCheckBox;
    @FXML private CheckBox intelliJCheckBox;

    @FXML private TextField idField;
    @FXML private ComboBox<String> articleLevel;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField keywordsField;
    @FXML private TextArea bodyArea;
    @FXML private TextArea linksList;
    @FXML private TextField newLinkField;
    @FXML private Button addLinkButton;
    @FXML private CheckBox sensitiveCheckbox;
   
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;

    @FXML private Label errorLabel;

    // Observable lists for UI components
    private ObservableList<HelpArticle> articles = FXCollections.observableArrayList();
    private ObservableList<String> links = FXCollections.observableArrayList();
    private final String[] levels = {"View All Articles","Beginner", "Intermediate", "Advanced", "Expert"};

    private static final String DB_URL = "jdbc:h2:./data/articles/help_articles"; // Path to H2 database
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	initializeDatabase();
        // Initialize ComboBoxes
        levelFilter.setItems(FXCollections.observableArrayList(levels));
        articleLevel.setItems(FXCollections.observableArrayList(levels));

        // Initialize ListViews
        articleList.setItems(articles);


        // Setup search functionality
        setupSearch();

        // Setup event handlers
        setupEventHandlers();

        initializeArticleGroups();

        // Load existing articles from database
        try {
			loadArticles();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Setup Enter key handling
         setupEnterKeyHandling();
    }

    @FXML
    private void initializeArticleGroups() {
        sqlFiddleCheckBox = new CheckBox("SQLFiddle");
        h2CheckBox = new CheckBox("H2");
        javaCheckBox = new CheckBox("Java");
        javaFXCheckBox = new CheckBox("JavaFX");
        eclipseCheckBox = new CheckBox("Eclipse");
        intelliJCheckBox = new CheckBox("IntelliJ");

        articleGroup.getChildren().addAll(
            sqlFiddleCheckBox, h2CheckBox, javaCheckBox,
            javaFXCheckBox, eclipseCheckBox, intelliJCheckBox
        );
    }
    
    private void initializeDatabase() {
        String createArticlesTableSQL = "CREATE TABLE IF NOT EXISTS articles (" +
                "id BIGINT PRIMARY KEY, " +
                "level VARCHAR(20), " +
                "title VARCHAR(255), " +
                "description TEXT, " +
                "keywords VARCHAR(255), " +
                "body TEXT, " +
                "isSensitive BOOLEAN, " +
                "groups VARCHAR(255), " +
                "referenceLinks VARCHAR(255)" +
                ");";

        String createEncryptionTableSQL = "CREATE TABLE IF NOT EXISTS article_encryption (" +
                "article_id BIGINT PRIMARY KEY, " +
                "encryption_key VARCHAR(512) NOT NULL, " +
                "FOREIGN KEY (article_id) REFERENCES articles(id)" +
                ");";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute(createArticlesTableSQL);
            statement.execute(createEncryptionTableSQL); // Ensure encryption table is created
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }


    private void setupEnterKeyHandling() {
        // Use Platform.runLater to ensure fields are properly focused after loading
        Platform.runLater(() -> searchBox.requestFocus());

        // Set up Enter key focus movement for each field
        searchBox.setOnAction(event -> levelFilter.requestFocus());
        levelFilter.setOnAction(event -> idField.requestFocus());
        idField.setOnAction(event -> articleLevel.requestFocus());
        articleLevel.setOnAction(event -> titleField.requestFocus());
        titleField.setOnAction(event -> descriptionArea.requestFocus());

        // Handle Enter key in TextArea fields (descriptionArea and bodyArea)
        descriptionArea.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                keywordsField.requestFocus(); // Move to the next field when Enter is pressed
                event.consume(); // Prevent adding a new line
            }
        });

        keywordsField.setOnAction(event -> bodyArea.requestFocus());

        bodyArea.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
            	linksList.requestFocus(); // Move to the next field when Enter is pressed
                event.consume(); // Prevent adding a new line
            }
        });

//        newLinkField.setOnAction(event -> handleAddLink()); // Trigger link addition on Enter
   }

    @FXML
    private void handleGroupArticlePageAction(ActionEvent event) {
        try {
        	UserSession.getInstance().addPageToHistory("HelpArticlePage.fxml");
            Parent groupPage = FXMLLoader.load(getClass().getResource("GroupArticlePage.fxml"));
            Scene groupScene = new Scene(groupPage);
            Stage currentStage = (Stage) viewByGroupButton.getScene().getWindow();
            currentStage.setScene(groupScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSearch() {
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filterArticles();
        });

        levelFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterArticles();
        });
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Get the current user session
            UserSession session = UserSession.getInstance();
            Parent homePage;

            if (session.hasPreviousPage()) {
                // Navigate to the previous page if it exists
                String previousPage = session.getPreviousPage();
                homePage = FXMLLoader.load(getClass().getResource(previousPage));
                System.out.println("Redirecting to: " + previousPage);
            } else {
                // If no previous page, navigate based on the user's role
                String role = session.getRole();
                if (role == null) {
                    throw new IOException("Role not set in session. Unable to navigate.");
                } else if ("admin".equalsIgnoreCase(role)) {
                    homePage = FXMLLoader.load(getClass().getResource("Admin_Home_Page.fxml"));
                } else if ("instructor".equalsIgnoreCase(role)) {
                    homePage = FXMLLoader.load(getClass().getResource("Instructor_Homepage.fxml"));
                } 
                else {
                    throw new IOException("User role not recognized");
                }
            }

            // Set the scene to the loaded page
            Scene homeScene = new Scene(homePage);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(homeScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void filterArticles() {
        String searchText = searchBox.getText().toLowerCase();
        String selectedLevel = levelFilter.getValue();

        ObservableList<HelpArticle> filteredList = FXCollections.observableArrayList();

        for (HelpArticle article : articles) {
            boolean levelMatch = (selectedLevel == null || selectedLevel.equals("View All Articles")) || 
                                article.getLevel().equals(selectedLevel);
            boolean searchMatch = searchText.isEmpty() || 
                                article.getTitle().toLowerCase().contains(searchText) ||
                                article.getDescription().toLowerCase().contains(searchText) ||
                                article.getKeywords().toLowerCase().contains(searchText);

            if (levelMatch && searchMatch) {
                filteredList.add(article);
            }
        }

        articleList.setItems(filteredList);
    }


    private void setupEventHandlers() {
        // Menu items
        newArticleMenuItem.setOnAction(e -> handleNewArticle());
//        backupMenuItem.setOnAction(e -> handleBackup());
//        restoreMenuItem.setOnAction(e -> handleRestore(false));
//        restoreAllMenuItem.setOnAction(e -> handleRestore(true));

        // Buttons
        newArticleButton.setOnAction(e -> handleNewArticle());

        saveButton.setOnAction(e -> handleSave());
        deleteButton.setOnAction(e -> {
			try {
				handleDelete();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        cancelButton.setOnAction(e -> handleCancel());

        // Article selection
        articleList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayArticle(newValue);
                }
            }
        );
    }

    private void handleNewArticle() {
        clearForm();
        idField.setText(generateNewId());
        linksList.clear();
    }


    @FXML
    private void handleSave() {
        try {
            HelpArticle article = createArticleFromForm();
            if (validateArticle(article)) {
                saveArticle(article);
                updateArticleList();
                showMessage("Article saved successfully", false);
            }
        } catch (Exception e) {
            showMessage("Error saving article: " + e.getMessage(), true);
        }
    }

    private HelpArticle createArticleFromForm() {
        long articleId = Long.parseLong(idField.getText());
        String level = articleLevel.getValue();
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String keywords = keywordsField.getText();
        String body = bodyArea.getText();
  
        String referenceLinks = linksList.getText();
        boolean isSensitive = sensitiveCheckbox.isSelected();
        String groups = getSelectedGroups().stream().collect(Collectors.joining(", "));

        return new HelpArticle(articleId, level, title, description, keywords, body, referenceLinks, isSensitive, groups);
    }

    @FXML
    private void handleDelete() throws Exception {
        HelpArticle selectedArticle = articleList.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            if (confirmDelete()) {
                deleteArticle(selectedArticle);
                updateArticleList();
                clearForm();
                showMessage("Article deleted successfully", false);
            }
        }
    }

    private List<String> getSelectedGroups() {
        List<String> selectedGroups = new ArrayList<>();
        if (sqlFiddleCheckBox.isSelected()) selectedGroups.add("SQLFiddle");
        if (h2CheckBox.isSelected()) selectedGroups.add("H2");
        if (javaCheckBox.isSelected()) selectedGroups.add("Java");
        if (javaFXCheckBox.isSelected()) selectedGroups.add("JavaFX");
        if (eclipseCheckBox.isSelected()) selectedGroups.add("Eclipse");
        if (intelliJCheckBox.isSelected()) selectedGroups.add("IntelliJ");
        return selectedGroups;
    }


    private void handleCancel() {
        clearForm();
        articleList.getSelectionModel().clearSelection();
    }

    private void displayArticle(HelpArticle article) {
        idField.setText(String.valueOf(article.getId()));
        articleLevel.setValue(article.getLevel());
        titleField.setText(article.getTitle());
        descriptionArea.setText(article.getDescription());
        keywordsField.setText(article.getKeywords());
        bodyArea.setText(article.getBody());
        linksList.setText(article.getReferenceLinks());
        sensitiveCheckbox.setSelected(article.isSensitive());
        updateArticleGroupCheckboxes(article.getGroups());
    }
    
    private void updateArticleGroupCheckboxes(String groups) {
        sqlFiddleCheckBox.setSelected(groups.contains("SQLFiddle"));
        h2CheckBox.setSelected(groups.contains("H2"));
        javaCheckBox.setSelected(groups.contains("Java"));
        javaFXCheckBox.setSelected(groups.contains("JavaFX"));
        eclipseCheckBox.setSelected(groups.contains("Eclipse"));
        intelliJCheckBox.setSelected(groups.contains("IntelliJ"));
    }
    
    private void clearArticleGroupCheckboxes() {
        sqlFiddleCheckBox.setSelected(false);
        h2CheckBox.setSelected(false);
        javaCheckBox.setSelected(false);
        javaFXCheckBox.setSelected(false);
        eclipseCheckBox.setSelected(false);
        intelliJCheckBox.setSelected(false);
    }

    private void clearForm() {
        idField.clear();
        articleLevel.setValue(null);
        titleField.clear();
        descriptionArea.clear();
        keywordsField.clear();
        bodyArea.clear();
        linksList.clear();
        sensitiveCheckbox.setSelected(false);
        errorLabel.setText("");
        clearArticleGroupCheckboxes();
    }

    private boolean validateArticle(HelpArticle article) {
        if (article.getTitle().isEmpty()) {
            showMessage("Title is required", true);
            return false;
        }
        if (article.getLevel() == null) {
            showMessage("Level is required", true);
            return false;
        }
        if (article.getGroups() == null || article.getGroups().isEmpty()) {  // Checking if group is not selected
            showMessage("Please select a group", true);
            return false;
        }
        
        
        // Add more validation as needed
        return true;
    }

    private void showMessage(String message, boolean isError) {
        errorLabel.setText(message);
        errorLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Article");
        alert.setContentText("Are you sure you want to delete this article?");
        return alert.showAndWait().get() == ButtonType.OK;
    }
    
    public static String generateEncryptionKey() {
        return Base64.getEncoder().encodeToString(java.util.UUID.randomUUID().toString().getBytes());
    }
    
    private void printDatabaseContent() {
        String articlesQuery = "SELECT * FROM articles";
        String encryptionQuery = "SELECT * FROM article_encryption";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            System.out.println("----- Articles Table -----");
            try (ResultSet rs = stmt.executeQuery(articlesQuery)) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getLong("id"));
                    System.out.println("Level: " + rs.getString("level"));
                    System.out.println("Title: " + rs.getString("title"));
                    System.out.println("Description: " + rs.getString("description"));
                    System.out.println("Keywords: " + rs.getString("keywords"));
                    System.out.println("Body: " + rs.getString("body"));
                    System.out.println("Sensitive: " + rs.getBoolean("isSensitive"));
                    System.out.println("Groups: " + rs.getString("groups"));
                    System.out.println("Links: " + rs.getString("referenceLinks"));
                    System.out.println("---------------------------");
                }
            }

            System.out.println("----- Article Encryption Table -----");
            try (ResultSet rs = stmt.executeQuery(encryptionQuery)) {
                while (rs.next()) {
                    System.out.println("Article ID: " + rs.getLong("article_id"));
                    System.out.println("Encryption Key: " + rs.getString("encryption_key"));
                    System.out.println("---------------------------");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error printing database content: " + e.getMessage());
        }
    }


    public void saveArticle(HelpArticle article) {
        String checkSQL = "SELECT COUNT(*) FROM articles WHERE id = ?";
        String updateSQL = "UPDATE articles SET level = ?, title = ?, description = ?, keywords = ?, " +
                           "body = ?, referenceLinks = ?, isSensitive = ?, groups = ? WHERE id = ?";
        String insertSQL = "INSERT INTO articles (id, level, title, description, keywords, body, " +
                           "referenceLinks, isSensitive, groups) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            boolean articleExists = false;
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL)) {
                checkStmt.setLong(1, article.getId());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    articleExists = rs.getInt(1) > 0;
                }
            }

            // Encrypt if sensitive
            if (article.isSensitive()) {
                String encryptedBody = EncryptionManager.encrypt(article.getBody(), EncryptionManager.getKey());
                System.out.println("Encrypted Body Before Saving: " + encryptedBody);
                article.setBody(encryptedBody); // Save the encrypted body
            }
            
            PreparedStatement stmt;
            if (articleExists) {
                stmt = connection.prepareStatement(updateSQL);
                stmt.setString(1, article.getLevel());
                stmt.setString(2, article.getTitle());
                stmt.setString(3, article.getDescription());
                stmt.setString(4, article.getKeywords());
                stmt.setString(5, article.getBody());
                stmt.setString(6, article.getReferenceLinks());
                stmt.setBoolean(7, article.isSensitive());
                stmt.setString(8, article.getGroups());
                stmt.setLong(9, article.getId());
            } else {
                stmt = connection.prepareStatement(insertSQL);
                stmt.setLong(1, article.getId());
                stmt.setString(2, article.getLevel());
                stmt.setString(3, article.getTitle());
                stmt.setString(4, article.getDescription());
                stmt.setString(5, article.getKeywords());
                stmt.setString(6, article.getBody());
                stmt.setString(7, article.getReferenceLinks());
                stmt.setBoolean(8, article.isSensitive());
                stmt.setString(9, article.getGroups());
            }



            stmt.executeUpdate();
            printDatabaseContent(); // Debug the database content
        } catch (SQLException e) {
            showMessage("Error saving article: " + e.getMessage(), true);
            e.printStackTrace();
            printDatabaseContent(); // Debug the database content.
        }
    }

    private void loadArticles() {
        String query = "SELECT * FROM articles ORDER BY id";
        articles.clear();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String level = resultSet.getString("level");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String keywords = resultSet.getString("keywords");
                String body = resultSet.getString("body");
                String referenceLinks = resultSet.getString("referenceLinks");
                boolean isSensitive = resultSet.getBoolean("isSensitive");
                String groups = resultSet.getString("groups");
                
                String username = UserSession.getInstance().getUsername();
                boolean hasAccess = H2Database.checkSpecialView(username);
                if (hasAccess) {
	                // Decrypt if sensitive	
	                if (isSensitive && body != null) {
	                    System.out.println("Encrypted Body After Loading: " + body);
                            try {
                                body = EncryptionManager.decrypt(body, EncryptionManager.getKey());
	                    } catch (Exception ex) {
	                        System.err.println(id + ex.getMessage());
	                    }
	                }
	
                }
                else {
                        if(EncryptionManager.isEncrypted(body, EncryptionManager.getKey())) {
                		body = "This article is encrypted!";
                	}
                }
           

                HelpArticle article = new HelpArticle(id, level, title, description, keywords, body, referenceLinks, isSensitive, groups);
                articles.add(article);
                printDatabaseContent(); // Debug the database content.

            }
        } catch (SQLException e) {
            showMessage("Error loading articles: " + e.getMessage(), true);
            e.printStackTrace();
            printDatabaseContent(); // Debug the database content.

        }
    }

    
    public void deleteArticle(HelpArticle article) {
        String deleteSQL = "DELETE FROM articles WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setLong(1, article.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            showMessage("Error deleting article: " + e.getMessage(), true);
        }
    }

    private void updateArticleList() throws Exception {
        loadArticles();
    }

    private String generateNewId() {
        // Implement generating a new unique ID
        return String.valueOf(System.currentTimeMillis()); // Example: using current time
    }
}

// Helper class to represent a Help Article
class HelpArticle {
    private long id;
    private String level;
    private String title;
    private String description;
    private String keywords;
    private String body;
    private String links;
    private boolean sensitive;
    private String groups;

    public HelpArticle(long articleId, String level, String title, String description, String keywords, String body, String referenceLinks, boolean isSensitive, String groups) {
        this.id = articleId;
        this.level = level;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.body = body;
        this.links = referenceLinks;  
        this.sensitive= isSensitive;
        this.groups = groups;
    }
    
    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public String getReferenceLinks() { return links; }
    public void setReferenceLinks(String referenceLinks) { this.links = referenceLinks; }
    
    public boolean isSensitive() { return sensitive; }
    public void setSensitive(boolean sensitive) { this.sensitive = sensitive; }
    
    public String getGroups() { return groups; }
    public void setGroups(String groups) { this.groups = groups; } 

    @Override
    public String toString() {
        return title; // This will be displayed in the ListView
    }
}
