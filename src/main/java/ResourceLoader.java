import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public final class ResourceLoader {
    private ResourceLoader() {}

    public static BufferedImage loadImage(String path) throws IOException {
        if (path == null) throw new IOException("Resource path is null");
        if (!path.startsWith("/")) path = "/" + path;
        InputStream in = ResourceLoader.class.getResourceAsStream(path);
        if (in == null) {
            throw new IOException("Resource not found: " + path);
        }
        try {
            return ImageIO.read(in);
        } finally {
            try { in.close(); } catch (IOException ignore) {}
        }
    }

    public static Clip loadClip(String path) throws Exception {
        if (path == null) throw new IOException("Resource path is null");
        if (!path.startsWith("/")) path = "/" + path;
        URL url = ResourceLoader.class.getResource(path);
        if (url == null) throw new IOException("Audio resource not found: " + path);
        AudioInputStream ais = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        return clip;
    }
}
