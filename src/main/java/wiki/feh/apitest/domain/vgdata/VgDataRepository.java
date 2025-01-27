package wiki.feh.apitest.domain.vgdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VgDataRepository extends JpaRepository<VgData, Long> {
    Optional<VgData> findByVgNumberAndRoundNumberAndTournamentIndexAndTimeIndex(int vgNumber, int roundNumber, int tournamentIndex, int timeIndex);
}
