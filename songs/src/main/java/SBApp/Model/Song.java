package SBApp.Model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String title;
    private String artist;
    private String label;
    private int released;

    @ManyToMany(mappedBy = "song", cascade = CascadeType.PERSIST)
    Set<SongList> songListSet;

    public Song(Builder builder) {
        this.id=id;
        this.title=title;
        this.artist=artist;
        this.label=label;
        this.released=released;
    }

    public Song() {

    }

    public Song(int id, String title, String artist, String label, int released) {
        this.id=id;
        this.title=title;
        this.artist=artist;
        this.label=label;
        this.released=released;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int getId() {
        return id;
    }

    @Column(name="title")
    public String getTitle() {
        return title;
    }

    @Column(name="artist")
    public String getArtist() {
        return artist;
    }

    @Column(name="label")
    public String getLabel() {
        return label;
    }

    @Column(name="released")
    public int getReleased() {
        return released;
    }

//	public Set<SongList> getSongListSet(){
//		return songListSet;
//	}

    public void setId(int id) {
        this.id=id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setReleased(int released) {
        this.released = released;
    }

//	public void addSongList(SongList songList) {
//		songListSet.add(songList);
//		songList.getSongList().add(this);
//	}

    @Override
    public java.lang.String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", label='" + label + '\'' +
                ", released=" + released +
                ", songListSet=" + songListSet +
                '}';
    }

    /**
     * Creates builder to build {@link Song}.
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link Song}.
     */
    public static final class Builder {
        private int id;
        private String title;
        private String artist;
        private String label;
        private int released;

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

        public Builder withLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder withReleased(int released) {
            this.released = released;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }
}
