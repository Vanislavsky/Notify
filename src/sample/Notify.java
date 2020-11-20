package sample;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

enum Position {
    RIGHT_BOTTOM,
    RIGHT_TOP,
    LEFT_BOTTOM,
    LEFT_TOP
}

enum View {
    SIMPLE,
    WITHBUTTON,
    WITHBUTTONANDFIELD,
    WITHBUTTONANDLIST,
}

enum Border {
    CIRCLE,
    RECTANGLE
}

class Config {
    public Double defWidth = 300.0;
    public Double defHeight = 140.0;
    public Double shift = 10.0;
    public Position position = Position.RIGHT_BOTTOM;
    public View view = View.SIMPLE;
    public String title = "";
    public String message = "";
    public String appName = "";
    public Border iconBorder = Border.RECTANGLE;
    public String iconPath = "";
    public String musicPath = "file:/Users/sergejvanislavskij/Desktop/icq-z_uk-nostalgii_-_soobschenie-prishlo_.mp3";
    public String bgColor = "#ffffff";
    public String appColor = "#000000";
    public String titleColor = "#000000";
    public String msqColor = "#000000";
    public Integer waitTime = 7000;
    public Double bgOpacity = 0.9;
    public ArrayList<Button> buttnos = null;
    public TextField textField = null;
    public ComboBox<Label> list = null;
}

public class Notify {
    Config config = new Config();
    final private Stage window = new Stage();
    final private BorderPane panel = new BorderPane();
    final private HBox content = new HBox();

    public static class Builder {
        private Notify newNotify;

        public Builder() {
            newNotify = new Notify();
        }

        public Builder withDefWidth(Double defWidth) {
            newNotify.config.defWidth = defWidth;
            return this;
        }

        public Builder withDefHeight(Double defHeight) {
            newNotify.config.defHeight = defHeight;
            return this;
        }

        public Builder withPosition(Position position) {
            newNotify.config.position = position;
            return this;
        }

        public Builder withView(View view) {
            newNotify.config.view = view;
            return this;
        }

        public Builder withTitle(String title) {
            newNotify.config.title = title;
            return this;
        }

        public Builder withMessage(String message) {
            newNotify.config.message = message;
            return this;
        }

        public Builder withAppName(String appName) {
            newNotify.config.appName = appName;
            return this;
        }

        public Builder withIconPath(String iconPath) {
            newNotify.config.iconPath = iconPath;
            return this;
        }

        public Builder withBorder(Border border) {
            newNotify.config.iconBorder = border;
            return this;
        }

        public Builder withButtons(Button button, EventHandler func) {
            if(newNotify.config.buttnos == null)
                newNotify.config.buttnos = new ArrayList<Button>();
            button.setOnAction(func);
            newNotify.config.buttnos.add(button);
            return this;
        }

        public Builder withCanselButton(String buttonString) {
            Button cansel = new Button(buttonString);
            cansel.setOnAction(actionEvent -> {
                newNotify.window.close();
            });

            newNotify.config.buttnos.add(cansel);
            return this;
        }

        public Builder withTextField() {
            newNotify.config.textField = new TextField();
            if(newNotify.config.buttnos == null)
                newNotify.config.buttnos = new ArrayList<Button>();
            Button ok = new Button("Ok");
            ok.setOnAction(actionEvent ->{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, newNotify.config.textField.getText(), ButtonType.YES);
                newNotify.config.textField.clear();
                alert.showAndWait();
            });

            newNotify.config.buttnos.add(ok);
            return this;
        }

        public Builder withCombobox(Label... labels) {
            newNotify.config.list = new ComboBox<Label>();
            if(newNotify.config.buttnos == null)
                newNotify.config.buttnos = new ArrayList<Button>();

            Button ok = new Button("Ok");
            ok.setOnAction(actionEvent ->{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, newNotify.config.list.getValue().getText(), ButtonType.YES);
                alert.showAndWait();
            });

