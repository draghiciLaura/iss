module org.example.issproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.issproject to javafx.fxml;
    exports org.example.issproject;
}