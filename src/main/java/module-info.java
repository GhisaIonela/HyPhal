module com.example.networkgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.networkgui to javafx.fxml;
    exports com.example.networkgui;
    exports com.example.networkgui.mainPage;
    opens com.example.networkgui.mainPage to javafx.fxml;

    requires Lab4;
}