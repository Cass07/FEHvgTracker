package wiki.feh.apitest.domain.vgdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VgDataRepository extends JpaRepository<VgData, Long> {


}
