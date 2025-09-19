// Ожидаемый интерфейс
interface MediaPlayer {
    void play(String file);
}

// Существующий класс, который мы не можем менять
class MP3Player {
    public void playMP3(String file) {
        System.out.println("Играем MP3: " + file);
    }
}

// Адаптер — "переводит" вызовы в нужный формат
class MP3Adapter implements MediaPlayer {
    private MP3Player player = new MP3Player();

    @Override
    public void play(String file) {
        player.playMP3(file); // адаптируем метод
    }
}

public class Adapter {
    public static void main(String[] args) {
        // Используем адаптер как будто это MediaPlayer
        MediaPlayer player = new MP3Adapter();
        player.play("song.mp3");
    }
}
