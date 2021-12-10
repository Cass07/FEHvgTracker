package wiki.feh.apitest.domain.herolist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeroListRepository extends JpaRepository<HeroList, String> {

    List<HeroList> findByJpname(String jpname);
}
