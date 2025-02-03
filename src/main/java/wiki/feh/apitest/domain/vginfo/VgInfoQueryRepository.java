package wiki.feh.apitest.domain.vginfo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static wiki.feh.apitest.domain.vginfo.QVgInfo.vgInfo;

@RequiredArgsConstructor
@Repository
public class VgInfoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<VgInfo> getLatestVgInfo()
    {
        return Optional.ofNullable(queryFactory
                .selectFrom(vgInfo)
                .orderBy(vgInfo.vgNumber.desc())
                .fetchFirst());
    }

    public List<VgInfo> findAllDesc()
    {
        return queryFactory
                .selectFrom(vgInfo)
                .orderBy(vgInfo.id.desc())
                .fetch();
    }

    public VgInfo findByVgNumber(int vgNumber)
    {
        return queryFactory
                .selectFrom(vgInfo)
                .where(vgInfo.vgNumber.eq(vgNumber))
                .fetchFirst();
    }

}
