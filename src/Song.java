import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

public class Song implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String title;
    private String artist;
    private int rate;


    public Song (String title, String artist, int rate) {
        this.id = generateId(title,artist);
        this.title = title;
        this.artist = artist;
        this.rate = rate;
    }

    //used in the merge
    public Song(UUID id, String title, String artist, int rate) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.rate = rate;
    }

    public static UUID generateId(String title, String artist) {
        String normalized = (title.trim() +'|'+ artist.trim()).toLowerCase();
        return UUID.nameUUIDFromBytes(normalized.getBytes(StandardCharsets.UTF_8));

    }
   //Pear jam  != Pear  jam
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        return id.equals(((Song) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }


    @Override
    public String toString() {
        int safeRate = Math.max(0, Math.min(rate, 5));
        String filled = "★".repeat(safeRate);
        String empty = "☆".repeat(5 - safeRate);
        return title + " - " + artist + " (" + filled + empty + ")";
    }
}
