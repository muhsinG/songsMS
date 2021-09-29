package SBApp.Repo;


import SBApp.Model.Song;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//@Qualifier("songs")
@Repository
public interface SongRepository extends CrudRepository<Song, Integer> {
}
