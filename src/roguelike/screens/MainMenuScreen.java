package roguelike.screens;

import java.awt.*;
import com.valkryst.VTerminal.Panel;
import com.valkryst.VTerminal.builder.component.ButtonBuilder;
import com.valkryst.VTerminal.builder.component.ScreenBuilder;
import com.valkryst.VTerminal.component.Button;
import com.valkryst.VTerminal.component.Screen;
import lombok.Getter;

public class MainMenuScreen extends Screen {

    @Getter
    private Button button_new;

    private final static ScreenBuilder screenBuilder = new ScreenBuilder();
    static{
        screenBuilder.setWidth(88);
        screenBuilder.setHeight(32);
        screenBuilder.setRowIndex(0);
        screenBuilder.setColumnIndex(0);
    }

    public MainMenuScreen(final Panel panel) {
        super(screenBuilder);

        this.setBackgroundColor(Color.BLACK);
        String title = "Dungeons of Elentria";
        int midpoint = title.length() / 2;
        this.write(title, panel.getWidthInCharacters() / 2 - midpoint, panel.getHeightInCharacters() / 8);

        // Construct menu options:
        final ButtonBuilder builder = new ButtonBuilder();
        builder.setText("New Game");
        builder.setRadio(panel.getRadio());
        builder.setBackgroundColor_normal(Color.black);
        builder.setForegroundColor_normal(Color.white);
        builder.setColumnIndex(panel.getWidthInCharacters() / 2 - 4);
        builder.setRowIndex(panel.getHeightInCharacters() / 2 + panel.getHeightInCharacters() / 4);

        button_new = builder.build();

        // Swap Screen:
        panel.swapScreen(this);

        // Add components to Screen VIA Panel functions:
        panel.addComponent(button_new);

        this.getButton_new().setOnClickFunction(() -> {
            System.out.println("Yay!");
        });
    }
}
