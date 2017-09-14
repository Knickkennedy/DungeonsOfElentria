package roguelike.applications;

import java.io.IOException;
import java.net.URISyntaxException;

import com.valkryst.VTerminal.Panel;
import com.valkryst.VTerminal.builder.PanelBuilder;
import com.valkryst.VTerminal.font.Font;
import com.valkryst.VTerminal.font.FontLoader;
import roguelike.mob.Colors;
import roguelike.screens.*;

public class ApplicationMain{
	public static void main(final String[] args) throws IOException, URISyntaxException, InterruptedException {
		final Font font = FontLoader.loadFontFromJar("Fonts/DejaVu Sans Mono/18pt/bitmap.png",
				"Fonts/DejaVu Sans Mono/18pt/data.fnt",
				1);

		final PanelBuilder builder = new PanelBuilder();
		builder.setFont(font);
		builder.setWidthInCharacters(88);
		builder.setHeightInCharacters(32);

		final Panel panel = builder.build();

		Thread.sleep(50);

		final MainMenuScreen mainMenu = new MainMenuScreen(panel);
		panel.draw();

	}
}
