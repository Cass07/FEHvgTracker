package wiki.feh.apitest.domain.vginfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VgInfoRepository extends JpaRepository<VgInfo, Long>{
}
