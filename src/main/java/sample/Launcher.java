package sample;

public class Launcher {

    public static void main(String[] args) throws Exception {
        System.setProperty("prism.vsync", "false");
        OpenGLFXGearSample.main(args);
    }
}
