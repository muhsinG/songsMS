package SBApp.Repo;

import SBApp.Model.SongList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Qualifier("songLists")
@Repository
public interface SongListRepository extends CrudRepository<SongList, Integer> {

    @Query(value = "SELECT * FROM d83uiprov5lh59.public.songList WHERE ownerid = ?1", nativeQuery = true)
    List<SongList> findByOwnerId(String ownerid);

    @Query(value = "SELECT * FROM d83uiprov5lh59.public.songList WHERE ownerid = ?1 AND isprivate=false", nativeQuery = true)
    List<SongList> findByOwnerIdPublic(String ownerid);

    @Query(value = "SELECT * FROM d83uiprov5lh59.public.songList WHERE isprivate=false", nativeQuery = true)
    List<SongList> findAllPublic();

    @Query(value = "SELECT * FROM d83uiprov5lh59.public.songList WHERE songlistid = ?1", nativeQuery = true)
    List<SongList> findBySongListId(int id);
}