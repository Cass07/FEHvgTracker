package wiki.feh.apitest.domain.vgdata;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static wiki.feh.apitest.domain.vgdata.QVgData.vgData;

@RequiredArgsConstructor
@Repository
public class VgDataQueryRepository {
    private final int[] LIMIT_NUM = {1,4,2,1};
    private final JPAQueryFactory queryFactory;

    //특정 vgnumber, roundnumber, tourIndex 로 조회되는 제일 최신의 vgdata
    public VgData getLatestVgDataByNumRoundTour(int vgNumber, int roundNumber, int tournamentIndex)
    {
        return queryFactory
                .selectFrom(vgData)
                .where(vgData.vgNumber.eq(vgNumber), vgData.roundNumber.eq(roundNumber), vgData.tournamentIndex.eq(tournamentIndex))
                .orderBy(vgData.timeIndex.desc())
                .fetchFirst();
    }

    //특정 vgNumber로 조회되는 전체 라운드의 첫번쨰 vgdata 리스트 (초동 데이터 출력용)
    public List<VgData> getFirstVgDataListByVgNumber(int vgNumber)
    {
        return queryFactory
                .selectFrom(vgData)
                .where(vgData.timeIndex.eq(1), vgData.vgNumber.eq(vgNumber))
                .orderBy(vgData.roundNumber.desc(), vgData.tournamentIndex.asc())
                .fetch();
    }

    //특정 vgnumber, roundnumber, tourindex로 조회되는 제일 처음의 vgdata
    public Optional<VgData> getFirstVgDataByNumRoundTour(int vgNumber, int roundNumber, int tournamentIndex)
    {
        return Optional.ofNullable(queryFactory
                .selectFrom(vgData)
                .where(vgData.vgNumber.eq(vgNumber), vgData.roundNumber.eq(roundNumber), vgData.tournamentIndex.eq(tournamentIndex))
                .orderBy(vgData.timeIndex.asc())
                .fetchFirst());
    }

    //특정 vgnumber로 조회되는 종료된 라운드의 마지막 vgdata 리스트 (결과표 출력용)
    public List<VgData> getLatestVgDataListByVgNumber(int vgNumber)
    {
        return queryFactory
                .selectFrom(vgData)
                .where(vgData.timeIndex.eq(45), vgData.vgNumber.eq(vgNumber))
                .orderBy(vgData.roundNumber.desc(), vgData.tournamentIndex.asc())
                .fetch();
    }

    //특정 vgnumber, round로 조회되는 제일 최신의 vgdata리스트 4~1개(라운드따라) (현재 상황표 출력용)
    public List<VgData> getLatestVgDataListByVgNumberRound(int vgNumber, int roundNumber)
    {
        return queryFactory
                .selectFrom(vgData)
                .where(vgData.vgNumber.eq(vgNumber), vgData.roundNumber.eq(roundNumber))
                .orderBy(vgData.timeIndex.desc(), vgData.tournamentIndex.asc())
                .limit(LIMIT_NUM[roundNumber])
                .fetch();
    }

    //특정 vgnumber, roundnumber, tourindex로 조회되는 vgdata 리스트
    public List<VgData> getVgDataListByNumRoundTour(int vgNumber, int roundNumber, int tournamentIndex)
    {
        return queryFactory
                .selectFrom(vgData)
                .where(vgData.vgNumber.eq(vgNumber), vgData.roundNumber.eq(roundNumber), vgData.tournamentIndex.eq(tournamentIndex))
                .orderBy(vgData.timeIndex.asc())
                .fetch();
    }

}
