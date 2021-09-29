package htwb.muhsinG.Repo;

import htwb.muhsinG.Model.SongLyrics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Qualifier("songlyrics")
@Repository
public interface SongLyricsRepository extends CrudRepository<SongLyrics, Integer> {

    @Query(value = "SELECT * FROM de2cfuvuu0ap4k.public.songlyrics WHERE id = ?1", nativeQuery = true)
    List<SongLyrics> findbyId(Integer id);

}
