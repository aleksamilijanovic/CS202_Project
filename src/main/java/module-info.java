module org.example.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.gson;
    requires jaudiotagger;

    opens org.example.musicplayer to javafx.fxml;
    exports org.example.musicplayer;
    exports org.example.musicplayer.song_searching;
    opens org.example.musicplayer.song_searching to javafx.fxml;
}