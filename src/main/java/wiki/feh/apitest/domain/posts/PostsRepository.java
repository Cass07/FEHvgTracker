package wiki.feh.apitest.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

///JpaRepository<Entity 클래스, Primary Key 자료형>
public interface PostsRepository extends JpaRepository<Posts, Long>{

    /*
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDecs();
     */
}
