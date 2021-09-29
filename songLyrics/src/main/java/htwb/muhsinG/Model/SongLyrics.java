package htwb.muhsinG.Model;

import javax.persistence.*;

@Entity
@Table(name = "songlyrics")
public class SongLyrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String title;
    private String artist;
    private String lyrics;

    public SongLyrics(Builder builder) {
        this.id=id;
        this.title=title;
        this.artist=artist;
        this.lyrics=lyrics;
    }

    public SongLyrics(){

    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", lyrics='" + lyrics + '\'' +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int getId() {return id;}
    public void setId(int id) {
        this.id=id;
    }

    @Column(name="title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name="artist")
    public String getArtist() { return artist; }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Column(name="lyrics")
    public String getLyrics() {return lyrics;}
    public void setLyrics(String lyrics){this.lyrics=lyrics;}

    public static final class Builder {
        private int id;
        private String title;
        private String artist;
        private String lyrics;

        private Builder() {
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }


        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public Builder withLabel(String lyrics) {
            this.lyrics = lyrics;
            return this;
        }

        public SongLyrics build() {
            return new SongLyrics(this);
        }
    }
}
