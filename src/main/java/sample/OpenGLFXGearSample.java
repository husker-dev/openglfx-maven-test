package sample;

import com.huskerdev.openglfx.canvas.GLCanvas;
import com.huskerdev.openglfx.canvas.GLCanvasAnimator;
import com.huskerdev.openglfx.canvas.GLProfile;
import com.huskerdev.openglfx.lwjgl.LWJGLExecutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.lwjgl.opengl.GL11;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class OpenGLFXGearSample extends Application {

	private Gears renderable;
	
	private Label fpsLabel;

	@Override
	public void start(Stage stage) throws Exception {
		VBox vBox = new VBox();
		vBox.setFillWidth(true);
		/* Root Node */
		StackPane root = new StackPane();

		fpsLabel = new Label("");
		vBox.getChildren().add(fpsLabel);

		/* Create OpenGLCanvas underneath */
		root.getChildren().add(createGLPane());

		/* Add Label On top */
		root.getChildren().add(createLabel());
		vBox.getChildren().add(root);
		VBox.setVgrow(root, Priority.ALWAYS);
		
		/* Setup Scene */
		stage.setScene(new Scene(vBox, 320, 240));
		stage.show();
	}
	
	private Label createLabel() {
		Label label = new Label("JavaFX rendering on top. LWJGL rendering below");
		label.setBackground(new Background(new BackgroundFill(new Color(0, 0.5, 0.9, 0.5), null, null)));
		return label;
	}

	private GLCanvas createGLPane() {
		GLCanvas glCanvas = new GLCanvas(LWJGLExecutor.LWJGL_MODULE, GLProfile.Core);

		glCanvas.addOnInitEvent((event)->{
			renderable = new Gears();
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

		});


		glCanvas.addOnRenderEvent((event)-> {
			Platform.runLater(()->{
				fpsLabel.setText("fps: " + event.fps);
			});



			double width = glCanvas.getWidth();
			double height = glCanvas.getHeight();

			double aspect = height/width;

			final float[] pos = {5.0f, 5.0f, 10.0f, 0.0f};
			GL11.glPushMatrix();
            GL11.glMatrixMode(GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glFrustum(-1.0, 1.0, -aspect, aspect, 5.0, 60.0);
            GL11.glMatrixMode(GL_MODELVIEW);
            GL11.glTranslatef(0.0f, 0.0f, -40.0f);

            GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Lighting
            {
                GL11.glLightfv(GL_LIGHT0, GL_POSITION, pos);
                GL11.glEnable(GL_LIGHTING);
                GL11.glEnable(GL_LIGHT0);
            }

            // Reset opengl flags
            {
                GL11.glCullFace(GL_BACK);
                GL11.glEnable(GL_CULL_FACE);
                GL11.glEnable(GL_DEPTH_TEST);
            }

            renderable.onRender();


			GL11.glPopMatrix();
		});




		glCanvas.setAnimator(new GLCanvasAnimator(60));
//
		return glCanvas;
	}



	

	public static void main(String[] args) {
		System.setProperty("prism.order", "es2,d3d,sw");
		System.setProperty("prism.vsync", "false");
		launch(args);
	}
}