            for(Label label: labels) {
                newNotify.config.list.getItems().addAll(label);
            }
            return this;
        }

        public Builder withBgColor(String color) {
            newNotify.config.bgColor = color;
            return this;
        }

        public Builder withAppColor(String color) {
            newNotify.config.appColor = color;
            return this;
        }

        public Builder withTitleColor(String color) {
            newNotify.config.titleColor = color;
            return this;
        }

        public Builder withMsgColor(String color) {
            newNotify.config.msqColor = color;
            return this;
        }

        public Builder withWaitTime(Integer time) {
            newNotify.config.waitTime = time;
            return this;
        }

        public Notify build(){
            newNotify.buildwindow();
            return newNotify;
        }
    }

    private void setImage() {
        if(!config.iconPath.isEmpty()) {
            Shape iconBorder;
            if(config.iconBorder == Border.CIRCLE) {
                iconBorder = new Circle(config.defHeight / 2, config.defHeight / 2, config.defHeight / 2 - 25);
            } else {
                iconBorder = new Rectangle(config.defHeight / 2 , config.defHeight /2, config.defHeight - 50, config.defHeight - 50);
            }
            try {
                iconBorder.setFill(new ImagePattern(new Image(new FileInputStream(config.iconPath))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            content.getChildren().addAll(iconBorder);
        }
    }

    private void buildContent() {
        panel.setStyle("-fx-background-color:" + config.bgColor);
        panel.setPrefSize(config.defWidth, config.defHeight);
        content.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        content.setSpacing(5.0);
        var vbox = new VBox();
        vbox.setSpacing(10);

        var title = new Label(config.title);
        title.setStyle("-fx-font-weight: bold; ");
        title.setFont(new Font(20.0));
        title.setStyle("-fx-text-fill: " + config.titleColor);

        var msg = new Label(config.message);
        msg.setFont(new Font(14.0));
        msg.setStyle("-fx-font-family: Helvetica;");
        msg.setStyle("-fx-text-fill: " + config.msqColor);
        msg.setOpacity(0.8);

        var app = new Label(config.appName);
        app.setFont(new Font(24.0));
        app.setStyle("-fx-font-weight: bold; ");
        app.setStyle("-fx-text-fill: " + config.appColor);

        setImage();
        vbox.getChildren().addAll(app,title, msg);
        vbox.setSpacing(2);
        content.getChildren().addAll(vbox);

        HBox managementObjects = new HBox();
        managementObjects.setAlignment(Pos.CENTER);
        managementObjects.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        managementObjects.setSpacing(5.0);

        for (Button button: config.buttnos) {
            managementObjects.getChildren().addAll(button);
        }

        if(config.textField != null) {
            managementObjects.getChildren().addAll(config.textField);
        }

        if(config.list != null) {
            managementObjects.getChildren().addAll(config.list);
        }

        panel.setCenter(content);
        panel.setBottom(managementObjects);
    }

    private void buildwindow() {
        buildContent();
        // buildview();
        window.initStyle(StageStyle.UNDECORATED);
        var screenRect = Screen.getPrimary().getBounds();


        switch (config.position) {
            case LEFT_BOTTOM -> {
                window.setX(0 - config.defWidth);
                window.setY(screenRect.getHeight() - config.defHeight - config.shift - 50);
            }
            case LEFT_TOP -> {
                window.setX(0 - config.defWidth);
                window.setY(config.shift);
            }
            case RIGHT_BOTTOM -> {
                window.setX(screenRect.getWidth());
                window.setY(screenRect.getHeight() - config.defHeight - config.shift - 50);
            }
            case RIGHT_TOP -> {
                window.setX(screenRect.getWidth());
                window.setY(config.shift);
            }
        }

        window.setScene(new Scene(panel, config.defWidth, config.defHeight));

        Thread thread = new Thread(() -> {
            try {
               Thread.sleep(config.waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            closeAnimation();
            exitAnimation();
            //Platform.runLater(() -> window.close());
        });

        new Thread(thread).start();
    }


    void show() {
        window.show();
        entranceAnimation();
        openAnimation();
        runMusic();
    }

    private void runMusic() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(config.musicPath));
        mediaPlayer.play();
    }

    void entranceAnimation() {
        Timer animTimer = new Timer();
        animTimer.scheduleAtFixedRate(new TimerTask() {
            Double i = window.getX();

            @Override
            public void run() {
                if(config.position == Position.LEFT_TOP || config.position == Position.LEFT_BOTTOM) {
                    if (i >= config.shift)
                        return;
                    window.setX(++i);
                } else  {
                    if (i <= Screen.getPrimary().getBounds().getWidth() - config.defWidth - config.shift)
                        return;
                    window.setX(--i);
                }
            }

        }, 0, 5);
    }

    void exitAnimation() {
        Timer animTimer = new Timer();
        animTimer.scheduleAtFixedRate(new TimerTask() {
            Double i= window.getX();

            @Override
            public void run() {
                if(config.position == Position.LEFT_TOP || config.position == Position.LEFT_BOTTOM) {
                    if (i <= config.defWidth) {
                        Platform.runLater(() -> window.close());
                        return;
                    }
                    window.setX(--i);
                } else  {
                    if (i >= Screen.getPrimary().getBounds().getWidth()) {
                        Platform.runLater(() -> window.close());
                        return;
                    }
                    window.setX(++i);
                }
            }

        }, 0, 5);
    }


    void openAnimation() {
        var ft = new FadeTransition(Duration.millis(1500.0), panel);
        ft.setFromValue(0.0);
        ft.setToValue(config.bgOpacity);
        ft.setCycleCount(1);
        ft.play();
    }

    void closeAnimation() {
        var ft = new FadeTransition(Duration.millis(1500.0), content);
        ft.setFromValue(config.bgOpacity);
        ft.setToValue(0);
        ft.setCycleCount(1);
        ft.play();
    }

}
