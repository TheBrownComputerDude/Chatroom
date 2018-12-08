import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChatWindow extends Application {
    private String name;
    private Pane layout;
    private Pane menu;
    private ScrollPane chat;
    private VBox holder = new VBox(5);
    private Client client;
    private Thread thread;
    private boolean running = true;
    private Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Chat Window");
        layout = new Pane();
        menu = new Pane();
        Scene startScene = new Scene(menu,250,140);
        mainScene = new Scene(layout,500,390);
        setUpStartScene(stage);
        stage.setScene(startScene);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(event -> {
            client.sendMessage(true);
            stage.close();
        });
    }
    private void setUpStartScene(Stage stage) {
        Label nameLabel = new Label("Enter Name:");
        nameLabel.setTranslateX(20);
        nameLabel.setTranslateY(82);

        Label IPLabel = new Label("Enter IP:");
        IPLabel.setTranslateX(42);
        IPLabel.setTranslateY(22);

        Label PortLabel = new Label("Enter Port:");
        PortLabel.setTranslateX(30);
        PortLabel.setTranslateY(52);


        TextField IP = new TextField();
        IP.setTranslateX(90);
        IP.setTranslateY(20);

        TextField Port = new TextField();
        Port.setTranslateX(90);
        Port.setTranslateY(50);

        TextField name = new TextField();
        name.setTranslateX(90);
        name.setTranslateY(80);

        Button submit = new Button("Enter Room");
        submit.setTranslateX(100);
        submit.setTranslateY(110);
        submit.setOnAction(event -> {
            if(!IP.getText().equals("") && !Port.getText().equals("") && !name.getText().equals("")) {
                String ip = IP.getText();
                this.name = name.getText();
                try {
                    int port = Integer.parseInt(Port.getText());
                    client = new Client(ip, port);
                    addToMainScene();
                    readMessages();
                    stage.setScene(mainScene);


                } catch (Exception e) {

                }
            }
        });

        menu.getChildren().addAll(IP,Port,name,submit,nameLabel,PortLabel,IPLabel);
    }
    private void readMessages(){
        Runnable task = () -> {
                while(running){
                    Message msg = client.getMessage();
                    String string = msg.toString();
                    StringBuilder sb = new StringBuilder(string);
                    sb.insert(0, msg.getFrom() + " : ");
                    for(int i = 1; i <= sb.length() / 33; i++){
                        sb.insert(34*i,'\n');
                    }
                    Label x = new Label(sb.toString());
                    x.setFont(new Font(25));
                    x.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
                    x.setAlignment(Pos.CENTER_LEFT);
                    x.setPrefWidth(460);
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            holder.getChildren().add(x);
                        }
                    });

                }

        };
        thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    private void addToMainScene(){
        for(int i = 0; i < 2; i++) {
            Line line = new Line(10, 10 + i* 340, 490, 10 + i * 340);
            Line line2 = new Line(10 + i * 480,10,10 + i * 480,350);
            layout.getChildren().addAll(line,line2);
        }

        chat = new ScrollPane();
        chat.setContent(holder);
        chat.setPrefSize(480,340);
        chat.setTranslateX(10);
        chat.setTranslateY(10);

        TextField input = new TextField();
        input.setPrefWidth(430);
        input.setTranslateX(10);
        input.setTranslateY(360);

        Button button = new Button("Send");
        button.setTranslateY(360);
        button.setTranslateX(450);
        button.setOnAction(e -> {
            if(!input.getText().equals("")){
                StringBuilder sb = new StringBuilder(input.getText());
                client.sendMessage(sb.toString(),name);
                for(int i = 1; i <= sb.length() / 33; i++){
                    sb.insert(34*i,'\n');
                }
                sb.insert(0, "Me: ");
                Label x = new Label(sb.toString());
                x.setFont(new Font(25));
                x.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
                x.setAlignment(Pos.CENTER_RIGHT);
                x.setPrefWidth(460);
                input.clear();
                holder.getChildren().add(x);
            }
        });
        input.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                button.fire();
                ev.consume();
            }
        });
        layout.getChildren().addAll(chat, input,button);
    }
    public static void main(String[] args){
        launch(args);
    }
}